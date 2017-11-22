import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Platform;

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
public class VideoLoader {
    private Timer jSonTimer;
    String processName = "OpenPoseDemo.exe";//"Telegram.exe";
    String inputFolder = ".";
    String outputFolder = "output\\";
    String param = "";
    String outputFolderForVideos = outputFolder+"/computedVideos/";
    String outputFolderForFails = outputFolder+"/failedVideos/";
    Integer index = 0;
    File currentVideo;
    VideoLoader(Data data){

        /**
         * pathes and params
         */

        inputFolder = data.getVideoSource();
        outputFolder = data.getVideoDestination();
        param = data.getParameters();
        outputFolderForVideos = outputFolder + "\\computedVideos\\";
        outputFolderForFails = outputFolder + "\\failedVideos\\";
//        if (args.length==4) {
//            if (args[0].equals("-input")) {
//                inputFolder = args[1];
//            } else errorMessage();
//            if (args[2].equals("-output")) {
//                outputFolder = args[3]+outputFolder;
//            } else errorMessage();
//        }
//        if (args.length==4) {
//            if (args[0].equals("-input")) {
//                inputFolder = args[1];
//            } else errorMessage();
//            if (args[2].equals("-output")) {
//                outputFolder = args[3]+outputFolder;
//            } else errorMessage();
//            if (args[4].equals("-process_real_time")){
//                mode = args[4];
//            }
//        }

        TasksClass task = new TasksClass();

        File folder = new File(inputFolder);

        /**
         * checking on availability of folder
         */
        if(!folder.exists()){
            System.out.println("folder does not exist");
            return;
        }

        File[] files = folder.listFiles();

        List<File> fileList = new ArrayList<>();

        //scan for needed files in folder. validate their names;
        for(File f : files){
            if(f.getName().endsWith(".mp4")){
                fileList.add(f);
            }
        }
        if(fileList.size() == 0) {
            System.out.println("No files found!");
        }
        else {System.out.println("Number of files:"+fileList.size());
        mkDir(outputFolderForVideos);
        mkDir(outputFolderForFails);
            jSonTimer = new Timer(3000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if(task.isProcessRunning("WerFault.exe")){
                            String cmdLine = "TASKKILL /f /IM WerFault.exe";
                            System.out.println("WerFault closed");
                            task.startTask(cmdLine);}
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
         jSonTimer.start();
        loop(task,fileList);
        jSonTimer.stop();
    }
        //File folder2 = new File(outputFolder + "test1/");
//                        jSonTimer = new Timer(3000, new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                                if(folder2.exists()){
//                                    System.out.println("timer");
//                                    File[] files = folder2.listFiles();
//                                    List<File> jSonFiles = new ArrayList<>();
//                                    Integer jSonCheck_1 = 0;
//                                    Integer jSonCheck_2 = 0;
//                                    for(File f: files){
//                                        jSonCheck_1++;
//
//                                        if(index > 0&&jSonCheck_2==jSonCheck_1){
//                                            try {
//                                                task.killProcess(processName);
//                                            } catch (Exception e1) {
//                                                e1.printStackTrace();
//                                            }
//                                        }
//                                        jSonCheck_2 = jSonCheck_1;
//                                        index++;
//                                    }}}});
//                       jSonTimer.start();

    }

//        System.out.println(fileList.get(0).getName());

    /**
     * method creates dir for computed videos
     * @param outputFolderForVideos
     */
    public void mkDir(String outputFolderForVideos) {

        File theDir = new File(outputFolderForVideos);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }

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
        Boolean failed = false;
        File failfolder = new File(outputFolderForFails);

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
                        System.out.println("0s");
                        //проверяем есть ли строки в папке, если нет вешаем флаг и на следущем шаге кидаем видос в фейлы
                      //  for(int g = 0; g<5; g++){
                        Thread.sleep(10 * 1000);
                        System.out.println("30s");
                        File folder = new File(outputFolder +fileList.get(i-1).getName().split("\\.")[0]);
                        System.out.println("debug");
                        setCurrentVideoFolder(folder);
                        

                        if(!folder.exists()){
                            System.out.println("killing process");
                            cmdLine = "TASKKILL /f /IM WerFault.exe";
                            task.startTask(cmdLine);
                             //task.killProcess(processName);
                            System.out.println("process should be killed already!");
                            failed = true;
                        }else{failed = false;}
//                        File[] files = folder.listFiles();
//                        List<File> jSonFiles = new ArrayList<>();
//                        for(File f : files){
//                            if(f.getName().endsWith(".json")){
//                                jSonFiles.add(f);
//                            }}
//                        if(jSonFiles.size() == 0) {
//                            System.out.println("No files found, shutting process down!");
//                            task.killProcess(processName);
//                        }


                    }
                    //if(status)jSonTimer.stop();
                }else{
                    status=false;
                    System.out.println("No process found...");
                    System.out.println("Starting new process...");
                    System.out.println(new Date());
//                    cmdLine = "D:\\Program Files (x86)\\CifrusMark\\bin\\OpenPoseDemo.exe -video "
//                            +fileList.get(i).toString()+" -write_keypoint_json output/tests/"
//                            +fileList.get(i).getName().split("\\.")[0]+"/ -process_real_time";
                    cmdLine="bin\\OpenPoseDemo.exe -video "+inputFolder
                            +fileList.get(i).getName()+" -write_keypoint_json "+outputFolder
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
                            fileList.get(i-1).renameTo(destination);
                            System.out.println(destination);
                        } // File destination = new File(".\\output\\tests\\ComputedVideos\\"+fileList.get(i-1).getName());
                        }
                    i++;
                }
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(i>=fileList.size()){
                System.out.println("We've done here! Waiting for OpenPoseDemo finish processing!");
               // File destination = new File(outputFolder+"/computedVideos/"+fileList.get(i-1).getName());

                for(;;){
                    try {
                        if(task.isProcessRunning(processName))
                            Thread.sleep(5 * 1000);
                        else
                        {
                            if(failed)
                            {File destination = new File(outputFolder+"/failedVideos/"+fileList.get(i-1).getName());
                                fileList.get(i-1).renameTo(destination);}
                            else
                            {File destination = new File(outputFolder+"/computedVideos/"+fileList.get(i-1).getName());
                                fileList.get(i-1).renameTo(destination);}
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
        return this.currentVideo;
    }
    private void setCurrentVideoFolder(File file){

    }
}
