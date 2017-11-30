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

    String getProfileName();
    void setProfileName(String profileName);

    String getNewVisionPath();
    void setNewVisionPath(String newVisionPath);

}
