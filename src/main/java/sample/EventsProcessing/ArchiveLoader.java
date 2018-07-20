package sample.EventsProcessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.Archiver;
import sample.FileDownloader;
import sample.PathJSONParser;
import sample.parameters.INewVisionParams;
import sample.parameters.Parameters;
import sample.requests.DataForGetArchive;
import sample.requests.GetRequesterArchive;
import sample.requests.IGetRequester;
import sample.requests.IRequestData;


import java.io.File;
import java.io.IOException;

public class ArchiveLoader implements ArchiveListener {
    private Thread downloadJSONArchive;
    String jsonArchiveSource;
    String jsonSource;
    private Logger logger = LogManager.getLogger("General");

    public ArchiveLoader(){
        //TODO: read json archive directory url parameters,
        INewVisionParams param =Parameters.loadParameters("managerParameters\\parameters.xml");
        this.jsonSource = param.getJsonSource();
        this.jsonArchiveSource = param.getJsonArchiveSource();

    }

    /**
     * request to get json with link to JSONs archive
     * if archive is found {create folder for JSONs, run new tread to download JSONs archive}
     */
    @Override
    public void onJsonRequest(final String jsonArchiveName) {
        final String jsonARchiveURL = jsonArchiveSource +jsonArchiveName;
        System.out.println("download archive.........." + jsonARchiveURL);
        //get JsonFrom Srver
        //............
        //  **************

        //get json with link on file
        DataForGetArchive data = new DataForGetArchive();
        data.setName(jsonArchiveName);
        data.setRequestURL(jsonArchiveSource);

        GetRequesterArchive getRequester = new GetRequesterArchive();
        //check link

        if(getRequester.sendGETRequest(data)){

            final String pathToArchive = PathJSONParser.getPathToArchive(getRequester.getResponse());
            //createFolder
            File folder = new File(jsonSource +jsonArchiveName);
            if (!folder.exists()){
                folder.mkdir();
            }
            downloadJSONArchive = new Thread(new Runnable() {
                @Override
                public void run() {
                    //download archive
                    FileDownloader fileDownloader = new FileDownloader();
                    try {
                        fileDownloader.downloadFile(pathToArchive, jsonSource+jsonArchiveName+".zip");
                    } catch (IOException e) {
                        logger.error(e);
                        e.printStackTrace();
                    }

                    //unZip archive
                    Archiver archiver = new Archiver();
                    archiver.unZip(jsonSource+jsonArchiveName+".zip",jsonSource);


                }
            });
            downloadJSONArchive.start();



        }

    }
}
