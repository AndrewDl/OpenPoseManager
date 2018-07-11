package sample.ParametersReader.MySQLController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Task {
    private int id;
    private int video_id;
    private int task_id;
    private boolean completed;
    private Timestamp date_add;

    /**
     * create Task object by rs
     * @param rs result of query
     */
    public Task(ResultSet rs) {
        try {
            this.id = rs.getInt(1);
            this.video_id = rs.getInt(2);
            this.task_id = rs.getInt(3);
            this.completed = rs.getBoolean(4);
            this.date_add = rs.getTimestamp(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public int getVideo_id() {
        return video_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Timestamp getDate_add() {
        return date_add;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", video_id=" + video_id +
                ", task_id=" + task_id +
                ", completed=" + completed +
                ", date_add=" + date_add +
                '}';
    }
}
