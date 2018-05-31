package sample;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir {

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    private Long c = 0L;

    private String eventName = "";
    private String eventChild = "";

    private List<Runnable> onFinishEventHandlers = new ArrayList<>();
    private CreateAndModifyEventQueue createAndModifyEventQueue = new CreateAndModifyEventQueue();

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    public WatchDir(Path dir, boolean recursive) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;

        checkPIDChanging();
    }

    /**
     * Process all events for keys queued to the watcher
     */
    public void processEvents() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
                eventName = event.kind().name();
                eventChild = child.toString();
                checkPIDChanging();

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);
                String[] splitChild = child.toString().split("\\.");
                if(splitChild.length>1)
                    incCount();

                checkPIDChanging();
                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    static void usage() {
        System.err.println("usage: java sample.WatchDir [-r] dir");
        System.exit(-1);
    }

    public static void start(String[] args) throws IOException {
        // parse arguments
        if (args.length == 0 || args.length > 2)
            usage();
        boolean recursive = false;
        int dirArg = 0;
        if (args[0].equals("-r")) {
            if (args.length < 2)
                usage();
            recursive = true;
            dirArg++;
        }

        // register directory and process its events
        Path dir = Paths.get(args[dirArg]);
        new WatchDir(dir, recursive).processEvents();
    }

    private void checkPIDChanging(){
        String separator = "\\";
        String nvPID;

        String[] eC = eventChild.split(Pattern.quote(separator));
        nvPID = eC[eC.length-1];

        if(nvPID.equals("NewVisionPID.txt")){
            createAndModifyEventQueue.poll(eventName);
        }

        if(createAndModifyEventQueue.checkCreateAndModifyOrder()){
            createAndModifyEventQueue.resetQueue();
            fireOnPIDChange();
        }
    }

    /**
     * Adds listener onPIDChange event
     * @param listener Runnable with actions to do on PID changing
     */
    public void addOnPIDChangeListener(Runnable listener){
        onFinishEventHandlers.add(listener);
    }

    /**
     * Removes listener of onPIDChange
     * @param listener specifies listener to remove from listeners
     */
    public void removeOnPIDChangeListener(Runnable listener){
        onFinishEventHandlers.remove(listener);
    }

    private void fireOnPIDChange(){
        System.out.println("onPIDChange fired");
        for (Runnable r : onFinishEventHandlers){
            r.run();
        }
    }


    public String getEventName() {
        return eventName;
    }

    public String getEventChild() {
        return eventChild;

    }
    public void setCount(){
        this.c = 0L;
    }

    private void incCount(){
        this.c++;
    }

    public Long getCount(){
        return c;
    }

    private class CreateAndModifyEventQueue{

        public CreateAndModifyEventQueue() {
            queue.add(ENTRY_MODIFY.name());
            queue.add(ENTRY_CREATE.name());
        }

        private ArrayList<String> queue = new ArrayList<>();
        public void poll(String eventName){
            queue.set(1,queue.get(0));
            queue.set(0,eventName);

        }

        public boolean checkCreateAndModifyOrder(){
            if(queue.get(1)==ENTRY_CREATE.name() && queue.get(0)==ENTRY_MODIFY.name())
                return true;
            else
                return false;
        }
        public void resetQueue(){
            queue.set(0,"");
            queue.set(1,"");
        }
        public ArrayList<String> getQueue() {
            return queue;
        }
    }
}


