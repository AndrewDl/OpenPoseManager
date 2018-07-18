package sample.requests;

/**
 * Created by July on 18.07.2018.
 */
public class DataForGetTelegram implements IRequestData{
    private String mainURL;
    private String message;
    private String keyURL;

    public String getMainURL() {
        return mainURL;
    }

    public String getMessage() {
        return message;
    }

    public String getKeyURL() {
        return keyURL;
    }

    public void setMainURL(String mainURL) {
        this.mainURL = mainURL;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setKeyURL(String keyURL) {
        this.keyURL = keyURL;
    }
}
