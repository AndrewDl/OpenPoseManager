package sample;

import com.sun.org.apache.bcel.internal.generic.NEW;
import sample.managers.NewVisionManager;
import sample.managers.OpenPoseManager;
import sample.parameters.Parameters;

import java.util.ArrayList;

public class Controller {
    private String mode;
    private String[] args;
    private ArrayList<String> jsonFoldersList;
    Arguments arguments = new Arguments();

    Controller(String[] args){
        this.args = args;
        initialize();
    }

    public void initialize(){
        Parameters parameters = Parameters.loadParameters("managerParameters\\parameters.xml");
        OpenPoseManager openPoseManager = new OpenPoseManager(parameters);

        //треба зчитати список папок з json-ами
        DirManager dirManager = new DirManager();
        jsonFoldersList = (ArrayList<String>) dirManager.getJsonFoldersList(parameters.getJsonSource());
        NewVisionManager newVisionManager = new NewVisionManager(jsonFoldersList, parameters);

        //створюємо NewVision менеджер, передаєм туди список папок і стартуєм
        arguments.setArgs(args);
        mode = arguments.getMode();
        if(mode.equals("p")){
            newVisionManager.start();
            openPoseManager.start();
        }else{if(mode.equals("op")){
            openPoseManager.start();
        }else{if(mode.equals("nv")){
           newVisionManager.start();
        }}}




    }
}
