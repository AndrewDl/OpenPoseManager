package sample.requests;
/**
 * Created by July on 12.07.2018.
 */
public interface IGetRequester {
    /**
     * Method sends GET request that obtains from server JSON with information for downloading archive
     * @param url - url to server where we send request
     * @param name - name of task (archive we need)
     */
    String getRequest(String url, String name);

    /**
     * Method sends GET request to notify Telegram-bot about an occurred error
     * @param mainURL - first part of url for request
     * @param message - text of error
     * @param keyURL - second part of url with key
     */
    void getRequest(String mainURL, String message, String keyURL);
}
