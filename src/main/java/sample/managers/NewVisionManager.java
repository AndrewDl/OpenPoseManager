package sample.managers;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Archiver;
import sample.DirManager;
import sample.EventsProcessing.ArchiveLoader;
import sample.ParametersReader.ParametersReader;
import imageProcessing.SceneLineParams;
import imageProcessing.ScenePolygonParams;
import sample.ParametersReader.ProfileParameters;
import sample.TasksClass;
import sample.WatchDir;
import sample.parameters.INewVisionParams;
import sample.requests.DataForPostArchive;
import sample.requests.PostRequester;


import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class NewVisionManager implements IManager{

    private String jsonFolderPath;
    private String newVisionPath;
    private String profileName;
    private Timer receiveJsonFolderFromList_Timer;
    private Timer receiveJsonFolderFromDB_Timer;
    private Timer timerDirManager;
    private final String NVpidPath = "NewVisionPID.txt";
    private int PID = -1;
    private final String TASKLIST = "tasklist";
    private int jsonFolderPointer=0;
    private ArrayList<String> jsonFoldersList;
    private static final String toProcessKey = "toProcess";
    private static final String completedKey = "completed";
    private DirManager dirManager = new DirManager();
    private WatchDir watchDir;
    private Thread watchDirThread;
    private final int RECEIVE_JSONFOLDER_FROM_LIST = 0;
    private final int RECEIVE_JSONFOLDER_FROM_DB = 1;
    private int typeOfTaskReceiver = 0;
    private String jsonArchiveSource = "";
    private String nvParametersPath = "";
    private Logger logger = LogManager.getLogger("NVManager");
    private Archiver archiver = new Archiver();
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
        this.typeOfTaskReceiver = params.getTypeOfJsonFolderReceiving();
        this.jsonArchiveSource=params.getJsonArchiveSource();
        this.nvParametersPath = params.getNvParametersPath();
        this.postURL = params.getURLforGET();
        this.getURL = params.getURLforPOST();
        this.deleteJsonFolderFlag = params.getDeleteProcessedJsonFolder();
        this.deleteJsonZipFlag = params.getDeleteUploadedZippedJsons();

        receiveJsonFolderFromList_Timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("NV PID is: "+PID);

                if(jsonFoldersList==null || jsonFoldersList.size() == 0) {
                    jsonFoldersList = (ArrayList<String>) dirManager.getJsonFoldersList(jsonFolderPath, toProcessKey);
                    jsonFolderPointer=0;
                }else {
                    if(jsonFolderPointer>0){
                        String newName  = dirManager.replaceNamePart(jsonFoldersList.get(jsonFolderPointer-1), toProcessKey,completedKey);
                        dirManager.renameFolder(jsonFolderPath,jsonFoldersList.get(jsonFolderPointer-1),newName);
                        System.out.println(newName);
                    }
                    if (checkNewVisionWork() == false && jsonFolderPointer < jsonFoldersList.size()) {
                        try {
                            //робимо PID нулем, щоб перевірки не відбувалися доки NV не збереже новий PID

                            String str = "cmd.exe /c start java -jar " + newVisionPath + " nogui " + profileName + " " + jsonFolderPath + "\\" + jsonFoldersList.get(jsonFolderPointer) + "\\";
                            System.out.println(str + "\n" + (jsonFolderPointer + 1) + "/" + jsonFoldersList.size());
                            TasksClass.startTask(str);

                        } catch (Exception ee) {
                            System.out.println(ee);
                        }
                        jsonFolderPointer++;
                    } else {
                        if (jsonFolderPointer >= jsonFoldersList.size()) {
                            jsonFoldersList.clear();
                        }
                    }
                }
            }
        });


        receiveJsonFolderFromDB_Timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("NV PID is: "+PID);

                if(checkNewVisionWork()==false){
                     try {
                         //Read parameters from DB
                         ParametersReader parametersNV = ParametersReader.getInstance();
                         parametersNV.setArchiveListener(new ArchiveLoader());
                         parametersNV.nextAfterThis();
                         System.out.println(parametersNV.getVideoParameters().getVideoDateInFormat("yyyyMMddHHmmss"));

                         //Set needed parameters to necessary classes
                         System.out.println(parametersNV.getVideoParameters().toString());
                         ArrayList<SceneLineParams> sceneLineParams = new ArrayList<>(parametersNV.getSceneLineParams());
                         ArrayList<ScenePolygonParams> scenePolygonParams = new ArrayList<>(parametersNV.getScenePolygonParams());
                         String videoDate = parametersNV.getVideoParameters().getVideoDateInFormat("yyyyMMddHHmmss");
                         int taskID = parametersNV.getTask().getOutsideTask_id();

                         //Load profileParameters.xml to profileParameters.java
                         String path = nvParametersPath;
                         ProfileParameters profileParameters = ProfileParameters.loadProfileParameters(path);

                         //Set new parameters to profileParameters.java
                         profileParameters.setSceneLineParams(sceneLineParams);
                         profileParameters.setScenePolygons(scenePolygonParams);
                         profileParameters.setVideoDate(videoDate);
                         profileParameters.setTaskID(taskID);

                         //Save new profileParameters.java to the profileParameters.xml
                         profileParameters.writeProfileParameters(profileParameters,path);

                         //start OffNewVision with new profileParameters
                         String str = "cmd.exe /c start java -jar " + newVisionPath + " nogui " + profileName + " " + jsonFolderPath + "\\" + parametersNV.getVideoParameters().getName()+ "_toProcess" + "\\";
                         System.out.println(str + "\n" + (jsonFolderPointer + 1) + "/" + jsonFoldersList.size());
                         TasksClass.startTask(str);
                         for (;;) {
                             if(checkNewVisionWork()==true)
                                 break;
                         }
                         for (;;) {
                             if(checkNewVisionWork()==false) {
                                 final String finalStr = parametersNV.getVideoParameters().getName();
                                 final String finalName = finalStr+"_delete";
                                 endLifeProcessing(finalStr,finalName);

                             }
                         }

                     }catch (Exception ee){
                         ee.printStackTrace();
                     }

                }
            }
        });
    }

    @Override
    public void start(){
        if(typeOfTaskReceiver==RECEIVE_JSONFOLDER_FROM_LIST) {
            receiveJsonFolderFromList_Timer.start();
        }else
            if(typeOfTaskReceiver==RECEIVE_JSONFOLDER_FROM_DB){
                receiveJsonFolderFromDB_Timer.start();
            }else
            {
                try {
                    throw new Exception("Wrong parameters value (int typeOfJsonFolderReceiving)");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        watchDirThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Path path = Paths.get("");
                try {
                    watchDir = new WatchDir(path,false);
                    watchDir.addOnPIDChangeListener(new Runnable() {
                        @Override
                        public void run() {
                            loadPID();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                watchDir.processEvents();
            }
        });
        watchDirThread.start();

    }

    @Override
    public void stop() {
        receiveJsonFolderFromList_Timer.stop();
        watchDirThread.stop();
        //:TODO зробити перевірку потока на null
    }

    private void loadPID(){
        System.out.println("loadPid");
        String PIDstring="";

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

            System.out.println(ex.getMessage());
        }

        PID = Integer.parseInt(PIDstring);
    }

    private boolean checkNewVisionWork(){

        Process p = null;
        try {
            p = Runtime.getRuntime().exec(TASKLIST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader( new InputStreamReader(
                p.getInputStream()));
        String line;
        try {
            while ((line = reader.readLine())!=null){
                // System.out.println(line); //
                if(line.contains(String.valueOf(PID))&&line.contains("java.exe")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     */
    private void endLifeProcessing(final String finalStr, final String finalName){
        Thread endLifeProcessThread = new Thread (new Runnable(){
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
            }
        }
        });
            endLifeProcessThread.start();
    }
}
