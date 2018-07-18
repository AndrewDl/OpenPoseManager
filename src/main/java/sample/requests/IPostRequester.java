package sample.requests;

/**
 * Created by July on 13.07.2018.
 */
public interface IPostRequester {
    /**
     * Method sends to server POST request
     */
    void sendPOSTRequest(IRequestData data);
}
