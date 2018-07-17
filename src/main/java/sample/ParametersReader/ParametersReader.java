package sample.ParametersReader;

import sample.ParametersReader.MySQLController.*;
import imageProcessing.SceneLineParams;
import imageProcessing.ScenePolygonParams;
import sample.XMLwriterReader;
import sample.parameters.Parameters;

import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * class that realize getting prepared task parameters from DB
 * EXAMPLE
 *  try {
 *            ParametersReader parametersNV = new ParametersReader();
 *            for (int i = 0; i < 5; i++) {
 *                System.out.println(parametersNV.getVideoParameters().toString());
 *                ArrayList<SceneLineParams> sceneLineParams = new ArrayList<>(parametersNV.getSceneLineParams());
 *                for (SceneLineParams lineParam :
 *                        sceneLineParams) {
 *                    System.out.println("- " + lineParam.toString());
 *                }
 *                parametersNV.nextAfterThis();
 *            }
 *        }catch (Exception e){
 *            e.printStackTrace();
 *        }
 */
public class ParametersReader {
    private static ParametersReader instance;
    private Task task = null;
    private VideoParameters videoParameters = null;
    private ArrayList<SceneLineParams> sceneLineParams = new ArrayList<SceneLineParams>();
    private ArrayList<ScenePolygonParams> scenePolygonParams = new ArrayList<ScenePolygonParams>();

    /**
     * create parameters by early created task from DB where completed = 0
     * @throws Exception not completed task with JSONs not found
     */
    private ParametersReader(){

//        this.task = getEarlyTask();
//        this.videoParameters = getVideoParametersByTaskId(task.getId());
//        List<LineLocation> lineLocation = getLineLocationByTaskID(task.getId());
//        if(lineLocation != null)
//            for (LineLocation loc: lineLocation) {
//                SceneLineParams sceneLineParam = new SceneLineParams(loc);
//                this.sceneLineParams.add(sceneLineParam);
//            }
//        this.scenePolygonParams = new ArrayList<>(getPolygonParamsByTaskId(task.getId()));
//        System.out.println(videoParameters.toString());
//        System.out.println(task.toString());
//        System.out.println(lineLocation.toString());

    }
    public static synchronized ParametersReader getInstance(){
        if(instance==null){
            instance = new ParametersReader();
        }

        return instance;
    }


    /**
     * set this task complete
     * @return parameters by next early not completed task
     */
    public ParametersReader nextAfterThis()throws Exception{
        if(task!=null)
            makeTaskComplete(this.task.getId());
        this.task = getEarlyTask();
        this.videoParameters = getVideoParametersByTaskId(task.getId());
        List<LineLocation> lineLocation = getLineLocationByTaskID(task.getId());
        if(lineLocation != null)
            for (LineLocation loc: lineLocation) {
                SceneLineParams sceneLineParam = new SceneLineParams(loc.getX1(),loc.getY1(),loc.getX2(),loc.getY2());
                sceneLineParam.setLocation(loc.getLocation());
                sceneLineParam.setName(loc.getLocation());
                this.sceneLineParams.add(sceneLineParam);
            }
        this.scenePolygonParams = new ArrayList<>(getPolygonParamsByTaskId(task.getId()));
        return  this;
    }

    public Task getTask() {
        return task;
    }

    public VideoParameters getVideoParameters() {
        return videoParameters;
    }

    public ArrayList<SceneLineParams> getSceneLineParams() {
        return sceneLineParams;
    }

    public ArrayList<ScenePolygonParams> getScenePolygonParams() {
        return scenePolygonParams;
    }

    /**
     * get all LineLocation objects where task_id=id
     * @param id - id of task
     * @return list of LineLocation object
     */
    private LinkedList<LineLocation> getLineLocationByTaskID(int id){
        //TODO: зробити параметри глобільні, щоб в кожному методі не звертатися до файлу
        XMLwriterReader<Parameters> reader = new XMLwriterReader("managerParameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        LinkedList<LineLocation> lineLocations = null;
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, LineLocation.class);
            //получение записи по ID
            lineLocations = (LinkedList<LineLocation>) daoL.getByTaskId(id);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            lineLocations = null;
            System.out.println("Not found Line by task" + id +"!");
            //e.printStackTrace();
        }

