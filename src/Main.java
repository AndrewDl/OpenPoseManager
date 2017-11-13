import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 11/08/17.
 */
public class Main {

    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM";

    public static void main(String[] args) {
        String processName = "idea64.exe";
        String inputFolder = ".";
        String outputFolder = "output";


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
            if(f.getName().endsWith(".dav")){
                fileList.add(f);
            }
        }
        System.out.println(fileList.size());
        try {
            if(isProcessRunning(processName)!=false){
                System.out.println("itWorks");
            }else{
                System.out.println("itNotWorks");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void errorMessage(){
        System.out.println("Invalid args. Use -input D:/path/to/the/input/folder/ -output D:/output/folder/path/");
        Platform.exit();
    }


    /**
     * This method searching given task in windows tasklist
     * @param serviceName
     * @return
     * @throws Exception
     */
    private static boolean isProcessRunning(String serviceName)throws Exception{
        Process p = Runtime.getRuntime().exec(TASKLIST);
        BufferedReader reader = new BufferedReader( new InputStreamReader(
                p.getInputStream()));
        String line;
        while ((line = reader.readLine())!=null){
          //  System.out.println(line); //
            if(line.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * I guess programm need more permissions to kill other tasks
     */
//    private static void killProcess(String serviceName) throws Exception {
//        Runtime.getRuntime().exec(KILL + serviceName);
//    }


}
