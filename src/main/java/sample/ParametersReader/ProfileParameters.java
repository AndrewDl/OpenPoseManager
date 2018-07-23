package sample.ParametersReader;


import imageProcessing.SceneLineParams;
import imageProcessing.ScenePolygonParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sample.XMLwriterReader;
import sample.parameters.ISlashInPath;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Andrew on 28.05.2017.
 */
public class ProfileParameters implements ISubtractorParameters, ISlashInPath {

    private Logger logger = LogManager.getLogger("MySQL");

    public ProfileParameters(){
        addLineParams(new SceneLineParams(0,0,0,0));
    }

    /**
     * This method is used to load parameters from a given file<br>
     *     Method uses deserialization to load instance from an xml file
     * @param file path to parameters xml file
     * @return parameters from the file
     */
    public static ProfileParameters loadProfileParameters(String file) {
        //final Logger userLogger = LogManager.getLogger("MainLogger");

        XMLwriterReader<ProfileParameters> reader = new XMLwriterReader(file);

        ProfileParameters profile = null;

        profile = reader.ReadFile(ProfileParameters.class);

        profile.addressRTSP = profile.checkIsSlash(profile.addressRTSP);
        profile.snapshotAddress =profile.checkIsSlash(profile.snapshotAddress);
        profile.db_host =profile.checkIsSlash(profile.db_host);
        profile.jsonFolderPath =profile.checkIsSlash(profile.jsonFolderPath);
        profile.Domain =profile.checkIsSlash(profile.Domain);
        profile.PostfixCounter =profile.checkIsSlash(profile.PostfixCounter);
        profile.PostfixTrack =profile.checkIsSlash(profile.PostfixTrack);
        profile.PostfixSnapshot =profile.checkIsSlash(profile.PostfixSnapshot);

        return profile;
    }

