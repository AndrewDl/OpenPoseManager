package sample.requests;
/**
 * Created by July on 12.07.2018.
 */
public interface IGetRequester {
    /**
     * Method sends GET request that obtains from server JSON with information for downloading archive
     * @param url - url to server to which we send GET request
     */
    boolean sendGETRequest(String url);

    /**
     * Method gets resonse from GET request
     * @return response
     */
    String getResponse();
}
