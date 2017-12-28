package sample;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
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
        System.err.println("usage: java WatchDir [-r] dir");
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
//        if(eventChild.contains(separator)) {
//            String[] eC = eventChild.split(Pattern.quote(separator));
//            nvPID = eC[eC.length-1];
//        }else{
//            System.out.println("standart pid");
//            nvPID = eventChild;
//        }
        String[] eC = eventChild.split(Pattern.quote(separator));
        nvPID = eC[eC.length-1];
        System.out.println("1+"+ENTRY_MODIFY.name());

        if(eventName.equals(ENTRY_MODIFY.name()) && nvPID.equals("NewVisionPID.txt")){
            System.out.println("2");
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
        private ArrayList<String> queue = new ArrayList<>();
        public void poll(String eventName){
            if(queue.size()==2){

            }

        }


        private void remove(){

        }
    }
}


