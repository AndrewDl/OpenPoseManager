package semple.ParametersReader;
import org.junit.Assert;
import org.junit.Test;
import sample.EventsProcessing.ArchiveLoader;
import sample.ParametersReader.ParametersReader;
import sample.parameters.INewVisionParams;
import sample.parameters.Parameters;

import java.io.File;

public class ParametersReaderTest {
    @Test
    public void fireOnAchiveRequestEvent_sendingRequestAndDownloadinArchive_downloadArcive(){
        ParametersReader paramReader = ParametersReader.getInstance();
        paramReader.setArchiveListener(new ArchiveLoader());
        String videoName = "lollybomb";
        paramReader.fireOnAchiveRequestEvent(videoName);
        INewVisionParams param = Parameters.loadParameters("managerParameters/parameters.xml");
        File f = new File(param.getJsonSource()+"\\"+videoName);
        boolean actual = f.exists();
        Assert.assertEquals("Check folder exists: ",true,actual);
    }
}
