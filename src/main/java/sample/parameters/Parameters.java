package sample.parameters;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.XMLwriterReader;

import java.io.IOException;


/**
 * Created by Andrew on 11/28/17.
 */
public class Parameters implements IOpenPoseParams, INewVisionParams {

    private Logger logger = LogManager.getLogger("OPManager");
    private String jsonSource = "";
    private String profileName = "";
    private String newVisionPath = "";
    private String videoSource= "";
    private String videoDestination = "";
    private String arguments = "";
    private String openPose = "";
    private String URLforGET = "";
    private String URLforPOST = "";
    private String telegramURL = "";
    private String keyURL = "";
    private Boolean deleteProcessedJsonFolder = Boolean.FALSE;
    private Boolean deleteUploadedZippedJsons = Boolean.FALSE;



    /**
     * This method is used to load parameters from a given file<br>
     *     Method uses deserialization to load instance from an xml file
     * @param file path to parameters xml file
     * @return parameters from the file
     */
    public static Parameters loadParameters(String file) {

        XMLwriterReader<Parameters> reader = new XMLwriterReader(file);

        Logger logger = LogManager.getLogger("General");

        Parameters parameters = null;

        try {
            parameters = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
            System.out.println("Can't read Parameters File");
            parameters = new Parameters();
        } catch (ClassNotFoundException e) {
            logger.error(e);
            e.printStackTrace();
            System.out.println("Class Not Found");
        }

        return parameters;
    }


    @Override
    public String getJsonSource() {
        return jsonSource;
    }

    @Override
    public void setJsonSource(String jsonSource) {
        this.jsonSource = jsonSource;
    }

    @Override
    public String getProfileName() {
        return profileName;
    }

    @Override
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    @Override
    public String getNewVisionPath() {
        return newVisionPath;
    }

    @Override
    public void setNewVisionPath(String newVisionPath) {
        this.newVisionPath = newVisionPath;
    }

    @Override
    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    @Override
    public String getVideoDestination() {
        return videoDestination;
    }

    public void setVideoDestination(String videoDestination) {
        this.videoDestination = videoDestination;
    }

    @Override
    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getOpenPose(){return openPose; }

    public void setOpenPose(String openPose){ this.openPose = openPose;}

    public Boolean getDeleteProcessedJsonFolder(){return  deleteProcessedJsonFolder;}

    public void setDeleteProcessedJsonFolder(){this.deleteProcessedJsonFolder = deleteProcessedJsonFolder;}

    public Boolean getDeleteUploadedZippedJsons(){return  deleteUploadedZippedJsons;}

    public void setDeleteUploadedZippedJsons(){this.deleteUploadedZippedJsons = deleteUploadedZippedJsons;}

    public String getURLforGET(){return URLforGET; }

    public void setURLforGET(String URLforGET){ this.URLforGET = URLforGET;}

    public String getURLforPOST(){return URLforPOST; }

    public void setURLforPOST(String URLforPOST){ this.URLforPOST = URLforPOST;}

    @Override
    public  String getTelegramURL(){
        return telegramURL;
    }

    public void setTelegramURL(String telegramURL){
        this.telegramURL = telegramURL;
    }

    @Override
    public String getKeyURL() {
        return keyURL;
    }

    public void setKeyURL(String keyURL) {
        this.keyURL = keyURL;
    }
}
