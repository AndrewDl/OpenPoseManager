package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by July on 16.07.2018.
 */
public class PathJSONParser {
    /**
     * Method gets json string with path data, parses it and gets path. We use this path to download archive.
     * @param jsonData - json that comes from server
     * @return path
     */
    public static String getPathToArchive(String jsonData){
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonData);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String path = (String) json.get("path");
        return path;
    }
}
