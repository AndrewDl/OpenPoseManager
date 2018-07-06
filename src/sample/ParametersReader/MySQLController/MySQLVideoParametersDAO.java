package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLVideoParametersDAO extends AbstractJDBCDao<VideoParameters, Integer> {

    private String tableName = "video";

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
    protected List<VideoParameters> parseResultSet(ResultSet rs) {
        LinkedList<VideoParameters> result = new LinkedList<VideoParameters>();
        try{
            while (rs.next()){
                VideoParameters l = new VideoParameters(rs);
                result.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, VideoParameters object) throws Exception {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, VideoParameters object) throws Exception {

    }

    @Override
    public VideoParameters create(VideoParameters object) throws SQLException {
        return null;
    }


    @Override
    public String createNewTable(Class cl) {
        return null;
    }

    @Override
    public void setTableName(Class cl) {

    }
}
