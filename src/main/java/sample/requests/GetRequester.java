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
public class GetRequester implements IGetRequester {
    Logger logger = LogManager.getLogger("HTTPLogger");
    @Override
    public void getRequest(String url, String name) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        url = url + "?name=" + name;
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpclient.execute(request);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();
        String responseString = null;
        try {
            responseString = EntityUtils.toString(responseEntity,"UTF-8");
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        System.out.println(responseString);
        logger.info("GET Request: "+url+" Server response: "+responseString);
    }

    @Override
    public void getRequest(String mainURL, String message, String keyURL) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        String url = mainURL + message + keyURL;
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();
        String responseString = null;
        try {
            responseString = EntityUtils.toString(responseEntity,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(responseString);
        logger.info("GET Request: "+url+" Server response: "+responseString);
    }
}

