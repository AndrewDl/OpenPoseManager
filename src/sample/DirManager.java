package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirManager {

    /**
     * path to the folder with videos or folders with jsons
     */
    private String path;
    private File[] files;
    private List<String> dirList = new ArrayList<>();
    private List<String> fileList = new ArrayList<>();


    private void getFileList(String path){
        File folder = new File(path);

        /**
         * checking on availability of folder
         */
        if(!folder.exists()){
            System.out.println("folder does not exist");
            return;
        }

        files = folder.listFiles();

    }

    public List<String> getJsonFoldersList(String path){
        getFileList(path);

        for(File f : files){
            if(f.isDirectory()){
                dirList.add(f.getName());
            }
        }
        if(dirList.size() == 0) {
            System.out.println("No directories found");
        }
        return dirList;
    }

    public List<String> getVideoNamesList(String path){
        getFileList(path);

        for(File f : files){
            if(f.getName().endsWith(".mp4")){
                fileList.add(f.getName());
            }
        }
        if(fileList.size() == 0) {
            System.out.println("Files not found");
        }
        return fileList;
    }
}
