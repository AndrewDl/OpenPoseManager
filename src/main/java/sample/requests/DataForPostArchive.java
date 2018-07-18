package sample.requests;

/**
 * Created by July on 18.07.2018.
 */
public class DataForPostArchive implements IRequestData{
    private String url;
    private String name;
    private String filepath;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
