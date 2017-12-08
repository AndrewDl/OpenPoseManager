package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirManager {

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

    /**
     * Checks the folders for the "_toProcess" at the end of the name
     * @param path path to the folder with folders for checking
     * @return true if at list one folder with "_toProcess" at the end of the name exist,
     *          false if not
     */
    public boolean isToProcessFolderInDir(String path){
        //всі не перебираємо, якщо є хоть одна то тру
        File[] fList;

        File F = new File(path);
        fList = F.listFiles();

        for(int i=0; i<fList.length-1; i++)
        {
            //Нужны только папки в место isFile() пишим isDirectory()
            if(fList[i].isDirectory() && isToProcessContains(fList[i].getName())==true){
                return true;
            }
        }
        return false;
    }

    /**
     * @param path path to the folder with folders for checking
     * @return the name of the folder with the "_toProcess" at the end of the name
     * if at list one exist
     */
    public String getToProcessFolderName(String path){

        File[] fList;

        File F = new File(path);
        fList = F.listFiles();

        for(int i=0; i<fList.length-1; i++)
        {
            //Нужны только папки в место isFile() пишим isDirectory()
            if(fList[i].isDirectory() && isToProcessContains(fList[i].getName())==true){
                return fList[i].getName();
            }
        }
        return null;
    }

    /**
     *
     * @param str name of the folder that may contains "_toProcess" at the end of the name
     * @return true if contains, false if not
     */
    private boolean isToProcessContains(String str){
        String[] _splitArr = str.split("_");

        if(_splitArr[_splitArr.length-1].equals("_toProcess")){
            return true;
        }
        return false;
    }

}
