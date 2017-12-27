package sample.managers;

import javafx.application.Platform;
import sample.DirManager;
import sample.TasksClass;
import sample.parameters.Parameters;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laimi on 15.11.2017.
 */
public class OpenPoseManager implements IManager{
    private Timer jSonTimer;
    String processName = "OpenPoseDemo.exe";//"Telegram.exe";
    String inputFolder = ".";
    String outputFolder = "output\\";
    String param = "";
    String outputFolderForVideos = outputFolder+"/computedVideos/";
    String outputFolderForFails = outputFolder+"/failedVideos/";
    String outputFolderForJsons = outputFolder+"/jsonFolders/";
    Integer index = 0;
    File currentVideoFolder;
    File tempVideoFolder;
    Boolean failed = false;
    DirManager dirMan = new DirManager();
    Thread opm;

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
        TasksClass task = new TasksClass();

        /**
         * timer for errors
         * it checks TASKLIST for WerFault.exe process. If it is there and video folder exists - openpose failed and WerFault should be killed
         * if WerFault running and folder is not created - openpose couldnt start processing video, so WerFault should be killed
         * As WerFault will be stopped OpenPoseManager will start new OpenPose prcees for anothe video
         */
        jSonTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    //jSonTimer.wait(10000);
                    System.out.println("boop!");
                    currentVideoFolder = getCurrentVideoFolder();
                    Integer c1 = 0;
                    c1 = getListOfFolderFiles(currentVideoFolder);
                    Integer cTemp = 0;
                try {
                    if(!task.isProcessRunning("WerFault.exe")&&currentVideoFolder.exists()&&currentVideoFolder!=null)
                        tempVideoFolder = getCurrentVideoFolder();

                cTemp = getListOfFolderFiles(tempVideoFolder);

                    if(task.isProcessRunning("WerFault.exe")&&currentVideoFolder!=null&&currentVideoFolder.exists()){
                      //  if(compareFolders(tempVideoFolder,getCurrentVideoFolder())) {
                        if(c1==cTemp) {
                            String cmdLine = "TASKKILL /f /IM WerFault.exe";
                            System.out.println("WerFault closed");
                            task.startTask(cmdLine);
                            failed = true;
                        }
                    }


                    if(task.isProcessRunning("WerFault.exe")&&currentVideoFolder!=null&&!currentVideoFolder.exists()){
                            String cmdLine = "TASKKILL /f /IM WerFault.exe";
                            System.out.println("WerFault closed");
                            task.startTask(cmdLine);
                            failed = true;
                        }
                } catch (Exception e1) {
                    e1.printStackTrace();
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

        List<File> fileList = new ArrayList<>();

        fileList = dirMan.getVideoNamesList(inputFolder);

        dirMan.mkDir(outputFolderForVideos);
        dirMan.mkDir(outputFolderForFails);
        dirMan.mkDir(outputFolderForJsons);

        jSonTimer.start();
        loop(task,fileList);
            }});
    }

    /**
     * OPManager starting in new Thread
     */
    @Override
    public void start() {
        opm.start();

    }

    /**
     * stops OPManager thread and Timer for errors
     */
    @Override
    public void stop() {
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
                    System.out.println(inputFolder+fileList.get(i).getName());
                    if(i>0){
                        if(failed){
                            File destination = new File(outputFolder+"/failedVideos/"+fileList.get(i-1).getName());
                            fileList.get(i-1).renameTo(destination);
                            System.out.println(destination);
                        }else {
                            File destination = new File(outputFolder + "/computedVideos/" + fileList.get(i - 1).getName());
                            File toProcess = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]);
                            File folderDestination = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]+"_toProcess");
                            toProcess.renameTo(folderDestination);
                            fileList.get(i-1).renameTo(destination);
                            System.out.println(destination);
                        }
                        }
                    failed = false;
                    i++;
                }
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
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
                                jSonTimer.stop();}
                            else
                            {File destination = new File(outputFolder+"/computedVideos/"+fileList.get(i-1).getName());
                                fileList.get(i-1).renameTo(destination);
                                File toProcess = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]);
                                File folderDestination = new File(outputFolderForJsons+fileList.get(i-1).getName().split("\\.")[0]+"_toProcess");
                                toProcess.renameTo(folderDestination);}
                            jSonTimer.stop();
                            System.out.println("Got it!");
                            System.out.println(new Date());
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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
