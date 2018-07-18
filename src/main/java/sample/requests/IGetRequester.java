package sample.requests;
/**
 * Created by July on 12.07.2018.
 */
public interface IGetRequester {
    /**
     * Method sends GET request to the server
     */
    boolean sendGETRequest(IRequestData data);

    /**
     * Method gets resonse from GET request
     * @return response
     */
    String getResponse();
}
