package managers;

/**
 * Implementation of this interface should contain logic for handling of specific tasks
 *
 * Created by Andrew on 11/28/17.
 */
public interface IManager {
    /**
     * implementation of this method should contain logic to start handling a task
     */
    void start();

    /**
     * implement this method to stop handling the task<br>
     *     <B>It is strongly recommended</B> to stop the task along with manager
     */
    void stop();
}
