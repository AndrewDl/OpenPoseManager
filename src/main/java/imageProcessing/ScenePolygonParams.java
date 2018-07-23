package imageProcessing;

import java.awt.*;
import java.util.*;

/**
 * Created by Andrew on 11/07/17.
 */
public class ScenePolygonParams {


    public Polygon polygon;
    public String location = "DefaultLocation";
    public String name = "DefaultName"+((System.currentTimeMillis() ^ new Random().nextLong()) & 0xFFFF);

    /*public ScenePolygon constructFrom(){
        return new ScenePolygon(this);
    }*/

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
        polygon = new Polygon(new int[pArray.size()], new int[pArray.size()], pArray.size());

        int n=pArray.size();
        polygon.npoints=n;
        for(int i=0; i<n; i++){
            Point p = pArray.get(i);
            polygon.xpoints[i] = p.x;
            polygon.ypoints[i] = p.y;
        }
    }

    public void setNewPoint(int x, int y){
        polygon.addPoint(x, y);
    }

    public int getNumberOfPoints(){
        return polygon.npoints;
    }

    @Override
    public String toString(){
        return name;
    }

//    public Polygon polygon = new Polygon(new int[]{0,0,0,0},new int[]{0,0,0,0},4);
//    public String location = "DefaultLocation";
//    public String name = "DefaultName"+((System.currentTimeMillis() ^ new Random().nextLong()) & 0xFFFF);
//
//    public java.util.List<Point> getPoints() {
//
//        int n=polygon.npoints;
//        java.util.List<Point> pArray = new ArrayList<>();
//        for(int i=0; i<n; i++){
//            Point temp = new Point();
//            temp.setLocation(polygon.xpoints[i],polygon.ypoints[i]);
//            pArray.add(temp);
//        }
//        return pArray;
//    }
//
//    public void setPoints(java.util.List<Point> pArray) {
//        polygon = new Polygon();
//
//        int[] x = new int[pArray.size()];
//        int[] y = new int[pArray.size()];
//
//        int n=pArray.size();
//        for(int i=0; i<n; i++){
//            Point p = pArray.get(i);
//            x[i] = p.x;
//            y[i] = p.y;
//        }
//        polygon = new Polygon(x,y,n);
//    }
//
//    @Override
//    public String toString(){
//        return name;
//    }

}
