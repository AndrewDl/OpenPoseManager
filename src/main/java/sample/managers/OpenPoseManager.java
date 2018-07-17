package sample.managers;

import javafx.application.Platform;
import sample.DirManager;
import sample.TasksClass;
import sample.WatchDir;
import sample.parameters.Parameters;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Laimi on 15.11.2017.
 */
public class OpenPoseManager implements IManager{
    private Timer jSonTimer;
    private String processName = "OpenPoseDemo.exe";//"Telegram.exe";
    private String inputFolder = ".";
    private String outputFolder = "output\\";
    private String param = "";
    private String outputFolderForVideos = outputFolder+"/computedVideos/";
    private String outputFolderForFails = outputFolder+"/failedVideos/";
    private String outputFolderForJsons = outputFolder+"/jsonFolders/";
    private Integer index = 0;
    private File currentVideoFolder;
    private File tempVideoFolder;
    private Boolean failed = false;
    private DirManager dirMan = new DirManager();
    private Thread opm;
    private Thread wd;
    private WatchDir wdir;
    private Path outputFolderForJsonsPath = Paths.get(outputFolderForJsons);
    private String Child;
    private Long Amount = 0L;
    private Long Max = 0L;
    private Logger logger = LogManager.getLogger("OPManager");

    public OpenPoseManager(Parameters param){

        /**
         * pathes and params
         */

        this.inputFolder = param.getVideoSource();
        this.outputFolder = param.getVideoDestination();
        this.param = param.getArguments();
        this.outputFolderForVideos = outputFolder + "\\computedVideos\\";
        this.outputFolderForFails = outputFolder + "\\failedVideos\\";
        this.processName = param.getOpenPose();
        final TasksClass task = new TasksClass();

        /**
         * WatchDir thread. Contents Timer for errors
         * WatchDir checks chosen folders for any changes, and its using for checking process for shooting errors
         *
         */

        wd = new Thread (new Runnable(){
            public void run(){
                try {
                    wdir = new WatchDir(outputFolderForJsonsPath, true);
                    wdir.setCount();
                    jSonTimer = new Timer(3000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("boop!");

                            try{
                                //if openpose processing without errors its getting amount of json changes
                                if(task.isProcessRunning(processName)&&!task.isProcessRunning("WerFault.exe")) {
                                    Amount = wdir.getCount();
                                    System.out.println("Amount ="+Amount);
                                }
                                // if openpose processing and some errors shoots it saving current amount of json
                                // then sleeps for 2 seconds and compare saved value with new one
                                // if value is the same - open
                                if(task.isProcessRunning("WerFault.exe")&&task.isProcessRunning(processName)){
                                    Max = Amount;
                                    Thread.sleep(2000);
                                    if(task.isProcessRunning("WerFault.exe")&&task.isProcessRunning(processName)){
                                        if(Max==Amount){
                                            String cmdLine = "TASKKILL /f /IM WerFault.exe";
                                            System.out.println("WerFault closed");
                                            task.startTask(cmdLine);
                                            logger.info("WerFaultTask was closed. OpenPoseFailure!");
                                            failed = true;
                                        }
                                    }
                                }
                                if(task.isProcessRunning("WerFault.exe")&&currentVideoFolder!=null&&!currentVideoFolder.exists()){
                                    String cmdLine = "TASKKILL /f /IM WerFault.exe";
                                    System.out.println("WerFault closed");
                                    task.startTask(cmdLine);
                                    failed = true;
                                }
                            } catch (Exception e1) {
                                logger.error(e);
                                e1.printStackTrace();
                            }
                        }
                    });
                    jSonTimer.start();
                    wdir.processEvents();
                } catch (IOException e) {
                    logger.error(e);
                    e.printStackTrace();
                }
            }
        });

