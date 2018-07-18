package sample.parameters;

/**
 * Created by Andrew on 11/28/17.
 */
public interface INewVisionParams {
    /**
     * @return path to folder that contains folders with jsons.
     */
    String getJsonSource();
    void setJsonSource(String jsonSource);

    String getJsonArchiveSource();

    String getProfileName();
    void setProfileName(String profileName);

    String getNewVisionPath();
    void setNewVisionPath(String newVisionPath);

    String getVideoSource();

    String getVideoDestination();

    String getArguments();

    int getTypeOfJsonFolderReceiving();

    public String getNvParametersPath();
    public void setNvParametersPath(String path);



    String getURLforGET();

    String getURLforPOST();

    Boolean getDeleteProcessedJsonFolder();

    Boolean getDeleteUploadedZippedJsons();
}
