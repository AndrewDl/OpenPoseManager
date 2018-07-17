package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MySQLTaskDAO extends AbstractJDBCDao<Task, Integer> {

    private String tableName = "ZoneLocation";

    public MySQLTaskDAO(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM " + tableName +" ";
    }

    @Override
    public String getByTaskIdQuery() {
        return getSelectQuery()+"WHERE id = ?";
    }

    public String getEarlyTaskQuery(){
        return "select * from task WHERE completed = 0 order by date_add";
                //"select * from task WHERE date_add=(select min(date_add) FROM newvision.task where completed=0)and completed = 0 limit 1;";
    }

    private String getCompleteQuery(){
        return "update newvision.task set completed = 1 where id = ?;";
    }

    @Override
    protected ArrayList<Task> parseResultSet(ResultSet rs) {
        ArrayList<Task> result = new ArrayList<Task>();
        try{
            while (rs.next()){
                Task l = new Task(rs);
                result.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public List<Task> getByTaskId(int id) {
        return null;
    }

    @Override
    public List<Task> getEarlyTask()throws Exception{
        List<Task> list = null;
        String sql = getEarlyTaskQuery();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if ((list == null) || (list.size() == 0)){
            throw new Exception("Record with  not found.");
        }
        /*if (list.size()>1){
            throw new Exception("Received more than one record.");
        }*/
        return list;
    }

    @Override
    public void setCompleted(int task_id){
        String sql = getCompleteQuery();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,task_id);
            statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
