package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLZoneLocationDAO extends AbstractJDBCDao<ZoneLocation, Integer>{

    private String tableName = "ZoneLocation";

    public MySQLZoneLocationDAO(Connection connection) {
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
    protected List<ZoneLocation> parseResultSet(ResultSet rs) {
        LinkedList<ZoneLocation> result = new LinkedList<ZoneLocation>();
        try{
            while (rs.next()){
                ZoneLocation l = new ZoneLocation(rs);
                result.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, ZoneLocation object) throws Exception {

    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, ZoneLocation object) throws Exception {

    }

    @Override
    public ZoneLocation create(ZoneLocation object) throws SQLException {
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
