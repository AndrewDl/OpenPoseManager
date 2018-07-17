package sample.requests;

/**
 * Created by July on 13.07.2018.
 */
public interface IPostRequester {
    /**
     * Method sends to server POST request
     * @param url - url for request
     */
    void sendPOSTRequest(String url);
}
