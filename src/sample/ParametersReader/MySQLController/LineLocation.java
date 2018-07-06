package sample.ParametersReader.MySQLController;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class LineLocation {
    private int id;
    private int task_id;
    private String location;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private Timestamp date_add;

    public LineLocation(ResultSet rs) {
        try {
            this.id = rs.getInt(1);
            this.task_id = rs.getInt(2);
            this.location = rs.getString(3);
            this.x1 = rs.getInt(4);
            this.y1 = rs.getInt(5);
            this.x2 = rs.getInt(6);
            this.y2 = rs.getInt(7);
            this.date_add = rs.getTimestamp(8);

        } catch (SQLException e) {
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

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public Timestamp getDate_add() {
        return date_add;
    }

    @Override
    public String toString() {
        return "LineLocation{" +
                "id=" + id +
                ", task_id=" + task_id +
                ", location='" + location + '\'' +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", date_add=" + date_add +
                '}';
    }
}
