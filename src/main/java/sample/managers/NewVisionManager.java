package sample.managers;

import sample.DirManager;
import sample.ParametersReader.ParametersReader;
import imageProcessing.SceneLineParams;
import imageProcessing.ScenePolygonParams;
import sample.ParametersReader.ProfileParameters;
import sample.TasksClass;
import sample.WatchDir;
import sample.parameters.INewVisionParams;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private String nvParameterspath="";



    /**
     * @param params
     */
    public NewVisionManager(INewVisionParams params){
        this.jsonFolderPath = params.getJsonSource();
        this.newVisionPath = params.getNewVisionPath();
        this.profileName = params.getProfileName();
        this.typeOfTaskReceiver = params.getTypeOfJsonFolderReceiving();
        this.nvParameterspath = params.getNvParametersPath();


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
                         parametersNV.nextAfterThis();
                         System.out.println(parametersNV.getVideoParameters().getVideoDateInFormat("yyyyMMddHHmmss"));

                         //Set needed parameters to necessary classes
                         System.out.println(parametersNV.getVideoParameters().toString());
                         ArrayList<SceneLineParams> sceneLineParams = new ArrayList<>(parametersNV.getSceneLineParams());
                         ArrayList<ScenePolygonParams> scenePolygonParams = new ArrayList<>(parametersNV.getScenePolygonParams());
                         String videoDate = parametersNV.getVideoParameters().getVideoDateInFormat("yyyyMMddHHmmss");
                         int taskID = parametersNV.getTask().getOutsideTask_id();

                         //Load profileParameters.xml to profileParameters.java
                         String path = nvParameterspath;
                         ProfileParameters profileParameters = ProfileParameters.loadProfileParameters(path);

                         //Set new parameters to profileParameters.java
                         profileParameters.setSceneLineParams(sceneLineParams);
                         profileParameters.setScenePolygons(scenePolygonParams);
                         profileParameters.setVideoDate(videoDate);
                         profileParameters.setTaskID(taskID);

                         //Save new profileParameters.java to the profileParameters.xml
                         profileParameters.writeProfileParameters(profileParameters,path);

                         //start OffNewVision with new profileParameters
                         String str = "cmd.exe /c start java -jar " + newVisionPath + " nogui " + profileName + " " + jsonFolderPath + "\\" + parametersNV.getVideoParameters().getName() + "\\";
                         System.out.println(str + "\n" + (jsonFolderPointer + 1) + "/" + jsonFoldersList.size());
                         TasksClass.startTask(str);
                         for (;;) {
                             if(checkNewVisionWork()==true)
                                 break;
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
}
