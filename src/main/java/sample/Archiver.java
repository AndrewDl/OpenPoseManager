package sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Laimi on 09.07.2018.
 * Class contains methods for zip and unzip directories.
 */

public class Archiver {


    private final String CHARSET_CP866  = "CP866";

    private Logger logger = LogManager.getLogger("Archiver");

    /**
     * Method to Zip json folders
     * @param source_dir - address of folder, which should be zipped
     * @param zip_file - address where .zip file should be placed. IMPORTANT! Should contain extension(.zip)
     * @throws Exception
     */
    public void Zip(String source_dir, String zip_file) throws Exception
    {
        long start = System.currentTimeMillis(); // when zip starts
        FileOutputStream fout = new FileOutputStream(zip_file);
        ZipOutputStream zout = new ZipOutputStream(fout);
        zout.setLevel(9); //compressing level. From 0 to 9. Ninth level have better performance than zero level
        File fileSource = new File(source_dir); //what exactly it should zip

        addDirectory(zout, fileSource); // zipping here

        zout.close();
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;

        System.out.println("Zip file was created!");
        System.out.println("Time was taken: "+timeConsumedMillis/1000+"s");
        logger.info(source_dir+" was zipped, Time was taken: "+timeConsumedMillis/1000+"s");

    }
    private void addDirectory(ZipOutputStream zout, File fileSource)
    {
        File[] files = fileSource.listFiles();

        System.out.println("Adding directory:" + fileSource.getName() + ">");

        for(int i = 0; i < files.length; i++) {

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(files[i]);
            } catch (FileNotFoundException e) {
                logger.error(e);
                e.printStackTrace();
            }
            String[] str = files[i].getPath().split("\\\\",3);
            String out = str[2];
            try {
                zout.putNextEntry(new ZipEntry(out));
            } catch (IOException e) {
                logger.error(e);
                e.printStackTrace();
            }

            byte[] buffer = new byte[4048];
            int length;
            try {
            while((length = fis.read(buffer)) > 0)
                zout.write(buffer, 0, length);
            zout.closeEntry();
            fis.close();
            } catch (IOException e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for Unzipping archives
     * @param zipFile - address to file, which should be unzipped. IMPORTANT - should contain extension(.zip)
     * @param outputFolder - address, where unzipped files should be placed.
     */
    public void unZip(String zipFile, String outputFolder){
        long start = System.currentTimeMillis();
        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                //+ File.separator +

                System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            long finish = System.currentTimeMillis();
            long timeConsumedMillis = finish - start;

            System.out.println("Time was taken: "+timeConsumedMillis/1000+"s");
            System.out.println("Done");
            logger.info(zipFile+"was unzipped, Time was taken: "+timeConsumedMillis/1000+"s");

        }catch(IOException ex){
            logger.error(ex);
            ex.printStackTrace();
        }
    }
}
