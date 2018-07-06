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

    public abstract String getByTaskIdQuery();

    /**
     * @return second part of Query to get some number of record by transmitted.
     * <p/>
     * WHERE transmitted = ? LIMIT ?
     */
    public abstract String getConditionOfQuery();

    /**
     * Return Query to create new record in Line table.
     * <p/>
     * INSERT INTO [Table] ([column, column, ...]) VALUES (?, ?, ...);
     */
    public abstract String getCreateQuery();

    /**
     * Return Query to update record in Line table.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateQuery();

    /**
     * Return Query to update record with transmitted = true in Line table.
     * <p/>
     * UPDATE [Table] SET [column = ?, column = ?, ...] WHERE id = ?;
     */
    public abstract String getUpdateTransmittedQuery();

    /**
     * Return Query to delete record by ID in table.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getDeleteQuery();

    /**
     * Return Query to get count of record in Line table.
     * <p/>
     * DELETE FROM [Table] WHERE id= ?;
     */
    public abstract String getCountQuery();

    /**
     * Return Query to create new table.
     * <p/>
     * CREATE TABLE [Table name]  (column = ?, column = ?, ...);
     */
    public abstract String createNewTableQuery();

    /**
     * Disassembles the ResultSet and returns a list of objects corresponding to the contents of the ResultSet
     * @param rs
     * @return returns a list of objects T
     */
    protected abstract List<T> parseResultSet(ResultSet rs);

    /**
     * Sets the arguments of the insert request according to the value of the fields of the object.
     * @param statement connection with table
     * @param object object to insert
     * @throws Exception
     */
    protected abstract void prepareStatementForInsert(PreparedStatement statement, T object) throws Exception;

    /**
     * Sets the arguments of the update request according to the value of the fields of the object.
     * @param statement connection with table
     * @param object object to update
     * @throws Exception
     */
    protected abstract void prepareStatementForUpdate(PreparedStatement statement, T object) throws Exception;

    /**
     * Create new record in DB Table
     * @param object determines table to create record
     * @return created record
     * @throws SQLException
     */
    @Override
    public T persist(T object) throws SQLException {
        T persistInstance = null;
        /**Add record*/
        String sql = getCreateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            prepareStatementForInsert(statement, object);
            int count = statement.executeUpdate();
            if (count != 1) {
                throw new Exception("On persist modify more then 1 record: " + count);
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**Get just insert record*/
        sql = getSelectQuery() + " WHERE id = LAST_INSERT_ID();";// + lastId;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            List<T> list = parseResultSet(rs);
            if ((list == null) || (list.size() != 1)) {
                throw new Exception("Exception on findByPK new persist data.");
            }
            persistInstance = list.iterator().next();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persistInstance;
    }

    /**
     * update record by ID object
     * @param object object of appropriate records
     */
    @Override
    public void update(T object) {
        String sql = getUpdateQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            prepareStatementForUpdate(statement, object);
            int count = statement.executeUpdate();
            if(count != 1){
                throw new Exception("On update modify more then 1 record: " + count);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the transmitted field of the record of the specified id according to the parameter transmitted
     * @param id id of the record you want to update
     * @param transmitted the value according to which you want to update the record
     */
    @Override
    public void updateTransmitted(int id, boolean transmitted) {
        String sql = getUpdateTransmittedQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setBoolean(1, transmitted);
            statement.setInt(2, id);
            int count = statement.executeUpdate();
            if(count != 1){
                throw new Exception("On update modify more then 1 record: " + count);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return  all records of the selected table as a list of objects
     */
    @Override
    public List<T> getAll(){
        List<T> list = new ArrayList<T>();
        String sql = getSelectQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)) {
            ResultSet rs = stm.executeQuery();
            list = parseResultSet(rs);
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * Returns n numbers of records with the value tr of the transmitted field
     * @param tr the value of transmitted field on which the search is performed
     * @param limit the number of entries you need to receive
     * @return
     */
    public List<T> getByTransmittedLimit(boolean tr, long limit){
        List<T> list = new ArrayList<T>();
        String sql = getSelectQuery() + getConditionOfQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBoolean(1,tr);
            stm.setLong(2, limit);
            ResultSet rs = stm.executeQuery();
            list = parseResultSet(rs);
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param key ID of records
     * @return records of table
     * @throws Exception
     */
    @Override
    public T getByPk(int key) throws Exception {
        List<T> list = null;
        String sql = getSelectQuery() + "WHERE id = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,key);
            ResultSet rs = statement.executeQuery();
            list = parseResultSet(rs);
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if ((list == null) || (list.size() == 0)){
            throw new Exception("Record with PK = " + key + " not found.");
        }
        if (list.size()>1){
            throw new Exception("Received more than one record.");
        }
        return list.iterator().next();
    }

    @Override
    public T getByTaskId(int task_id) throws Exception{
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
        return list.iterator().next();
    }

    public T getBy(String column,  String value)throws Exception{
        List<T> list = null;
        String sql = getSelectQuery() + "WHERE " +column+"="+value;
        try(PreparedStatement statement = connection.prepareStatement(sql)){
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

    /**
     * Delete record by ID
     * @param id ID of record
     */
    @Override
    public void delete(int id) {
        String sql = getDeleteQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)){
            stm.setInt(1,id);
            int count = stm.executeUpdate();
            if(count != 1){
                throw new Exception("On delete modify more then 1 record: " + count);
            }
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds number of records by 'transmitted'
     * @param transmitted to tell which data we want to get (with transmitted = true/false)
     * @return count of records by transmitted
     */
    @Override
    public long getCountTransmitted(boolean transmitted){
        long count = 0;
        String sql = getCountQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)){
            stm.setBoolean(1,transmitted);
            ResultSet rs = stm.executeQuery();
            rs.next();
            count = rs.getLong(1);
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Create new table
     * @param cl determine type of table to create
     * @return new table name
     */
    @Override
    public String creatingTable(Class cl){
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String tableName = cl.getSimpleName()+ dateFormat.format(new java.util.Date());
        String sql = "CREATE TABLE " + tableName + createNewTableQuery();
        try(PreparedStatement stm = connection.prepareStatement(sql)){
            stm.executeUpdate();
            stm.close();
            System.out.println("New table is: " + tableName);
        } catch (SQLException e){
            if(e.getErrorCode() == 1050) {
                tableName = null;
            } else e.printStackTrace();
        }
        return tableName;
    }

    public T getEarlyTask()throws Exception{
        return null;
    }
}
