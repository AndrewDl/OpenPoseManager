package sample;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.managers.NewVisionManager;
import sample.managers.OpenPoseManager;
import sample.parameters.Parameters;




public class Controller {
    private String mode;
    private String[] args;
    private Logger logger = LogManager.getLogger("General");

    Controller(String[] args){
        this.args = args;
        initialize();
    }


    public void initialize(){

        logger.info("Manager was started!");

        Parameters parameters = Parameters.loadParameters("managerParameters\\parameters.xml");

        OpenPoseManager openPoseManager = new OpenPoseManager(parameters);

        NewVisionManager newVisionManager = new NewVisionManager(parameters);

        //створюємо NewVision менеджер, передаєм туди список папок і стартуєм
        Arguments arguments = new Arguments(args);
        mode = arguments.getMode();
        if(mode.equals("p")){
            newVisionManager.start();
            openPoseManager.start();
            logger.info("parallel mode chosen");
        }else{
            if(mode.equals("op")){
                openPoseManager.start();
                logger.info("OpenPose mode chosen");
            }else{
                if(mode.equals("nv")){
                    newVisionManager.start();
                    logger.info("NewVision mode chosen");
                }
            }
        }




    }
}
