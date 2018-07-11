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
     * Returns list of objects corresponding to a record with a primary key "id" or null
     * @param id of record you need
     * @return objects corresponding to a record you need
     * @throws Exception
     */
    List<T> getByTaskId(int id)throws Exception;

    /**
     * get task id where completed = 0, sorted by date add
     * @return list of objects T
     * @throws Exception
     */
    List<T> getEarlyTask()throws Exception;


    /**
     * Changes 'completed' by id
     * @param id - id of the object
     */
    void setCompleted(int id);

    /**
     * get first find object where column = value
     * @param column name of column
     * @param value - value to column
     * @return first find object T
     * @throws Exception
     */
    T getBy(String column, String value) throws Exception;

}
