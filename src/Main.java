import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Andrew on 11/08/17.
 */
public class Main {

    public static void main(String[] args) {
        Data data = null;
        /**
         * loading pathes and additional args from xml file
         */
        XMLwriterReader reader = new XMLwriterReader("data.xml");

        try {
            data = (Data)reader.ReadFile(Data.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /**
         * starting processing
         */
        VideoLoader vl = new VideoLoader(data);


    }
}
