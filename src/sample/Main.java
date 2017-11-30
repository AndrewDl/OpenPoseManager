package sample;

import sample.managers.VideoLoader;
import sample.parameters.Parameters;

import java.io.IOException;

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
        Parameters parameters = Parameters.loadParameters("parameters.xml");
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
