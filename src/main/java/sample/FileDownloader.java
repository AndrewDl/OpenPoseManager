package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by July on 12.07.2018.
 */
public class FileDownloader {
    public void downloadFile(String urlStr,String output) throws IOException {
        long start = System.currentTimeMillis();
        URL url = new URL(urlStr);
        File file = new File(output);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;
        System.out.println("Time was taken: "+timeConsumedMillis/1000+"s");
    }
}
