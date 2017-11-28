package sample.parameters;


import sample.XMLwriterReader;

import java.io.IOException;

/**
 * Created by Andrew on 11/28/17.
 */
public class Parameters implements IOpenPoseParams, INewVisionParams {

    private String jsonSource = "";
    private String profileName = "";
    private String videoSource= "";
    private String videoDestination = "";
    private String arguments = "";



    /**
     * This method is used to load parameters from a given file<br>
     *     Method uses deserialization to load instance from an xml file
     * @param file path to parameters xml file
     * @return parameters from the file
     */
    public static Parameters loadParameters(String file) {

        XMLwriterReader<Parameters> reader = new XMLwriterReader(file);

        Parameters parameters = null;

        try {
            parameters = reader.ReadFile(Parameters.class);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can't read Parameters File");
            parameters = new Parameters();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Class Not Found");
        }

        return parameters;
    }


    @Override
    public String getJsonSource() {
        return null;
    }

    @Override
    public String setJsonSource() {
        return null;
    }

    @Override
    public String getProfileName() {
        return null;
    }

    @Override
    public String setProfileName() {
        return null;
    }

    @Override
    public String getVideoSource() {
        return null;
    }

    @Override
    public String getVideoDestination() {
        return null;
    }

    @Override
    public String getArguments() {
        return null;
    }


}
