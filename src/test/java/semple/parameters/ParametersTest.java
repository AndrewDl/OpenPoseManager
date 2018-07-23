package semple.parameters;

import org.junit.Assert;
import org.junit.Test;
import sample.parameters.Parameters;

public class ParametersTest {
    @Test
    public void loadParameters_testCheckIsSlashUsingInLoadParameters_(){
        Parameters param = Parameters.loadParameters("managerParameters\\parameters.xml");

        Assert.assertEquals("https://vision.ecsv.org.ua/json-store/get",param.getJsonArchiveSource());
        Assert.assertEquals("input",param.getVideoSource());
    }
}
