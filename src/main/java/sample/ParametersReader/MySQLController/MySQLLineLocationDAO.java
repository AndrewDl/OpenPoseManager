package sample.ParametersReader.MySQLController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MySQLLineLocationDAO extends AbstractJDBCDao<LineLocation, Integer> {

    private String tableName = "linelocation";

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

}
