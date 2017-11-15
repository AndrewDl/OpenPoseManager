import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Andrew on 11/08/17.
 */
public class Main {

    public static void main(String[] args) {
        String processName = "OpenPoseDemo.exe";//"Telegram.exe";
        String inputFolder = ".";
        String outputFolder = "output\\";
        String mode = "";
        String outputFolderForVideos = outputFolder+"computedVideos\\";

        if (args.length >=4) {
            if (args[0].equals("-input")) {
                inputFolder = args[1];
            } else errorMessage();
            if (args[2].equals("-output")) {
                outputFolder = args[3]+outputFolder;
            } else errorMessage();
            mode = args[4];
        }

        TasksClass task = new TasksClass();

        File folder = new File(inputFolder);

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
        System.out.println("Number of files:"+fileList.size());
//        System.out.println(fileList.get(0).getName());



        File theDir = new File(outputFolderForVideos);

// if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }



        loop(task,processName,fileList,outputFolder,inputFolder, mode);

    }

    private static void errorMessage(){
        System.out.println("Invalid args. Use -input D:/path/to/the/input/folder/ -output D:/output/folder/path/");
        Platform.exit();
    }

    private static void loop(TasksClass task, String processName, List<File> fileList,String outputFolder,String inputFolder, String Mode){
        Integer i=0;
        String cmdLine;
        Boolean status = false;
        String outputFolderForVideos = outputFolder+"computedVideos\\";
        System.out.println("Starting loop");
        for(;;){
            try {
                if(task.isProcessRunning(processName)!=false){
                    if(status!=true){
                        System.out.println("Process works");
                        System.out.println(new Date());
                        status=true;
                        if(i>0){ File destination = new File(outputFolderForVideos+fileList.get(i-1).getName());
                            fileList.get(i-1).renameTo(destination);}
                    }
                }else{
                    status=false;
                    System.out.println("No process found...");
                    System.out.println("Starting new process...");
                    System.out.println(new Date());
//                    cmdLine = "D:\\Program Files (x86)\\CifrusMark\\bin\\OpenPoseDemo.exe -video "
//                            +fileList.get(i).toString()+" -write_keypoint_json output/tests/"
//                            +fileList.get(i).getName().split("\\.")[0]+"/ -process_real_time";
                        cmdLine="E:\\IdeaProjects\\OpenPoseManagerr\\OpenPoseDemo.exe -video"
                        +fileList.get(i).toString()+" -write_keypoint_json "+outputFolder
                        +fileList.get(i).getName().split("\\.")[0]+"/ "+Mode;
                            task.startTask(cmdLine);
                    System.out.println(fileList.get(i).getName());
                    i++;
                }
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(i>=fileList.size()){
                System.out.println("We've done here!"+new Date());
                File destination = new File(outputFolderForVideos+fileList.get(i-1).getName());
                fileList.get(i-1).renameTo(destination);
                break;}
        }
    }

}
