package sample.managers;

import com.sun.org.apache.xpath.internal.SourceTree;
import sample.DirManager;
import sample.TasksClass;
import sample.parameters.INewVisionParams;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NewVisionManager implements IManager{

    private String jsonFolderPath;
    private String newVisionPath;
    private String profileName;
    private Timer timerNewVisionWorkManager;
    private final String NVpidPath = "NewVisionPID.txt";
    private int PID = 0;
    private final String TASKLIST = "tasklist";
    private int jsonFolderPointer=0;
    private ArrayList<String> jsonFoldersList;
    private static final String toProcessKey = "toProcess";
    private static final String completedKey = "completed";
    private DirManager dirManager = new DirManager();


    /**
     * @param params
     */
    public NewVisionManager(INewVisionParams params){
        this.jsonFolderPath = params.getJsonSource();
        this.newVisionPath = params.getNewVisionPath();
        this.profileName = params.getProfileName();


        timerNewVisionWorkManager = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jsonFoldersList==null || jsonFoldersList.size() == 0) {
                    jsonFoldersList = (ArrayList<String>) dirManager.getJsonFoldersList(jsonFolderPath, toProcessKey);
                    jsonFolderPointer=0;
                }else {
                    if(jsonFolderPointer>0){
                        String newName  = dirManager.replaceNamePart(jsonFoldersList.get(jsonFolderPointer-1), toProcessKey,completedKey);
                        dirManager.renameFolder(jsonFolderPath,jsonFoldersList.get(jsonFolderPointer-1),newName);
                        System.out.println(newName);
                    }
                    if (checkNewVisionWork() == false && jsonFolderPointer < jsonFoldersList.size()) {

                        try {
                            String str = "cmd.exe /c start java -jar " + newVisionPath + " nogui " + profileName + " " + jsonFolderPath + "\\" + jsonFoldersList.get(jsonFolderPointer) + "\\";
                            System.out.println(str + "\n" + (jsonFolderPointer + 1) + "/" + jsonFoldersList.size());
                            TasksClass.startTask(str);
                        } catch (Exception ee) {
                            System.out.println(ee);
                        }
                        jsonFolderPointer++;
                    } else {
                        if (jsonFolderPointer >= jsonFoldersList.size()) {
                            jsonFoldersList.clear();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void start() {
        timerNewVisionWorkManager.start();
    }

    @Override
    public void stop() {
        timerNewVisionWorkManager.stop();
    }

    private boolean checkNewVisionWork(){
        String PIDstring="";

        try(FileReader reader = new FileReader(NVpidPath))
        {
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){
                //System.out.println((char)c);
                PIDstring = PIDstring.concat(String.valueOf((char)c));
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        PID = Integer.parseInt(PIDstring);
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(TASKLIST);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader( new InputStreamReader(
                p.getInputStream()));
        String line;
        try {
            while ((line = reader.readLine())!=null){
                // System.out.println(line); //
                if(line.contains(String.valueOf(PID))) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
