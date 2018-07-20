package sample.ParametersReader.MySQLController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLVideoParametersDAO extends AbstractJDBCDao<VideoParameters, Integer> {

    private String tableName = "video";

    private Logger logger = LogManager.getLogger("MySQL");

    public MySQLVideoParametersDAO(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM " + tableName +" ";
    }

    @Override
    public String getByTaskIdQuery() {
        return getSelectQuery()+"WHERE id = (SELECT video_id FROM task WHERE id = ?);";
    }

    @Override
    protected List<VideoParameters> parseResultSet(ResultSet rs) {
        LinkedList<VideoParameters> result = new LinkedList<VideoParameters>();
        try{
            while (rs.next()){
                VideoParameters l = new VideoParameters(rs);
                result.add(l);
            }
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return result;
    }


}