        return lineLocations;
    }

    /**
     * get all ZoneLocation objects where task_id=id
     * @param id - id of task
     * @return list of ZoneLocation object
     */
    private LinkedList<ZoneLocation> getZoneLocationByTaskID(int id){
        XMLwriterReader<Parameters> reader = new XMLwriterReader("managerParameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);

        LinkedList<ZoneLocation> zoneLocation = new LinkedList<>();
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, ZoneLocation.class);
            //получение записи по ID
            zoneLocation = (LinkedList<ZoneLocation>) daoL.getByTaskId(id);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            zoneLocation = null;
            System.out.println("Not found Zone by task" + id +"!");
            //e.printStackTrace();
        }
        return zoneLocation;
    }

    /**
     * get VideoParameters object by task_id
     * @param task_id - id of task
     * @return VideoParameters object
     */
    private VideoParameters getVideoParametersByTaskId(int task_id){
        XMLwriterReader<Parameters> reader = new XMLwriterReader("managerParameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        VideoParameters videoParameters = null;
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, VideoParameters.class);
            //получение записи по ID
            videoParameters = (VideoParameters) daoL.getByTaskId(task_id).get(0);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return videoParameters;
    }

    /**
     * get early task_id where completed=0 and JSON folder are exist
     * @return id of the task
     * @throws Exception Not found any task where completed=0 and JSON folder are exist
     */
    private Task getEarlyTask() throws Exception {
        XMLwriterReader<Parameters> reader = new XMLwriterReader("managerParameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        List<Task> tasks = null;
        Task task = null;
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Task.class);
            //получение записи по ID
            tasks = (List<Task>) daoL.getEarlyTask();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Task t :
                tasks) {
            String videoName = getVideoParametersByTaskId(t.getId()).getName();
            String path = param.getJsonSource()+"\\"+videoName;
            File f = new File(path+"_toProcess");
            if(f.exists()){
                task = t;
                return task;
            }//TODO: check server
            else{
                f = new File(path);
                if (f.exists()) {
                    System.out.println("JSON by "+videoName+" video, in processing");
                }else {
                    //TODO: check server
//                    if(isJSONonServer){
//                        //start load and create folder
//                    }else
                        System.out.println("Not found JSON by "+videoName+" video");
                }
            }
        }
        if(task==null){
            throw new Exception("Not found any prepared task");
        }
        return task;
    }

    /**
     * get all ZoneLocation by task_id, sorted by date of addition and point number,
     * group them by location, assign points related to a particular location to the corresponding object ScenePolygonParams
     * @param task_id - id of task
     * @return list of ScenePolygonParams
     */
    private List<ScenePolygonParams> getPolygonParamsByTaskId(int task_id){
        LinkedList<ScenePolygonParams> scenePolygonParams = new LinkedList<ScenePolygonParams>();
        List<ZoneLocation> zoneLocation = getZoneLocationByTaskID(task_id);
        while(zoneLocation != null && zoneLocation.size()>0){
            ScenePolygonParams polygon = new ScenePolygonParams();
            polygon.location = (zoneLocation).get(0).getLocation();
            polygon.name = (zoneLocation).get(0).getLocation();
            ArrayList<Point> points = new ArrayList<>();
            List<ZoneLocation> zl = new LinkedList<ZoneLocation>(zoneLocation);
            for (ZoneLocation loc: zl) {
                if(loc.getLocation().equals(polygon.location)){
                    points.add(new Point(loc.getX(),loc.getY()));
                    zoneLocation.remove(loc);
                }
                System.out.println(loc.toString());
            }
            polygon.setPoints(points);
            scenePolygonParams.add(polygon);
        }
//        for(ScenePolygonParams pol: scenePolygonParams){
//            ArrayList<Point> points = new ArrayList<>(pol.getPoints());
//            System.out.println(points.toString());
//        }
        return scenePolygonParams;
    }

    /**
     * make task where id = task_id completed = 0
     * @param task_id id of the task
     */
    private void makeTaskComplete(int task_id) {
        XMLwriterReader<Parameters> reader = new XMLwriterReader("managerParameters/parameters.xml");
        Parameters param = reader.ReadFile(Parameters.class);
        Task task = null;
        //создание фабрики объектов для работы с базой данных
        IDAOFactory daoFactory = new MySQLDaoFactory(param.getDB_URL(), param.getDB_USER(), param.getDB_PASSWORD());
        try(Connection con = daoFactory.getConnection()){
            //создание объекта реализующего интерфейс работы с базой данных
            IGenericDAO daoL = daoFactory.getDAO(con, Task.class);
            daoL.setCompleted(task_id);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
