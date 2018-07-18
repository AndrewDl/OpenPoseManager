package sample.requests;

/**
 * Created by July on 18.07.2018.
 */
public class DataForGetArchive implements IRequestData{
    private String requestURL;
    private String name;

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
