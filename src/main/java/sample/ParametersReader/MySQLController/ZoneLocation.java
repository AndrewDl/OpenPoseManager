package sample.ParametersReader.MySQLController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class ZoneLocation {
    private int id;
    private int task_id;
    private String location;
    private int point_number;
    private int x;
    private int y;
    private Timestamp date_add;
    private Logger logger = LogManager.getLogger("MySQL");

    /**
     * create ZoneLocation object by rs
     * @param rs result of query
     */
    public ZoneLocation(ResultSet rs) {
        try {
            this.id = rs.getInt(1);
            this.task_id = rs.getInt(2);
            this.location = rs.getString(3);
            this.point_number = rs.getInt(4);
            this.x = rs.getInt(5);
            this.y = rs.getInt(6);
            this.date_add = rs.getTimestamp(7);

        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getTask_id() {
        return task_id;
    }

    public String getLocation() {
        return location;
    }

    public int getPoint_number() {
        return point_number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Timestamp getDate_add() {
        return date_add;
    }

    @Override
    public String toString() {
        return "ZoneLocation{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", location='" + location + '\'' +
                ", point_number=" + point_number +
                ", x=" + x +
                ", y=" + y +
                ", date_add=" + date_add +
                '}';
    }
}
