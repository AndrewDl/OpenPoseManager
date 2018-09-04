package semple.ParametersReader;
import OPMException.TaskException;
import org.junit.Assert;
import org.junit.Test;
import sample.EventsProcessing.ArchiveLoader;
import sample.ParametersReader.ParametersReader;
import sample.parameters.INewVisionParams;
import sample.parameters.Parameters;

import java.io.File;

public class ParametersReaderTest {
    @Test
    public void fireOnArchiveRequestEvent_sendingRequestAndDownloadinArchive_downloadArchive(){
        Parameters param = Parameters.loadParameters("managerParameters\\parameters.xml");
        ParametersReader paramReader = ParametersReader.getInstance(param);
        paramReader.setArchiveListener(new ArchiveLoader());
        String videoName = "1531490638.0144732";
//        paramReader.fireOnAchiveRequestEvent(videoName);

        for (int i =0; i <3;i++) {
            try {
                paramReader.nextAfterThis();
            }catch (TaskException te){
                System.err.print(te.getMessage());
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //check
            videoName = paramReader.getVideoParameters().getName();
            File f = new File(param.getJsonSource() + videoName);
            boolean actual = f.exists();
            Assert.assertEquals("Check folder exists: ", true, actual);
        }
    }
}