    /**
     * Writes parameters of profile to file
     * @param parameters that are to be written
     * @param path of the file we write to
     */
    public void writeProfileParameters(ProfileParameters parameters, String path){
        XMLwriterReader<ProfileParameters> writer = new XMLwriterReader<>(path);
        try {
            writer.WriteFile(parameters, ProfileParameters.class);
        } catch (IOException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    private ArrayList<SceneLineParams> sceneLineParams = new ArrayList<>();
    //TODO: це треба буде прибрати коли вдасться підігнати всі регіони під один інтерфейс
    private ArrayList<ScenePolygonParams> scenePolygons = new ArrayList<>();

    private String SceneLocation;

    //private ICamera Camera = new CameraImageJavaCV("addr");


    private String pose_keypoints_key;
    private String addressRTSP;
    private String camName;
    private String snapshotAddress;
    private String db_host;
    private String db_name;
    private String db_user;
    private String db_password;
    private String videoDate;

    private boolean EnableAutoconnect;
    private boolean EnableProcessing = false;
    private boolean EnableCounting = false;
    private boolean EnableSendData = false;
    private boolean CenterTrackingPoint = false;

    private int reconnectTimeout;

    private int personArea;
    private int ObjectArea;
    private double BinarizationThreshold;
    private int MergeRadius;
    private int MergeRememberDepth;
    private int minValueColorAdd;
    private int maxValueColorAdd;
    private int colorAdd;
    private int neuralLimit;
    private int videoDateSource;
    private int taskID;


    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    private int frameRate;

    public int getDeleteRadius() {
        return deleteRadius;
    }

    public void setDeleteRadius(int deleteRadius) {
        this.deleteRadius = deleteRadius;
    }

    private int deleteRadius;

    public String getWeightsFilePath() {
        return weightsFilePath;
    }

    public void setWeightsFilePath(String weightsFileName) {
        this.weightsFilePath = checkIsSlash(weightsFileName);
    }

    private String weightsFilePath;
    private String jsonFolderPath;

    private int notIgnoreRectX;
    private int notIgnoreRectY;
    private int notIgnoreRectWidth;
    private int notIgnoreRectHeight;

    private int ScaleRate;
    private int tempWidth;
    private int tempHeight;
    private int tempPercent;
    private int tempShift;

    private String Domain;
    private String PostfixCounter;
    private String PostfixTrack;
    private String PostfixSnapshot;


    private int BackgroundRefreshRate;

    private int AcceptObjectRate;
    private int AcceptRadius;

    private float ImageSendPeriodInHours;
    private int DataSendPeriod;
    private int DataLogPackageLimit;
    private int KeepArchivedDataInHours;

    private boolean collectHeatmapFlag;

    private int MatchingThreshold;
    private int RequiredMatches;
    private int UpdateFactor;

    private boolean StandAloneFilter;
    private int AlgorithmType;
    private int TrackingType;

    private String videoDateRegEx;
    private String jsonNameRegEx;
    private String videoDateFormat;

    public String getJsonFolderPath() {
        return jsonFolderPath;
    }

    public void setJsonFolderPath(String jsonFolderPath) {
        this.jsonFolderPath = checkIsSlash(jsonFolderPath);
    }

    public int getNeuralLimit() {
        return neuralLimit;
    }

    public void setNeuralLimit(int neuralLimit) {
        this.neuralLimit = neuralLimit;
    }

    public void addLineParams(SceneLineParams params){
        sceneLineParams.add(params);
    }

    public ArrayList<SceneLineParams> getSceneLineParams(){
        return sceneLineParams;
    }

    //TODO: це треба буде прибрати коли вдасться підігнати всі регіони під один інтерфейс
    public ArrayList<ScenePolygonParams> getScenePolygons(){
        return scenePolygons;
    }
    public void setScenePolygons(ArrayList<ScenePolygonParams> scenePolygons){
        this.scenePolygons = scenePolygons;
    }
    public void addZones(ScenePolygonParams polygonParams){
        scenePolygons.add(polygonParams);
    }

    public void setEnableProcessing(boolean enableProcessing){
        EnableProcessing = enableProcessing;
    }

    public boolean getEnableProcessing(){
        return EnableProcessing;
    }

    public void setEnableCounting(boolean enableCounting){
        EnableCounting = enableCounting;
    }

    public boolean getEnableCounting(){
        return EnableCounting;
    }

    public void setEnableSendData(boolean enableSendData){
        EnableSendData = enableSendData;
    }

    public boolean getEnableSendData(){
        return EnableSendData;
    }

    public int getObjectArea() {
        return ObjectArea;
    }

    public void setObjectArea(int objectArea) {
        ObjectArea = objectArea;
    }

    public void setBinarizationThreshold(double binarizationThreshold) {
        BinarizationThreshold = binarizationThreshold;
    }

    public double getBinarizationThreshold() {
        return BinarizationThreshold;
    }

    public int getMergeRadius() {
        return MergeRadius;
    }

    public void setMergeRadius(int mergeRadius) {
        MergeRadius = mergeRadius;
    }

    public int getScaleRate() {
        return ScaleRate;
    }

    public int getTempShift(){
        return tempShift;
    }

    public int getTempWidth(){
        return tempWidth;
    }

    public int getTempHeight(){
        return tempHeight;
    }

    public int getTempPercent(){return tempPercent;}

    public int getPersonArea() {
        return personArea;
    }

    public void setPersonArea(int personArea) {
        this.personArea = personArea;
    }
    public int getMinValueColorAdd() {
        return minValueColorAdd;
    }

    public void setMinValueColorAdd(int minValueColorAdd) {
        this.minValueColorAdd = minValueColorAdd;
    }

    public int getMaxValueColorAdd() {
        return maxValueColorAdd;
    }

    public void setMaxValueColorAdd(int maxValueColorAdd) {
        this.maxValueColorAdd = maxValueColorAdd;
    }

    public int getColorAdd() {
        return colorAdd;
    }

    public void setColorAdd(int colorAdd) {
        this.colorAdd = colorAdd;
    }

    /**
     * Set's image scale rate.
     * @param scaleRate how many times the image for analysis will be smaller than original image from camera
     */
    public void setScaleRate(int scaleRate) {
        ScaleRate = scaleRate;
    }

    public void setTempShift(int shift){
        tempShift = shift;
    }

    public void setTempWidth(int width){
        tempWidth = width;
    }

    public void setTempHeight(int height){
        tempHeight = height;
    }

    public void setTempPercent(int percent) {tempPercent = percent;}

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public int getBackgroundRefreshRate() {
        return BackgroundRefreshRate;
    }

    public void setBackgroundRefreshRate(int backgroundRefreshRate) {
        BackgroundRefreshRate = backgroundRefreshRate;
    }

    public boolean isCenterTrackingPoint() {
        return CenterTrackingPoint;
    }

    public void setCenterTrackingPoint(boolean horizontalFlow) {
        CenterTrackingPoint = horizontalFlow;
    }

    public int getAcceptObjectRate() {
        return AcceptObjectRate;
    }

    public void setAcceptObjectRate(int acceptObjectRate) {
        AcceptObjectRate = acceptObjectRate;
    }

    public int getAcceptRadius() {
        return AcceptRadius;
    }

    public void setAcceptRadius(int acceptRadius) {
        AcceptRadius = acceptRadius;
    }

    public int getDataSendPeriod() {
        return DataSendPeriod;
    }

    public void setDataSendPeriod(int dataSendPeriod) {
        DataSendPeriod = dataSendPeriod;
    }

    public int getDataLogPackageLimit() {
        return DataLogPackageLimit;
    }

    public void setDataLogPackageLimit(int dataLogPackageLimit) {
        DataLogPackageLimit = dataLogPackageLimit;
    }

    public int getKeepArchivedDataInHours() {
        return KeepArchivedDataInHours;
    }

    public void setKeepArchivedDataInHours(int keepArchivedDataInHours) {
        KeepArchivedDataInHours = keepArchivedDataInHours;
    }

    public String getSceneLocation() {
        return SceneLocation;
    }

    public void setSceneLocation(String sceneLocation) {
        SceneLocation = sceneLocation;
    }

    public String getPostfixCounter() {
        return PostfixCounter;
    }

    public void setPostfixCounter(String postfixCounter) {
        PostfixCounter = checkIsSlash(postfixCounter);
    }

    public String getPostfixTrack() {
        return PostfixTrack;
    }

    public void setPostfixTrack(String postfixTrack) {
        PostfixTrack = checkIsSlash(postfixTrack);
    }

    public String getPostfixSnapshot() {
        return PostfixSnapshot;
    }

    public void setPostfixSnapshot(String postfixSnapshot) {
        PostfixSnapshot = checkIsSlash(postfixSnapshot);
    }

    public float getImageSendPeriodInHours() {
        return ImageSendPeriodInHours;
    }

    public void setImageSendPeriodInHours(int imageSendPeriodInHours) {
        ImageSendPeriodInHours = imageSendPeriodInHours;
    }

    public int getReconnectTimeout() {
        return reconnectTimeout;
    }

    public void setReconnectTimeout(int reconnectTimeout) {
        this.reconnectTimeout = reconnectTimeout;
    }

    public boolean isEnableAutoconnect() {
        return EnableAutoconnect;
    }

    public void setEnableAutoconnect(boolean enableAutoconnect) {
        this.EnableAutoconnect = enableAutoconnect;
    }

    public int getNotIgnoreRectX() {
        return notIgnoreRectX;
    }

    public void setNotIgnoreRectX(int notIgnoreRectX) {
        this.notIgnoreRectX = notIgnoreRectX;
    }

    public int getNotIgnoreRectY() {
        return notIgnoreRectY;
    }

    public void setNotIgnoreRectY(int notIgnoreRectY) {
        this.notIgnoreRectY = notIgnoreRectY;
    }

    public int getNotIgnoreRectWidth() {
        return notIgnoreRectWidth;
    }

    public void setNotIgnoreRectWidth(int notIgnoreRectWidth) {
        this.notIgnoreRectWidth = notIgnoreRectWidth;
    }

    public int getNotIgnoreRectHeight() {
        return notIgnoreRectHeight;
    }

    public void setNotIgnoreRectHeight(int notIgnoreRectHeight) {
        this.notIgnoreRectHeight = notIgnoreRectHeight;
    }

    public String getAddressRTSP() {
        return addressRTSP;
    }

    public void setAddressRTSP(String addressRTSP) {
        this.addressRTSP = checkIsSlash(addressRTSP);
    }

    public String getCamName() {
        return camName;
    }

    public void setCamName(String camName) {
        this.camName = camName;
    }

    public boolean isCollectHeatmapFlag() {
        return collectHeatmapFlag;
    }

    public void setCollectHeatmapFlag(boolean collectHeatmapFlag) {
        this.collectHeatmapFlag = collectHeatmapFlag;
    }

    @Override
    public int getMatchingThreshold() {
        return MatchingThreshold;
    }

    @Override
    public void setMatchingThreshold(int matchingThreshold) {
        MatchingThreshold = matchingThreshold;
    }

    @Override
    public int getRequiredMatches() {
        return RequiredMatches;
    }

    @Override
    public void setRequiredMatches(int requiredMatches) {
        RequiredMatches = requiredMatches;
    }

    @Override
    public int getUpdateFactor() {
        return UpdateFactor;
    }

    @Override
    public void setUpdateFactor(int updateFactor) {
        UpdateFactor = updateFactor;
    }

    public boolean isStandAloneFilter() {
        return StandAloneFilter;
    }

    public void setStandAloneFilter(boolean standAloneFilter) {
        StandAloneFilter = standAloneFilter;
    }

    public int getAlgorithmType() {
        return AlgorithmType;
    }

    public void setAlgorithmType(int algorithmType) {
        AlgorithmType = algorithmType;
    }

    public String getSnapshotAddress() {
        return snapshotAddress;
    }

    public void setSnapshotAddress(String snapshotAddress) {
        this.snapshotAddress = checkIsSlash(snapshotAddress);
    }

    public int getMergeRememberDepth() {
        return MergeRememberDepth;
    }

    public void setMergeRememberDepth(int mergeRememberDepth) {
        MergeRememberDepth = mergeRememberDepth;
    }

    public void setDb_host(String DB_HOST){
        this.db_host = checkIsSlash(DB_HOST);
    }

    public void setDb_name(String DB_NAME){
        this.db_name = DB_NAME;
    }

    public void setDb_user(String DB_USER){
        this.db_user = DB_USER;
    }

    public void setDb_password(String DB_PASSWORD){
        this.db_password = DB_PASSWORD;
    }

    public String getDb_host(){
        return db_host;
    }

    public String getDb_name(){
        return db_name;
    }

    public String getDb_user(){
        return db_user;
    }

    public String getDb_password(){
        return db_password;
    }
    public String getVideoDateRegEx() {
        return videoDateRegEx;
    }

    public void setVideoDateRegEx(String videoDateRegEx) {
        this.videoDateRegEx = videoDateRegEx;
    }

    public String getJsonNameRegEx() {
        return jsonNameRegEx;
    }

    public void setJsonNameRegEx(String jsonNameRegEx) {
        this.jsonNameRegEx = jsonNameRegEx;
    }

    public String getVideoDateFormat() {
        return videoDateFormat;
    }

    public void setVideoDateFormat(String videoDateFormat) {
        this.videoDateFormat = videoDateFormat;
    }

    public String getPose_keypoints_key() {
        return pose_keypoints_key;
    }

    public void setPose_keypoints_key(String pose_keypoints_key) {
        this.pose_keypoints_key = pose_keypoints_key;
    }

    public int getTrackingType() {
        return TrackingType;
    }

    public void setTrackingType(int trackingType) {
        TrackingType = trackingType;
    }

    public String getVideoDate(){return videoDate;}

    public void setVideoDate(String videoDate){this.videoDate = videoDate;}

    public Integer getVideoDateSource(){return videoDateSource;}

    public void setVideoDateSource(Integer videoDateSource){this.videoDateSource = videoDateSource;}

    public void setTaskID(int taskid){
        this.taskID = taskid;
    }

    public int getTaskID(){
        return taskID;
    }

    public void setSceneLineParams(ArrayList<SceneLineParams> sceneLineParams) {
        this.sceneLineParams = sceneLineParams;
    }

    @Override
    public String checkIsSlash(String path) {
        if(path.substring(path.length()-1).equals("\\") || path.substring(path.length()-1).equals("/")){
            path = path.substring(0,path.length()-1);
            path = checkIsSlash(path);
        }
        return path;
    }

}
