package imageProcessing;

import java.awt.*;
import java.util.Random;

/**
 * Created by Andrew on 11/06/17.
 */
public class SceneLineParams {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private String location = "defaultLocation";
    private String Name = "DefaultName"+((System.currentTimeMillis() ^ new Random().nextLong()) & 0xFFFF);

    public SceneLineParams(int x1, int y1, int x2, int y2){
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public SceneLineParams(SceneLineParams sceneLineParams){
        x1 = sceneLineParams.X1();
        y1 = sceneLineParams.Y1();

        x2 = sceneLineParams.X2();
        y2 = sceneLineParams.Y2();

        location = sceneLineParams.getLocation();
        Name = sceneLineParams.getName();
    }

    public SceneLineParams(Point p1, Point p2){
        this.x1 = p1.x;
        this.x2 = p2.x;
        this.y1 = p1.y;
        this.y2 = p2.y;
    }

    public int X1(){
        return x1;
    }
    public int X2(){
        return x2;
    }
    public int Y1(){
        return y1;
    }
    public int Y2(){
        return y2;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setPoints(Point[] points)
    {
        x1 = points[0].x;
        y1 = points[0].y;

        x2 = points[1].x;
        y2 = points[1].y;
    }

    public Point[] getPoints(){
        Point[] p = new Point[2];
        p[0] = new Point(x1,y1);
        p[1] = new Point(x2,y2);
        return p;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString(){
        return Name;
    }
}
