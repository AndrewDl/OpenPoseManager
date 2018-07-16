package sample.requests;

/**
 * Created by July on 13.07.2018.
 */
public interface IPostRequester {
    /**
     * Method sends to server POST request that contains archive
     * @param url - url for request
     * @param name - name of task
     * @param filepath - path to archive
     */
    void postRequest(String url, String name, String filepath);
}
