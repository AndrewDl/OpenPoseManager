package sample.parameters;


import sample.XMLwriterReader;

/**
 * Created by Andrew on 11/28/17.
 */
public class Parameters implements IOpenPoseParams, INewVisionParams {


    private String jsonSource = "";
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


    /**
     * This method is used to load parameters from a given file<br>
     *     Method uses deserialization to load instance from an xml file
     * @param file path to parameters xml file
     * @return parameters from the file
     */
    public static Parameters loadParameters(String file) {

        XMLwriterReader<Parameters> reader = new XMLwriterReader(file);

        Parameters parameters = reader.ReadFile(Parameters.class);

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
        nvParametersPath = path;
    }


    //TODO: сделать для параметров ниже интерфейс
    public String getDB_URL() {
        return DB_URL;
    }

    public void setDB_URL(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    public String getDB_USER() {
        return DB_USER;
    }

    public void setDB_USER(String DB_USER) {
        this.DB_USER = DB_USER;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public void setDB_PASSWORD(String DB_PASSWORD) {
        this.DB_PASSWORD = DB_PASSWORD;
    }
}
