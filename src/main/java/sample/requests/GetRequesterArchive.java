package sample.requests;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by July on 12.07.2018.
 */
public class GetRequesterArchive implements IGetRequester {
    Logger logger = LogManager.getLogger("HTTPLogger");
    private String serverResponse = "";

    @Override
    public boolean sendGETRequest(IRequestData data) {
        boolean success = false;
        DataForGetArchive obtainedData = (DataForGetArchive) data;
        String serverURL = obtainedData.getRequestURL() + "?name=" + obtainedData.getName();

        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(serverURL);
        HttpResponse response = null;
        try {
            response = httpclient.execute(request);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();
        try {
            serverResponse = EntityUtils.toString(responseEntity,"UTF-8");
            success = true;
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("GET Request: "+serverURL+" Server response: "+ serverResponse);
        return success;
    }

    @Override
    public String getResponse(){
        return serverResponse;
    }
}

