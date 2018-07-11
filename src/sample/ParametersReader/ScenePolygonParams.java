package sample.ParametersReader;

import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Created by Andrew on 11/07/17.
 */
public class ScenePolygonParams {

    public Polygon polygon = new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4);
    public String location = "DefaultLocation";
    public String name = "DefaultName"+((System.currentTimeMillis() ^ new Random().nextLong()) & 0xFFFF);

    public java.util.List<Point> getPoints() {

        int n=polygon.npoints;
        java.util.List<Point> pArray = new ArrayList<>();
        for(int i=0; i<n; i++){
            Point temp = new Point();
            temp.setLocation(polygon.xpoints[i],polygon.ypoints[i]);
            pArray.add(temp);
        }
        return pArray;
    }

    public void setPoints(java.util.List<Point> pArray) {
        polygon = new Polygon();

        int n=pArray.size();
        //TODO: extend number of point to n
        polygon.npoints=(n>4)?n=4:n;
        for(int i=0; i<n; i++){
            Point p = pArray.get(i);
            polygon.xpoints[i] = p.x;
            polygon.ypoints[i] = p.y;
        }
    }

    @Override
    public String toString(){
        return name;
    }

}
