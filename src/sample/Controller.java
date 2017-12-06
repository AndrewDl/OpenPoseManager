package sample;

import sample.managers.NewVisionManager;
import sample.managers.OpenPoseManager;
import sample.parameters.Parameters;

import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    private String[] args;
    private ArrayList<String> jsonFoldersList;

    Controller(String[] args){
        this.args = args;
        initialize();
    }

    public void initialize(){
        Parameters parameters = Parameters.loadParameters("managerParameters\\parameters.xml");

        //треба зчитати список папок з json-ами
        DirManager dirManager = new DirManager();
        jsonFoldersList = (ArrayList<String>) dirManager.getJsonFoldersList(parameters.getJsonSource());
        NewVisionManager newVisionManager = new NewVisionManager(jsonFoldersList, parameters);
        OpenPoseManager openPoseManager = new OpenPoseManager(parameters);

        //створюємо NewVision менеджер, передаєм туди список папок і стартуєм

        Scanner sc = new Scanner("");
        //TODO: аргументы в отдельном классе. Если args[i] начинается с "-", то в args[i+1] должно содержаться соответсвуеще значение
        if(args[0]==""){
            System.out.println("No arguments found! Please, choose mode:");
            String s = sc.nextLine();
            if(s=="parallel"||s=="p"){
                System.out.println("Starting parallel mode...");
                openPoseManager.start();
                newVisionManager.start();
            }
            if(s=="OpenPose"&&s=="op"){
                System.out.println("Starting OpenPose only mode...");
                openPoseManager.start();
            }
            if(s=="NewVision"&&s=="nv"){
                System.out.println("Starting NewVision only mode...");
                newVisionManager.start();
            }
            if(s=="q"&&s=="exit"){
                System.exit(0);
            }
        }
    }
}
