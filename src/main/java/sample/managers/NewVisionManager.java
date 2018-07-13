package sample.managers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.Timer;

import sample.Archiver;
import sample.DirManager;
import sample.TasksClass;
import sample.WatchDir;
import sample.parameters.INewVisionParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.requests.IHttpRequester;
import sample.requests.Post;


public class NewVisionManager implements IManager {
    private String jsonFolderPath;
    private String newVisionPath;
    private String profileName;
    private Timer timerNewVisionWorkManager;
    private Timer timerDirManager;
    private final String NVpidPath = "NewVisionPID.txt";
    private int PID = -1;
    private final String TASKLIST = "tasklist";
    private int jsonFolderPointer = 0;
    private ArrayList<String> jsonFoldersList;
    private static final String toProcessKey = "toProcess";
    private static final String completedKey = "completed";
    private DirManager dirManager = new DirManager();
    private WatchDir watchDir;
    private Thread watchDirThread;
    private Logger logger = LogManager.getLogger("NVManager");
    private Archiver archiver = new Archiver();
    private Thread z;

    /**
     * @param params
     */
    public NewVisionManager(INewVisionParams params){
        this.jsonFolderPath = params.getJsonSource();
        this.newVisionPath = params.getNewVisionPath();
        this.profileName = params.getProfileName();
        this.timerNewVisionWorkManager = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("NV PID is: " + PID);
                if (jsonFoldersList != null && jsonFoldersList.size() != 0) {
                    String str;
                    if (!checkNewVisionWork() && jsonFolderPointer < jsonFoldersList.size()) {
                        if (jsonFolderPointer > 0) {
                            str = dirManager.replaceNamePart((String)jsonFoldersList.get(jsonFolderPointer - 1), "_toProcess", "");
                            System.out.println((String)jsonFoldersList.get(jsonFolderPointer-1)+" <-afterCut");
                            dirManager.renameFolder(jsonFolderPath, (String)jsonFoldersList.get(jsonFolderPointer - 1), str);
                            System.out.println(str);
                            try {
                                System.out.println(jsonFolderPath+ "/" +str);

                                String finalStr = str;
                                z = new Thread (new Runnable(){
                                    public void run(){
                                        try {
                                            archiver.Zip(jsonFolderPath + "/" + finalStr, jsonFolderPath + "/" + finalStr + ".zip");
                                            dirManager.renameFolder(jsonFolderPath,finalStr,(String)jsonFoldersList.get(jsonFolderPointer - 1));
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }}});
                                z.run();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                        }

                        try {
                            str = "cmd.exe /c start java -jar " + newVisionPath + " nogui " + profileName + " " + jsonFolderPath + "/" + (String)jsonFoldersList.get(jsonFolderPointer) + "/";
                            System.out.println(str + "\n" + (jsonFolderPointer + 1) + "/" + jsonFoldersList.size());
                            TasksClass.startTask(str);
                        } catch (Exception ee) {
                            logger.error(ee);
                            System.out.println(ee);
                        }

                        jsonFolderPointer++;
                    } else if (jsonFolderPointer >= jsonFoldersList.size() && !checkNewVisionWork()) {
                        str = dirManager.replaceNamePart((String)jsonFoldersList.get(jsonFolderPointer - 1), "_toProcess", "");
                        dirManager.renameFolder(jsonFolderPath, (String)jsonFoldersList.get(jsonFolderPointer - 1), str);
                        System.out.println(str);


                        String finalStr = str;
                        z = new Thread (new Runnable(){
                                public void run(){
                                    try {
                                        archiver.Zip(jsonFolderPath + "/" + finalStr, jsonFolderPath + "/" + finalStr + ".zip");
                                        dirManager.renameFolder(jsonFolderPath,finalStr,(String)jsonFoldersList.get(jsonFolderPointer - 1));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }}});
                        z.run();

                        try {
                           // archiver.Zip(jsonFolderPath + "/" + str, jsonFolderPath + "/" + str + "Zipped.zip");
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        jsonFoldersList.clear();
                    }
                } else {
                    jsonFoldersList = (ArrayList)dirManager.getJsonFoldersList(jsonFolderPath, "toProcess");
                    jsonFolderPointer = 0;
                }

            }
        });
    }

    public void start() {
        this.timerNewVisionWorkManager.start();
        this.watchDirThread = new Thread(new Runnable() {
            public void run() {
                Path path = Paths.get("");

                try {
                    watchDir = new WatchDir(path, false);
                    watchDir.addOnPIDChangeListener(new Runnable() {
                        public void run() {
                            loadPID();
                        }
                    });
                } catch (IOException e) {
                    logger.error(e);
                    e.printStackTrace();
                }

                watchDir.processEvents();
            }
        });
        this.watchDirThread.start();
    }

    public void stop() {
        this.timerNewVisionWorkManager.stop();
        this.watchDirThread.stop();
    }

    private void loadPID() {
        System.out.println("loadPid");
        String PIDstring = "";

            try(FileReader reader = new FileReader(NVpidPath))
            {
                // читаем посимвольно
                int c;
                while((c=reader.read())!=-1){
                    //System.out.println((char)c);
                    PIDstring = PIDstring.concat(String.valueOf((char)c));
                }
            }
            catch(IOException ex){
                logger.error(ex);
                System.out.println(ex.getMessage());
            }

            PID = Integer.parseInt(PIDstring);
        }

    private boolean checkNewVisionWork() {
        Process p = null;

        try {
            p = Runtime.getRuntime().exec(TASKLIST);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line;
        try {
            while((line = reader.readLine()) != null) {
                if (line.contains(String.valueOf(this.PID)) && line.contains("java.exe")) {
                    return true;
                }
            }
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }

        return false;
    }
}