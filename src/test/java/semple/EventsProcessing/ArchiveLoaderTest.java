package semple.EventsProcessing;

import org.junit.Assert;
import org.junit.Test;
import sample.EventsProcessing.ArchiveLoader;
import sample.parameters.INewVisionParams;
import sample.parameters.Parameters;

import java.io.File;

public class ArchiveLoaderTest {
    @Test
    public void onJsonRequest_downloadJSONArchiveByName_getNameOfArchivefindAllPathConcatenateAndDonLoad(){
        ArchiveLoader archiveLoader = new ArchiveLoader();
        archiveLoader.onJsonRequest("1531754751.4456806");

        //check
        INewVisionParams param = Parameters.loadParameters("managerParameters/parameters.xml");
        File f = new File(param.getJsonSource()+"\\"+"1531754751.4456806.zip");
        boolean actual = f.exists();
        Assert.assertEquals("File download is ",true,actual);
    }

}
