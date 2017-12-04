package sample;

import sample.managers.NewVisionManager;
import sample.parameters.Parameters;

import java.util.ArrayList;

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
        newVisionManager.start();
        //створюємо NewVision менеджер, передаєм туди список папок і стартуєм
    }
}
