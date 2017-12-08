package sample.managers;

import com.sun.org.apache.xpath.internal.SourceTree;
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
    private ArrayList<String> names;
    private String newVisionPath;
    private String profileName;
    private Timer timerNewVisionWorkManager;
    private final String NVpidPath = "NewVisionPID.txt";
    private int PID = 0;
    private final String TASKLIST = "tasklist";
    private int jsonFolderPointer=0;

    /**
     * @param names of folders with jsons
     * @param params
     */
    public NewVisionManager(ArrayList<String> names, INewVisionParams params){
        this.names = names;
        this.jsonFolderPath = params.getJsonSource();
        this.newVisionPath = params.getNewVisionPath();
        this.profileName = params.getProfileName();

        timerNewVisionWorkManager = new Timer(4000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkNewVisionWork()==false && jsonFolderPointer<names.size()){
                    try {
                        String str = "cmd.exe /c start java -jar "+newVisionPath+" nogui "+profileName+" "+jsonFolderPath+"\\"+names.get(jsonFolderPointer)+"\\";
                        System.out.println(str+"\n"+(jsonFolderPointer+1)+"/"+names.size());
                        TasksClass.startTask(str);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                    jsonFolderPointer++;
                }else{
                    if(jsonFolderPointer>=names.size())
                    {
                        System.out.println("All json folders are computed");
                        stop();
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
