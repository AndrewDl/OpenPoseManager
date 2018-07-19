package semple;

import org.junit.Assert;
import org.junit.Test;
import sample.Archiver;

import java.io.File;

public class ArchiverTest {
    @Test
    public void unzip_UnzipingArchive_xxx(){
        Archiver archiver = new Archiver();
        archiver.unZip("F:\\CIF\\pistobribitka\\JSONs\\a.zip","F:\\CIF\\pistobribitka\\JSONs\\");

        File file = new File("F:\\CIF\\pistobribitka\\JSONs\\a");

        Assert.assertEquals("Number of file: ",25728,file.listFiles().length);
    }
}
