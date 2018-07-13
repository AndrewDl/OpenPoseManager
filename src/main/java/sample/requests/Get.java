package sample.requests;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by July on 12.07.2018.
 */
public class Get implements IHttpRequester{
    public void httpRequest(String url, String name) {
        HttpClient httpclient = HttpClientBuilder.create().build();
        url = url + "?name=" + name;
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
    }
}