/**
 * New Thread for OPManager
 */
        opm = new Thread (new Runnable() {
            @Override
            public void run(){


        File folder = new File(inputFolder);

        /**
         * checking on availability of folder
         */
        if(!folder.exists()){
            System.out.println("folder does not exist");
            return;
        }

      //  File[] files = folder.listFiles();



        dirMan.mkDir(outputFolderForVideos);
        dirMan.mkDir(outputFolderForFails);
        dirMan.mkDir(outputFolderForJsons);

                List<File> fileList = new ArrayList<>();
                for(;;) {
                    if (fileList.isEmpty()) {
                        fileList = dirMan.getVideoNamesList(inputFolder);
                    } else {
                        dirMan.dropFilelist();
                        fileList = dirMan.getVideoNamesList(inputFolder);
                        loop(task, fileList);
                    }
                    try {
                        Thread.sleep(10*1000);
                    } catch (InterruptedException e) {
                        logger.error(e);
                        e.printStackTrace();
                    }
                }
//      //  jSonTimer.start()
//      if(!fileList.isEmpty())
//        loop(task,fileList);
//      else {
//          System.out.println("No Video File Found");
//
//      }

//          try {
//              Thread.sleep(10000);
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }
//          System.out.println("start again");
//          start();
//      }

            }
        });
    }

    /**
     * OPManager starting in new Thread
     */
    @Override
    public void start() {
        opm.start();
        if(wd.isAlive())wd.interrupt();
        if(wd.isAlive())wd.stop();
        wd.start();
    }
    public void secondStart(List<File> fileList){
        fileList.clear();
        start();
    }

    /**
     * stops OPManager thread and Timer for errors
     */
    @Override
    public void stop() {
        wd.interrupt();
        opm.interrupt();
        jSonTimer.stop();
    }

    private void errorMessage(){
        System.out.println("Invalid args. Use -input D:/path/to/the/input/folder/ -output D:/output/folder/path/");
        Platform.exit();
    }

    /**
     * This method checks tasklist for running openposedemo process.
     * while process not running it is forms cmdLine for openposedemo.exe and starts it
     * every new loop iteration previously computed video placing into special folder
     * after all iterations manager waits for openpose process to stop, and only then replacing last video and stops.
     * Pay attention that index changes while new video process was started
     * @param task
     * @param fileList
     */
    private void loop(TasksClass task, List<File> fileList){
        Integer i=0;
        String cmdLine;
        Boolean status = false;

       // String outputFolderForVideos = outputFolder+"\\computedVideos\\";
        System.out.println("Starting loop");
        for(;;){
            try {
                if(task.isProcessRunning(processName)!=false){
                    if(status!=true){
                        index = 0;
                        System.out.println("Process works");
                        System.out.println(new Date());
                        status=true;
                        System.out.println("sleep...");
                        Thread.sleep(10 * 1000);
                        System.out.println("awaken...");
                        File folder = new File(outputFolderForJsons +fileList.get(i-1).getName().split("\\.")[0]);
                        if(folder.exists())setCurrentVideoFolder(folder);
                        if(!folder.exists()){
                            setCurrentVideoFolder(folder);
                            System.out.println("failed!");
                            failed = true;
                        }
                    }

                }else{
                    status=false;
                    System.out.println("No process found...");
                    System.out.println("Starting new process...");
                    System.out.println(new Date());
                    cmdLine="bin\\"+processName+" -video "+inputFolder
                            +fileList.get(i).getName()+" -write_keypoint_json "+outputFolderForJsons
                            +fileList.get(i).getName().split("\\.")[0]+"/ "+this.param;
                    task.startTask(cmdLine);
                    logger.info(fileList.get(i).getName()+" in process");
                    //System.out.println(inputFolder+fileList.get(i).getName());
                    if(i>0){
                        if(failed){
                            File destination = new File(outputFolder+"/failedVideos/"+fileList.get(i-1).getName());
                            fileList.get(i-1).renameTo(destination);
                            System.out.println(destination);
                            logger.info(fileList.get(i).getName()+" failed ");
                        }else {
                            File destination = new File(outputFolder + "/computedVideos/" + fileList.get(i - 1).getName());
                            File toProcess = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]);
                            File folderDestination = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]+"_toProcess");
                            toProcess.renameTo(folderDestination);
                            fileList.get(i-1).renameTo(destination);
                            System.out.println(destination);
                            logger.info(fileList.get(i).getName()+" completed");
                        }
                        }
                    failed = false;
                    i++;
                }
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
            }
            if(i>=fileList.size()){
                System.out.println("We've done here! Waiting for OpenPoseDemo finish processing!");
                for(;;){
                    try {
                        if(task.isProcessRunning(processName))
                            Thread.sleep(5 * 1000);
                        else
                        {
                            if(failed)
                            {File destination = new File(outputFolder+"/failedVideos/"+fileList.get(i-1).getName());
                                fileList.get(i-1).renameTo(destination);
                                logger.info(fileList.get(i-1).getName()+" failed");
                                jSonTimer.stop();}
                            else
                            {
                                System.out.println("filelist.size:"+fileList.size());
                                File destination = new File(outputFolder+"/computedVideos/"+fileList.get(i-1).getName());
                                fileList.get(i-1).renameTo(destination);
                                File toProcess = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]);
                                File folderDestination = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]+"_toProcess");
                                toProcess.renameTo(folderDestination);
                                logger.info(fileList.get(i-1).getName()+" completed");
                            }
                            jSonTimer.stop();
                            System.out.println("Got it!");
                            System.out.println(new Date());
                            dirMan.dropFilelist();
//                            List<File> secondFileList = new ArrayList<>();
//                            if(secondFileList.size()==0){
//                                System.out.println("filling new list");
//                                secondFileList = dirMan.getVideoNamesList(inputFolder);}
//                            else
//                            {
//                                System.out.println("clear second list");
//                                secondFileList.clear();
//                                secondFileList = dirMan.getVideoNamesList(inputFolder);
//                            }
//                            if(!secondFileList.isEmpty())
//                                loop(task,secondFileList);
//                            else {
//                                System.out.println("No Video File Found");
//                                try {
//                                    Thread.sleep(10 * 1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                System.out.println("start again");
//                                start();
//                            }
                           // System.out.println("Checking folder for new videos");
                            this.stop();
                            break;
                        }
                    } catch (Exception e) {
                        logger.error(e);
                        e.printStackTrace();
                    }
                }
                //secondStart(fileList);
                break;}
        }
    }
    private File getCurrentVideoFolder(){
        if(currentVideoFolder == null) System.out.println("NULL");
        return this.currentVideoFolder;
    }
    private void setCurrentVideoFolder(File file){
    this.currentVideoFolder = file;
    }

    /**
     * comparing two folders. using for check folders for new files
     * @param folder1
     * @param folder2
     * @return true if folders have same size
     */
    private boolean compareFolders(File folder1, File folder2){
        if(folder1.exists()&&folder2.exists()){
            File[] files1 = folder1.listFiles();
            File[] files2 = folder2.listFiles();
            List<File> jSonFiles = new ArrayList<>();
            Integer c1 = 0, c2 = 0;
            for (File f : files2) c1++;
            for (File f : files1) c2++;
            if (c1 == c2) return true;
            else
                return false;
        }
        return false;
    }

    /**
     * @param folder
     * @return amount of files in folder
     */
    private Integer getListOfFolderFiles(File folder){
        Integer c = 0;
        if(folder!=null&&folder.exists()){
         File[] files = folder.listFiles();
         List<File> jSonFiles = new ArrayList<>();
         for(File f : files)c++;
         return c;
     }
     else return c;
    }

}