import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 11/08/17.
 */
public class Main {
    public static void main(String[] args) {

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

    }

    private static void errorMessage(){
        System.out.println("Invalid args. Use -input D:/path/to/the/input/folder/ -output D:/output/folder/path/");
        Platform.exit();
    }
}
