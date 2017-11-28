package sample.parameters;

/**
 * Created by Andrew on 11/28/17.
 */
public interface INewVisionParams {
    /**
     * @return path to folder that contains folders with jsons.
     */
    String getJsonSource();
    String setJsonSource();

    String getProfileName();
    String setProfileName();

}
