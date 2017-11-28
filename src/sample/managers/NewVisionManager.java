package sample.managers;

import sample.TasksClass;
import sample.parameters.INewVisionParams;

import java.util.ArrayList;

public class NewVisionManager implements IManager{

    private String jsonFolderPath;
    private ArrayList<String> names;
    private String newVisionPath;
    private String profileName;
    /**
     * @param names of folders with jsons
     * @param params
     */
    public NewVisionManager(ArrayList<String> names, INewVisionParams params){
        this.names = names;
        this.jsonFolderPath = params.getJsonSource();
        this.newVisionPath = params.getNewVisionPath();
        this.profileName = params.getProfileName();
    }

    @Override
    public void start() {
        try {
            String str = "java -jar "+newVisionPath+" gui "+profileName+" "+jsonFolderPath+"\\"+names.get(0)+"\\";
            System.out.println(str);
            TasksClass.startTask(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //стартуємо NewVision в який передаємо перший
        //запускаємо таймер в якому крутиться перевірка на роботу NewVision-а
        //якщо не робить то запускаємо наступну папку з json-ами
    }

    @Override
    public void stop() {

    }
}
