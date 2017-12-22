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
            System.out.println("NV:folder does not exist");
            return;
        }

        files = folder.listFiles();

        System.out.println("NV:file.length:"+files.length);
    }

    public List<String> getJsonFoldersList(String path){
        getFileList(path);

        for(File f : files){
            if(f.isDirectory()){
                dirList.add(f.getName());
            }
        }
        if(dirList.size() == 0) {
            System.out.println("NV:No directories found");
        }
        return dirList;
    }

    /**
     *
     * @param path
     * @param key - key that folder name should contain
     * @return
     */
    public List<String> getJsonFoldersList(String path, String key){
        getFileList(path);
        if(files!=null) {
            for (File f : files) {
                if (f.isDirectory() == true && isToProcessContains(f.getName(), key) == true) {
                    dirList.add(f.getName());
                }
            }
        }
        if (dirList.size() == 0) {
            System.out.println("NV:No directories found");
        }

        return dirList;
    }

    public List<File> getVideoNamesList(String path){
        System.out.println("before getFileList");
        getFileList(path);
        System.out.println("after");
        for(File f : files){
            if(f.getName().endsWith(".mp4")){
                fileList.add(f);
            }
        }
        if(fileList.size() == 0) {
            System.out.println("Files not found");
        }
        System.out.println("fileList.length:"+fileList.size());
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
     * Checks the folders for the @param key at the end of the name
     * @param path path to the folder with folders for checking
     * @return true if at list one folder with @param key at the end of the name exist,
     *          false if not
     */
    public boolean isWithKeyFolderInDir(String path, String key){
        //всі не перебираємо, якщо є хоть одна то тру
        File[] fList;

        File F = new File(path);
        fList = F.listFiles();

        for(int i=0; i<fList.length-1; i++)
        {
            //Нужны только папки в место isFile() пишим isDirectory()
            if(fList[i].isDirectory() && isToProcessContains(fList[i].getName(), key)==true){
                return true;
            }
        }
        return false;
    }

    /**
     * @param path path to the folder with folders for checking
     * @return the name of the folder with the @param key at the end of the name
     * if at list one exist
     */
    public String getToProcessFolderName(String path, String key){

        File[] fList;

        File F = new File(path);
        fList = F.listFiles();

        for(int i=0; i<fList.length-1; i++)
        {
            //Нужны только папки в место isFile() пишим isDirectory()
            if(fList[i].isDirectory() && isToProcessContains(fList[i].getName(), key)==true){
                return fList[i].getName();
            }
        }
        return null;
    }

    /**
     *
     * @param str name of the folder that may contains "_toProcess" at the end of the name
     * @param key
     * @return true if contains, false if not
     */
    private boolean isToProcessContains(String str, String key){
        String[] _splitArr = str.split("_");

        if(_splitArr[_splitArr.length-1].equals(key)){
            return true;
        }
        return false;

    }

    /**
     *
     * @param folderPath - path to the folder that will be renamed
     * @param newName
     * @param newName
     */
    public void renameFolder(String folderPath, String name, String newName){
        if(folderPath.charAt(folderPath.length()-1)!='\\')
            folderPath=folderPath+"\\";
        File oldN = new File(folderPath+name);
        File newN = new File(folderPath+newName);
        oldN.renameTo(newN);
    }

    /**
     *
     * @param name
     * @param cut
     * @param insert
     */
    public String replaceNamePart(String name, String cut,String insert){
        if(name.contains(cut)) {
            name = name.replace(cut, insert);
            return name;
        }
        return name;
    }
}
