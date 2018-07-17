package sample.requests;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Laimi on 12.07.2018.
 */
public class PostRequester implements IPostRequester {
    String name = "";
    String filepath = "";

    public void sendArchiveToServer(String url, String name, String filepath){
        this.name = name;
        this.filepath = filepath;
        sendPOSTRequest(url);
    }

    @Override
    public void sendPOSTRequest(String url) {
        Logger logger = LogManager.getLogger("HTTPLogger");
        long start = System.currentTimeMillis();
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url); //destination for file
        File file = new File(filepath);
        FileBody uploadFilePart = new FileBody(file);
        StringBody str = new StringBody(name, ContentType.TEXT_PLAIN);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("name", str)
                .addPart("file", uploadFilePart)
                .build(); // building multipart entity
        httpPost.setEntity(reqEntity);
        HttpResponse response = null; // executing entity
        try {
            response = httpclient.execute(httpPost);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        HttpEntity responseEntity = response.getEntity();
        String responseString = null; //server response
        try {
            responseString = EntityUtils.toString(responseEntity,"UTF-8");
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
        logger.info("File: "+name+" was sent to server. Server response: "+responseString);
    }
}


