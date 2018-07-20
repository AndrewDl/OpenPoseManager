package sample.ParametersReader.MySQLController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class use to determine different connection to different table of DB
 */
public class MySQLDaoFactory implements IDAOFactory {

    private Logger logger = LogManager.getLogger("MySQL");

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private Map<Class, DaoCreator> creators;

    /**
     * create a list of possible database classes
     * @param DB_URL - connection to DB
     * @param DB_USER - connection to DB
     * @param DB_PASSWORD - connection to DB
     */
    public MySQLDaoFactory(String DB_URL, String DB_USER, String DB_PASSWORD){
        this.DB_URL = DB_URL;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
        creators = new HashMap<Class, DaoCreator>();

        creators.put(LineLocation.class, new DaoCreator<Connection>() {
            @Override
            public IGenericDAO create(Connection connection) {
                return new MySQLLineLocationDAO(connection);
            }
        });

        creators.put(ZoneLocation.class, new DaoCreator<Connection>() {
            @Override
            public IGenericDAO create(Connection connection) {
                return new MySQLZoneLocationDAO(connection);
            }
        });

        creators.put(VideoParameters.class, new DaoCreator<Connection>() {
            @Override
            public IGenericDAO create(Connection connection) {
                return new MySQLVideoParametersDAO(connection);
            }
        });

        creators.put(Task.class, new DaoCreator<Connection>() {
            @Override
            public IGenericDAO create(Connection connection) {
                return new MySQLTaskDAO(connection);
            }
        });

    }


    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    @Override
    public IGenericDAO getDAO(Connection connection, Class dtoClass) throws Exception {
        DaoCreator creator = creators.get(dtoClass);
        if(creator == null){
            throw new Exception("Dao object " + " not found.");
        }
        return creator.create(connection);
    }

}
