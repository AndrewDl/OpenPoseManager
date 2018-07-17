package sample.ParametersReader.MySQLController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoParameters {
    private int id;
    private String name;
    private String file_path;
    private String screen_path;
    private int screen_width;
    private int screen_height;
    private Timestamp video_date;
    private Timestamp date_add;

    /**
     * create VideoParameters object by rs
     * @param rs result of query
     */
    public VideoParameters(ResultSet rs){
        try {
            this.id = rs.getInt(1);
            this.name = rs.getString(2);
            this.file_path = rs.getString(3);
            this.screen_path = rs.getString(4);
            this.screen_width = rs.getInt(5);
            this.screen_height = rs.getInt(6);
            this.video_date = rs.getTimestamp(7);
            this.date_add = rs.getTimestamp(8);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getScreen_path() {
        return screen_path;
    }

    public int getScreen_width() {
        return screen_width;
    }

    public int getScreen_height() {
        return screen_height;
    }

    public Timestamp getVideo_date() {
        return video_date;

    }

    public String getVideoDateInFormat(String format) {

        Date date = new Date(video_date.getTime());
        DateFormat formatter = new SimpleDateFormat(format);
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }


    public Timestamp getDate_add() {
        return date_add;
    }

    @Override
    public String toString() {
        return "VideoParameters{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", file_path='" + file_path + '\'' +
                ", screen_path='" + screen_path + '\'' +
                ", screen_width=" + screen_width +
                ", screen_height=" + screen_height +
                ", video_date=" + video_date +
                ", date_add=" + date_add +
                '}';
    }
}
