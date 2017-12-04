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
    List<String> dirList = new ArrayList<>();
    List<String> namesFileList = new ArrayList<>();
    List<File> fileList = new ArrayList<>();


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

    public List<File> getVideoNamesList(String path){
        getFileList(path);

        for(File f : files){
            if(f.getName().endsWith(".mp4")){
                fileList.add(f);
            }
        }
        if(fileList.size() == 0) {
            System.out.println("Files not found");
        }
        return fileList;
    }

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

}
