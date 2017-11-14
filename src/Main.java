import javafx.application.Platform;

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
        String outputFolder = "output";

        TasksClass task = new TasksClass();

        if (args.length == 4) {
            if (args[0].equals("-input")) {
                inputFolder = args[1];
            } else errorMessage();
            if (args[2].equals("-output")) {
                outputFolder = args[3]+outputFolder;
            } else errorMessage();

        }

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
        System.out.println(fileList.size());
        System.out.println(fileList.get(0).getName());

        loop(task,processName,fileList);

    }

    private static void errorMessage(){
        System.out.println("Invalid args. Use -input D:/path/to/the/input/folder/ -output D:/output/folder/path/");
        Platform.exit();
    }

    private static void loop(TasksClass task, String processName, List<File> fileList){
        Integer i=0;
        String cmdLine;
        for(;;){
            try {
                if(task.isProcessRunning(processName)!=false){
                    System.out.println("itWorks");
                }else{
                    System.out.println("itNotWorks");
                    cmdLine = "D:\\Program Files (x86)\\CifrusMark\\bin\\OpenPoseDemo.exe -video "+fileList.get(i).toString()+" -write_keypoint_json output/ -process_real_time";
                    task.startTask(cmdLine);
                    if(i>fileList.size())break;
                }
                System.out.println(new Date());
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
