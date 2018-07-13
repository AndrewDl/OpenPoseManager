package sample.requests;
/**
 * Created by July on 12.07.2018.
 */
public interface IGetRequester {
    void getRequest(String url, String name);
    void getRequest(String mainURL, String message, String keyURL);
}
