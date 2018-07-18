package sample.EventsProcessing;

import sample.parameters.INewVisionParams;
import sample.parameters.Parameters;

import java.io.File;

public class ArchiveLoader implements ArchiveListener {
    private Thread downloadJSONArchive;
    String arhivesDirURL;
    String jsonsLocalDir;
    public ArchiveLoader(){
        //TODO: read json archive directory url parameters,
        INewVisionParams param =Parameters.loadParameters("managerParameters\\parameters.xml");
        this.jsonsLocalDir = param.getJsonSource();
        this.arhivesDirURL = param.getJsonArchiveSource();

    }

    /**
     * request to get json with link to JSONs archive
     * if archive is found {create folder for JSONs, run new tread to download JSONs archive}
     */
    @Override
    public void onJsonRequest(String jsonArchiveName) {
        String jsonARchiveURL = arhivesDirURL+jsonArchiveName;
        System.out.println("download archive.........." + jsonARchiveURL);
        //get JsonFrom Srver
        //............
        //  **************


        //check link
        if(true){
            //createFolder
            File folder = new File(jsonsLocalDir+"\\"+jsonArchiveName);
            if (!folder.exists()){
                folder.mkdir();
            }




            downloadJSONArchive = new Thread(new Runnable() {
                @Override
                public void run() {
                    //download archive
                }
            });
            downloadJSONArchive.start();

        }

    }
}
