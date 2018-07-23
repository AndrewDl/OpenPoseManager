package sample.parameters;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.XMLwriterReader;

import java.io.IOException;


/**
 * Created by Andrew on 11/28/17.
 */
public class Parameters implements IOpenPoseParams, INewVisionParams, IParametersReader, ISlashInPath{

    private Logger logger = LogManager.getLogger("OPManager");
    private String jsonSource = "";
    private String jsonArchiveSource = "";
    private String profileName = "";
    private String newVisionPath = "";
    private String videoSource= "";
    private String videoDestination = "";
    private String arguments = "";
    private String openPose = "";
    private String nvParametersPath = "";
    private String DB_URL = "";
    private String DB_USER = "";
    private String DB_PASSWORD = "";
    private int typeOfJsonFolderReceiving = 0;
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
        Parameters parameters = reader.ReadFile(Parameters.class);
        parameters.jsonArchiveSource = parameters.checkIsSlash(parameters.jsonArchiveSource);
        parameters.jsonSource = parameters.checkIsSlash(parameters.jsonSource);
        parameters.newVisionPath = parameters.checkIsSlash(parameters.newVisionPath);
        parameters.videoSource = parameters.checkIsSlash(parameters.videoSource);
        parameters.videoDestination = parameters.checkIsSlash(parameters.videoDestination);
        parameters.nvParametersPath = parameters.checkIsSlash(parameters.nvParametersPath);
        parameters.DB_URL = parameters.checkIsSlash(parameters.nvParametersPath);
        parameters.URLforGET = parameters.checkIsSlash(parameters.nvParametersPath);
        parameters.URLforPOST = parameters.checkIsSlash(parameters.nvParametersPath);
        parameters.telegramURL = parameters.checkIsSlash(parameters.nvParametersPath);
        return parameters;
    }


    @Override
    public String getJsonSource() {
        return jsonSource;
    }

    @Override
    public void setJsonSource(String jsonSource) {
        this.jsonSource = checkIsSlash(jsonSource);
    }

    public String getJsonArchiveSource() {
        return jsonArchiveSource;
    }

    public void setJsonArchiveSource(String jsonArchiveSource) {
        this.jsonArchiveSource = checkIsSlash(jsonArchiveSource);
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
        this.newVisionPath = checkIsSlash(newVisionPath);
    }

    @Override
    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = checkIsSlash(videoSource);
    }

    @Override
    public String getVideoDestination() {
        return videoDestination;
    }

    public void setVideoDestination(String videoDestination) {
        this.videoDestination = checkIsSlash(videoDestination);
    }

    @Override
    public String getArguments() {
        return arguments;
    }

    @Override
    public int getTypeOfJsonFolderReceiving() {
        return typeOfJsonFolderReceiving;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public String getOpenPose(){return openPose; }

    public void setOpenPose(String openPose){ this.openPose = openPose;}

    public String getNvParametersPath(){
        return nvParametersPath;
    }

    public void setNvParametersPath(String path){
        nvParametersPath = checkIsSlash(path);
    }

    @Override
    public String getDB_URL() {
        return DB_URL;
    }

    public void setDB_URL(String DB_URL) {
        this.DB_URL = checkIsSlash(DB_URL);
    }

    @Override
    public String getDB_USER() {
        return DB_USER;
    }

    public void setDB_USER(String DB_USER) {
        this.DB_USER = DB_USER;
    }

    @Override
    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public void setDB_PASSWORD(String DB_PASSWORD) {
        this.DB_PASSWORD = DB_PASSWORD;
    }

    public Boolean getDeleteProcessedJsonFolder(){return  deleteProcessedJsonFolder;}

    public void setDeleteProcessedJsonFolder(){this.deleteProcessedJsonFolder = deleteProcessedJsonFolder;}

    public Boolean getDeleteUploadedZippedJsons(){return  deleteUploadedZippedJsons;}

    public void setDeleteUploadedZippedJsons(){this.deleteUploadedZippedJsons = deleteUploadedZippedJsons;}

    public String getURLforGET(){return URLforGET; }

    public void setURLforGET(String URLforGET){ this.URLforGET = checkIsSlash(URLforGET);}

    public String getURLforPOST(){return URLforPOST; }

    public void setURLforPOST(String URLforPOST){ this.URLforPOST = checkIsSlash(URLforPOST);}

    @Override
    public  String getTelegramURL(){
        return telegramURL;
    }

    public void setTelegramURL(String telegramURL){
        this.telegramURL = checkIsSlash(telegramURL);
    }

    @Override
    public String getKeyURL() {
        return keyURL;
    }

    public void setKeyURL(String keyURL) {
        this.keyURL = keyURL;
    }

    @Override
    public String checkIsSlash(String path) {
        if(path.substring(path.length()-1).equals("\\") || path.substring(path.length()-1).equals("/")){
            path = path.substring(0,path.length()-1);
            path = checkIsSlash(path);
        }
        return path;
    }
}
