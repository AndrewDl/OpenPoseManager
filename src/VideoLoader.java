import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laimi on 15.11.2017.
 */
public class VideoLoader {
    String processName = "OpenPoseDemo.exe";//"Telegram.exe";
    String inputFolder = ".";
    String outputFolder = "output\\";
    String param = "";
    String outputFolderForVideos = outputFolder+"computedVideos\\";

    VideoLoader(Data data){

       // System.out.println(args.length);
        inputFolder = data.getVideoSource();
        outputFolder = data.getVideoDestination();
        param = data.getParameters();

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
        loop(task,fileList);
    }
    }

//        System.out.println(fileList.get(0).getName());

public static void mkDir(String outputFolderForVideos) {

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

    private void loop(TasksClass task, List<File> fileList){
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
                    cmdLine="OpenPoseDemo.exe -video "+inputFolder+"\\"
                            +fileList.get(i).toString()+" -write_keypoint_json "+outputFolder+"\\"
                            +fileList.get(i).getName().split("\\.")[0]+"/ "+this.param;
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
