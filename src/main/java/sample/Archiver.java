package sample;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Laimi on 09.07.2018.
 */
public class Archiver {
    public void Zip(String source_dir, String zip_file) throws Exception
    {
        long start = System.currentTimeMillis();
        FileOutputStream fout = new FileOutputStream(zip_file);
        ZipOutputStream zout = new ZipOutputStream(fout);
        zout.setLevel(0);
        File fileSource = new File(source_dir);

        addDirectory(zout, fileSource);

        zout.close();
        long finish = System.currentTimeMillis();
        long timeConsumedMillis = finish - start;

        System.out.println("Zip file was created!");
        System.out.println("Time was taken: "+timeConsumedMillis/1000+"s");

    }
    private void addDirectory(ZipOutputStream zout, File fileSource)
            throws Exception
    {
        File[] files = fileSource.listFiles();
        //System.out.println(fileSource.listFiles().toString());
        Integer count=0;
        System.out.println("Adding directory:" + fileSource.getName() + ">");
        for(int i = 0; i < files.length; i++) {
            // Если file является директорией, то рекурсивно вызываем
            // метод addDirectory
//            if(files[i].isDirectory()) {
//                addDirectory(zout, files[i]);
//                continue;
//            }
     //       System.out.println("Adding file <" + files[i].getName() + ">");
            //count++;

            FileInputStream fis = new FileInputStream(files[i]);
           // System.out.println(String.valueOf(files[i].getPath().split("output/jsonFolders/")));
            String[] str = files[i].getPath().split("\\\\",3);
           // System.out.println(str[2]+"\\\\"+str[3]);
            System.out.println(str[2]);
            String out = str[2];

           // zout.putNextEntry(new ZipEntry(String.valueOf(files[i].getPath().split("output/jsonFolders/"))));
           // zout.putNextEntry(new ZipEntry(files[i].getPath()));
            zout.putNextEntry(new ZipEntry(out));

            byte[] buffer = new byte[4048];
            int length;
            while((length = fis.read(buffer)) > 0)
                zout.write(buffer, 0, length);
            zout.closeEntry();
            fis.close();
       //     System.out.println("Number of files: "+count);
        }
    }

    private final String CHARSET_CP866  = "CP866";

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

        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
