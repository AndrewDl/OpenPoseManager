package sample.parameters;

/**
 * Interface to get DB authorisation data,
 * local open pose JSONs folder path,
 * url to json with link on archive with open pose JSONs
 */
public interface IParametersReader {

    String getDB_URL();
    String getDB_USER();
    String getDB_PASSWORD();
    /**
     * @return path to folder that contains folders with jsons.
     */
    String getJsonSource();
    String getJsonArchiveSource();
}
