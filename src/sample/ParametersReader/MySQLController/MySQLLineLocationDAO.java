package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLLineLocationDAO extends AbstractJDBCDao<LineLocation, Integer> {

    private String tableName = "LineLocation";

    public MySQLLineLocationDAO(Connection connection) {
        super(connection);
    }


    @Override
    public String getSelectQuery() {
        return "SELECT * FROM " + tableName +" ";
    }

    @Override
    public String getByTaskIdQuery() {
        return getSelectQuery()+"WHERE task_id = ?";
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
    protected List<LineLocation> parseResultSet(ResultSet rs) {
        LinkedList<LineLocation> result = new LinkedList<LineLocation>();
        try{
            while (rs.next()){
                LineLocation l = new LineLocation(rs);
                result.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, LineLocation object) throws Exception {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, LineLocation object) throws Exception {

    }

    @Override
    public LineLocation create(LineLocation object) throws SQLException {
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
