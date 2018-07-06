package sample.ParametersReader.MySQLController;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * universal database object interface
 * @param <T>
 * @param <PK>
 */
public interface IGenericDAO<T, PK extends Serializable> {

     /**
     * Creates a new record and its corresponding object
      *calling method persist
     * @param object object which need to write in DB
     * @return written object
     * @throws SQLException
     */
    T create(T object) throws SQLException;

    /**
     * Creates a new record corresponding to the object "object"
     * @param object object which need to write in DB
     * @return
     * @throws SQLException
     */
    T persist(T object) throws SQLException;

    /**
     * Returns an object corresponding to a record with a primary key "key" or null
     * @param key id of record you need
     * @return object corresponding to a record you need
     * @throws Exception
     */
    T getByPk(int key) throws Exception;

    T getByTaskId(int id)throws Exception;

    T getEarlyTask()throws Exception;

    T getBy(String column, String value) throws Exception;

    /**
     * Saves the state of the object in the database
     * @param object object which need to update in DB
     * @throws SQLException
     */
    void update(T object) throws SQLException;

    /**
     * @param tr transmitted = true/false
     * @param limit number of strings we need to get
     * @return List of objects with needed 'transmitted' and with size of 'limit'
     */
    List<T> getByTransmittedLimit(boolean tr, long limit);

    /**
     * Changes 'transmitted' by id
     * @param id - id of the object
     * @param transmitted
     * @throws SQLException
     */
    void updateTransmitted(int id, boolean transmitted) throws SQLException;

    /**
     * Delete record about an object from the database
     * @param id id of record need to delete
     * @throws SQLException
     */
    void delete(int id) throws SQLException;

    /**
     * @return all objects of the selected table
     * @throws SQLException
     */
    List<T> getAll() throws SQLException;

    /**
     * Finds number of records by 'transmitted'
     * @param transmitted to tell which data we want to get (with transmitted = true/false)
     * @return count of records by transmitted
     */
    long getCountTransmitted(boolean transmitted) throws SQLException;

    /**
     * need to point on class of creating table
     * @param cl class table of which need create
     * @return Name of new table
     */
    String createNewTable(Class cl);

    /**
     * Create new table
     * @param cl determine type of table to create
     * @return new table name
     */
    String creatingTable(Class cl);

    /**
     * Set name of table with which we wood work
     * @param cl name of this table
     */
    void setTableName(Class cl);

}
