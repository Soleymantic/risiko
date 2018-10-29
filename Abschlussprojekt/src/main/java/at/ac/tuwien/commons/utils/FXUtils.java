package at.ac.tuwien.commons.utils;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class FXUtils {

    public static Polygon createPolygon( double[] coors, String name,
           Color fillColor, Color strokeColor, double strokeWidth ) {
        Polygon poly = new Polygon( coors );
        poly.setFill( fillColor );
        poly.setStroke( strokeColor );
        poly.setStrokeWidth( strokeWidth );
        poly.setUserData( name );
        return poly;
    }



    public static Line createLine( double fromX, double fromY, double toX, double toY,
           Color strokeColor, double strokeWidth ) {
        Line line = new Line();
        line.setStartX( fromX );
        line.setStartY( fromY );
        line.setEndX( toX );
        line.setEndY( toY );
        line.setStroke( strokeColor );
        line.setStrokeWidth( strokeWidth );
        return line;
    }

    public static String getData(MouseEvent event)
    {
        Node node = (Node) event.getTarget();
        return String.valueOf(node.getUserData());
    }
}
