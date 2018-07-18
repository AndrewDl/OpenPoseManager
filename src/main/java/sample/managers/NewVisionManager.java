package sample.managers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
import sample.requests.DataForPostArchive;
import sample.requests.IRequestData;
import sample.requests.PostRequester;
import org.apache.commons.io.FileUtils;


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
    private Thread endLifeProcessThread;
    private PostRequester httpPOST = new PostRequester();
    private String postURL = "";
    private String getURL = "";
    private Boolean deleteJsonFolderFlag = false;
    private Boolean deleteJsonZipFlag = false;
    private DataForPostArchive requestData = new DataForPostArchive();

    /**
     * @param params
     */
    public NewVisionManager(INewVisionParams params){
        this.jsonFolderPath = params.getJsonSource();
        this.newVisionPath = params.getNewVisionPath();
        this.profileName = params.getProfileName();
        this.postURL = params.getURLforGET();
        this.getURL = params.getURLforPOST();
        this.deleteJsonFolderFlag = params.getDeleteProcessedJsonFolder();
        this.deleteJsonZipFlag = params.getDeleteUploadedZippedJsons();

        //TODO:PUSH IT TO THE LIMIT

        this.timerNewVisionWorkManager = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("NV PID is: " + PID);
                if (jsonFoldersList != null && jsonFoldersList.size() != 0) {
                    String str;
                    if (!checkNewVisionWork() && jsonFolderPointer < jsonFoldersList.size()) {
                        if (jsonFolderPointer > 0) {
                            str = dirManager.replaceNamePart((String)jsonFoldersList.get(jsonFolderPointer - 1), "_toProcess", "");
                            dirManager.renameFolder(jsonFolderPath, (String)jsonFoldersList.get(jsonFolderPointer - 1), str);
                            try {
                                String finalStr = str;
                                String finalName = finalStr+"_delete";
                                endLifeProcessThread = new Thread (new Runnable(){
                                    public void run(){
                                        try {
                                            archiver.zip(jsonFolderPath + "/" + finalStr, jsonFolderPath + "/" + finalStr + ".zip");
                                            //zip jsonFolder
                                            dirManager.renameFolder(jsonFolderPath,finalStr,finalName);
                                            //rename folder to "_delete" condition, to not allow
                                            requestData.setUrl(postURL);
                                            requestData.setName(finalStr);
                                            requestData.setFilepath(jsonFolderPath+"/"+finalStr+".zip");
                                            httpPOST.sendPOSTRequest(requestData);
                                            //uploading zip file on server
                                            //httpGET.getRequest(getURL,finalStr);
                                            if(deleteJsonFolderFlag){
                                                File folderToDelete = new File(jsonFolderPath+"/"+finalName);
                                                long start = System.currentTimeMillis();
                                                FileUtils.forceDelete(folderToDelete);
                                                //deleting folder
                                                long finish = System.currentTimeMillis();
                                                long timeConsumedMillis = finish - start;
                                                System.out.println("Time was taken: "+timeConsumedMillis/1000+"s");
                                                System.out.println("Folder "+finalStr+" was deleted!");
                                                logger.info("Folder "+finalStr+" was deleted!");
                                            }
                                            if(deleteJsonZipFlag){
                                                File zipToDelete = new File(jsonFolderPath+"/"+finalStr+".zip");
                                                zipToDelete.delete();
                                                System.out.println("zip "+finalStr+" was deleted!");
                                                logger.info("zip "+finalStr+" was deleted!");
                                            }
                                        } catch (Exception e1) {
                                            logger.error(e1);
                                            e1.printStackTrace();
                                        }}});
                                endLifeProcessThread.start();
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


                        String finalStr = str;
                        String finalName = finalStr+"_delete";
                        //comments for code above are actual for code under this line
                        endLifeProcessThread = new Thread (new Runnable(){
                                public void run(){
                                    try {
                                        archiver.zip(jsonFolderPath + "/" + finalStr, jsonFolderPath + "/" + finalStr + ".zip");
                                        dirManager.renameFolder(jsonFolderPath,finalStr,finalName);
                                        requestData.setUrl(postURL);
                                        requestData.setName(finalStr);
                                        requestData.setFilepath(jsonFolderPath+"/"+finalStr+".zip");
                                        httpPOST.sendPOSTRequest(requestData);
                                        // httpGET.getRequest(getURL,finalStr);
                                        if(deleteJsonFolderFlag){
                                            File folderToDelete = new File(jsonFolderPath+"/"+finalName);
                                            long start = System.currentTimeMillis();
                                            FileUtils.forceDelete(folderToDelete);
                                            long finish = System.currentTimeMillis();
                                            long timeConsumedMillis = finish - start;
                                            System.out.println("Time was taken: "+timeConsumedMillis/1000+"s");
                                            System.out.println("Folder "+finalStr+" was deleted!");
                                            logger.info("Folder "+finalStr+" was deleted!");
                                        }
                                        if(deleteJsonZipFlag){
                                            File zipToDelete = new File(jsonFolderPath+"/"+finalStr+".zip");
                                            zipToDelete.delete();
                                            System.out.println("zip "+finalStr+" was deleted!");
                                            logger.info("zip "+finalStr+" was deleted!");
                                        }
                                    } catch (Exception e1) {
                                        logger.error(e1);
                                        e1.printStackTrace();
                                    }}});
                        endLifeProcessThread.start();
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