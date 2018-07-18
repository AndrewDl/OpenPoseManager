package sample.EventsProcessing;

import java.util.EventListener;

public interface ArchiveListener extends EventListener {
    /**
     * download json archive
     * @param videoName
     */
    void onJsonRequest(String videoName);
}
