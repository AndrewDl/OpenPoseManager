import imageProcessing.ScenePolygonParams;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

public class ScenePolygonParamsTest {
    @Test
    public void StePoints_assignPointsToAPolygon_parsListOfPointsAndAssignTheyToPolygon(){
        /**
         * required output:
         * xpoints
         * 0 2 4 6 8 10 12 14 16 18
         * ypoints
         * 2 3 4 5 6 7 8 9 10 11
         */
        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i<10;i++) {
            points.add(new Point(i*2, i+2));
        }

        ScenePolygonParams polygon = new ScenePolygonParams();
        polygon.setPoints(points);
        System.out.println("xpoints");
        for(int x : polygon.polygon.xpoints){
            System.out.print(x+" ");
        }

        System.out.println("\nypoints");
        for(int y : polygon.polygon.ypoints){
            System.out.print(y+" ");
        }

    }
}
