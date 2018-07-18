package sample.parameters;

/**
 * Created by Andrew on 11/28/17.
 */
public interface IOpenPoseParams {
    String getVideoSource();

    String getVideoDestination();

    String getArguments();

    String getOpenPose();

    String getURLforGET();

    String getURLforPOST();

    String getTelegramURL();

    String getKeyURL();
}
