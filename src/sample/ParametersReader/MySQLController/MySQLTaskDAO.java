package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return "SELECT * FROM task  WHERE date_add=(select MIN(date_add) FROM task)AND complitted=0;";
    }

    @Override
    public String getConditionOfQuery() {
        return null;
    }

    @Override
    public String getCreateQuery() {
        return null;
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }

    @Override
    public String getUpdateTransmittedQuery() {
        return null;
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM "+ tableName +" WHERE id = ?;";
    }

    @Override
    public String getCountQuery() {
        return null;
    }

    @Override
    public String createNewTableQuery() {
        return null;
    }

    @Override
    protected List<Task> parseResultSet(ResultSet rs) {
        LinkedList<Task> result = new LinkedList<Task>();
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
    protected void prepareStatementForInsert(PreparedStatement statement, Task object) throws Exception {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Task object) throws Exception {

    }

    @Override
    public Task create(Task object) throws SQLException {
        return null;
    }

    @Override
    public Task getByTaskId(int id) {
        return null;
    }

    @Override
    public String createNewTable(Class cl) {
        return null;
    }

    @Override
    public void setTableName(Class cl) {

    }

    @Override
    public Task getEarlyTask()throws Exception{
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
        return list.iterator().next();
    }
}
