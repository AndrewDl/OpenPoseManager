package sample.ParametersReader.MySQLController;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**implements similar methods of obtaining data from the database*/
public abstract class AbstractJDBCDao<T, PK extends Serializable> implements IGenericDAO<T,PK> {

    public AbstractJDBCDao(Connection connection){
        this.connection = connection;
    }

    protected Connection connection;

    /**
     * Return Query to get all records.
     * <p/>
     * SELECT * FROM [Table]
     */
    public abstract String getSelectQuery();

    /**
     *
     * @return SELECT * FROM [Table] WHERE task_id = ?
     */
    public abstract String getByTaskIdQuery();

    /**
     * Disassembles the ResultSet and returns a list of objects corresponding to the contents of the ResultSet
     * @param rs
     * @return returns a list of objects T
     */
    protected abstract List<T> parseResultSet(ResultSet rs);

    /**
     * set cell 'completed' true in task table
     * @param id - id of the object
     */
    @Override
    public void setCompleted(int id){

    }

    /**
     * Returns list of objects corresponding to a record with a primary key "task_id" or null
     * @param task_id - id of the task
     * @return list of required objects T
     * @throws Exception
     */
    @Override
    public List<T> getByTaskId(int task_id) throws Exception{
        List<T> list = null;
        String sql = getByTaskIdQuery();
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,task_id);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if ((list == null) || (list.size() == 0)){
            throw new Exception("Record with PK = " + task_id + " not found.");
        }
        /*if (list.size()>1){
            throw new Exception("Received more than one record.");
        }*/
        return list;
    }


    public T getBy(String column,  String value)throws Exception{
        List<T> list = null;
        String sql = getSelectQuery() + "WHERE ? = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,column);
            statement.setString(2,value);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if ((list == null) || (list.size() == 0)){
            throw new Exception("Record with " + column +": "+ value + " not found.");
        }
        if (list.size()>1){
            throw new Exception("Received more than one record.");
        }
        return list.iterator().next();
    }



    public List<T> getEarlyTask()throws Exception{
        return null;
    }
}
