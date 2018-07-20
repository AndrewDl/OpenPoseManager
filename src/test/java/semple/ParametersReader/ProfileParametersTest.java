package semple.ParametersReader;

import org.junit.Assert;
import org.junit.Test;
import sample.ParametersReader.ProfileParameters;

public class ProfileParametersTest {
    @Test
    public void loadProfileParameters_loadParametersFromNewVisionParametersXML_xxx(){
        ProfileParameters params = ProfileParameters.loadProfileParameters("F:\\CIF\\NewVision\\newvision\\resources\\profiles\\profile 1\\config\\parameters.xml");

        Assert.assertEquals("resources\\cameraVideos\\ekoRiba\\",params.getJsonFolderPath());
    }
}
