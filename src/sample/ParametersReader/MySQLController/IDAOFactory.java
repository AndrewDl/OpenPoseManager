package sample.ParametersReader.MySQLController;

import java.sql.*;
import java.sql.SQLException;

/** Factory of objects to working with database */
public interface IDAOFactory<Context> {


    interface DaoCreator<Context>{
        IGenericDAO create(Context context);
    }

    /**
     * @return connection to the database
     * @throws SQLException
     */
    Connection getConnection () throws SQLException;

    /**
     *
     * @param connection connection to DB
     * @param dtoClass class corresponding to DB table
     * @return an object that contains a connection with the current state of the database at that particular moment
     * @throws Exception
     */
    IGenericDAO getDAO(Connection connection, Class dtoClass) throws Exception;

}
