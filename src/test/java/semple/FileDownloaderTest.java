package semple;

import org.junit.Assert;
import org.junit.Test;
import sample.FileDownloader;
import sample.parameters.INewVisionParams;
import sample.parameters.Parameters;

import java.io.File;
import java.io.IOException;

public class FileDownloaderTest {
    @Test
    public void downloadFile_xxx_xxx(){
        INewVisionParams param = Parameters.loadParameters("managerParameters/parameters.xml");
        String archiveName = "lollybomb.zip";
        FileDownloader downloader = new FileDownloader();
        try {
            downloader.downloadFile("https://cifrusconverter.s3.eu-west-1.amazonaws.com/json/5b507d103f0cb/5b507d103f170.zip",
                    param.getJsonSource()+archiveName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //check

        File f = new File(param.getJsonSource()+archiveName);
        boolean actual = f.exists();
        Assert.assertEquals("File download is ",true,actual);


    }
}
