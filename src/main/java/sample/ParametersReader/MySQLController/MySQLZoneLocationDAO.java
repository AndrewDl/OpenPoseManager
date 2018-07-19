package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLZoneLocationDAO extends AbstractJDBCDao<ZoneLocation, Integer>{

    private String tableName = "zonelocation";

    public MySQLZoneLocationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return "SELECT * FROM " + tableName +" ";
    }

    @Override
    public String getByTaskIdQuery() {
        return getSelectQuery()+"WHERE task_id = ? ORDER by location, point_number";
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


}
