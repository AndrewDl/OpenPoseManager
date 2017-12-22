package sample;

import com.sun.org.apache.bcel.internal.generic.NEW;
import sample.managers.NewVisionManager;
import sample.managers.OpenPoseManager;
import sample.parameters.Parameters;

import java.util.ArrayList;

public class Controller {
    private String mode;
    private String[] args;

    Arguments arguments = new Arguments();

    Controller(String[] args){
        this.args = args;
        initialize();
    }

    public void initialize(){
        Parameters parameters = Parameters.loadParameters("managerParameters\\parameters.xml");
        OpenPoseManager openPoseManager = new OpenPoseManager(parameters);

        NewVisionManager newVisionManager = new NewVisionManager(parameters);

        //створюємо NewVision менеджер, передаєм туди список папок і стартуєм
        arguments.setArgs(args);
        //mode = arguments.getMode();
        mode = "nv";
        if(mode.equals("p")){
            newVisionManager.start();
            openPoseManager.start();
        }else{
            if(mode.equals("op")){
                openPoseManager.start();
            }else{
                if(mode.equals("nv")){
                    newVisionManager.start();
                }
            }
        }




    }
}
