package sample;

import sample.ParametersReader.ParametersReader;
import sample.ParametersReader.SceneLineParams;

import java.util.ArrayList;

/**
 * Created by Andrew on 11/08/17.
 */
public class Main {

    public static void main(String[] args) {
       /* Controller controller = new Controller(args);

        while(true){

        }*/


       try {
           ParametersReader parametersNV = new ParametersReader();
           for (int i = 0; i < 5; i++) {
               System.out.println(parametersNV.getVideoParameters().toString());
               ArrayList<SceneLineParams> sceneLineParams = new ArrayList<>(parametersNV.getSceneLineParams());
               for (SceneLineParams lineParam :
                       sceneLineParams) {
                   System.out.println("- " + lineParam.toString());
               }
               parametersNV.nextAfterThis();
           }
//        System.out.println(task.toString());
//        System.out.println(lineLocation.toString());
//        System.out.println(zoneLocation.toString());
       }catch (Exception e){
           e.printStackTrace();
       }
    }




}
