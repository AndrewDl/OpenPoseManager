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

    @Test
    public void checkIsSlash_getStrWithSlashInTheEndAndDeleteIt_checkLastSimbolInStringAndDelItIfSimbolIsSlash(){
        ProfileParameters params = new ProfileParameters();

        Assert.assertEquals("F:\\CIF\\NewVision\\newvision",params.checkIsSlash("F:\\CIF\\NewVision\\newvision\\\\\\"));
    }

    @Test
    public  void loadProfileParameters_testCheckIsSlashUsingInLoadParameters_(){
        ProfileParameters params = ProfileParameters.loadProfileParameters("F:\\CIF\\pistobribitka\\OpenPoseManager\\src\\test\\java\\semple\\ParametersReader\\parameters.xml");

        Assert.assertEquals("resources\\cameraVideos\\ekoriba_mp4h264.mp4",params.getAddressRTSP());
        Assert.assertEquals("resources\\cameraVideos\\ekoRiba",params.getJsonFolderPath());
        Assert.assertEquals("jdbc:mysql://localhost:3306",params.getDb_host());
        Assert.assertEquals("C:\\Users\\p0keT\\OneDrive\\Desktop\\11_completed",params.getSnapshotAddress());
        Assert.assertEquals("testcounter.cifr.us",params.getDomain());
        Assert.assertEquals("/api/crypt/cam-add",params.getPostfixCounter());
        Assert.assertEquals("/api/shot-add",params.getPostfixSnapshot());
        Assert.assertEquals("/api/crypt/track-add",params.getPostfixTrack());
    }
}
