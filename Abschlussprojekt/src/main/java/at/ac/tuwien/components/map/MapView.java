package at.ac.tuwien.components.map;

import at.ac.tuwien.commons.utils.FXUtils;
import at.ac.tuwien.domain.map.Territory;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.ac.tuwien.commons.Props.getProp;
import static at.ac.tuwien.commons.utils.FXUtils.createPolygon;
import static java.util.stream.Collectors.toList;

/**
 * Created by soli on 24.02.16.
 */
public class MapView extends Group {

    private static final Color CONNECTION_COLOR = Color.valueOf(getProp("territory_connection_color"));
    private static final double CONNECTION_WIDTH = Double.valueOf(getProp("territory_connection_width"));
    private static final Color TERR_COLOR = Color.valueOf( getProp( "territory_color" ));
    private static final Color TERR_BRD_COLOR = Color.valueOf( getProp( "territory_border_color" ));
    private static final double TERR_BRD_WIDTH = Double.valueOf( getProp( "territory_border_width" ));
    private  EventHandler<MouseEvent> delegateHandler;

    private Map<String,List<Polygon>> territories;
    private Map<String,Label> labels;


    //constructor
    public MapView()
    {
        territories = new HashMap<>();
        labels = new HashMap<>();

    }

    protected void drawNeighborLines(double fromX, double fromY, double toX, double toY)
    {
        Line line = new Line(fromX,fromY,toX,toY);
        this.getChildren().add(line);

        line.setStroke(CONNECTION_COLOR);
        line.setStrokeWidth(CONNECTION_WIDTH);

    }



    protected void drawLabel(String tName, double X, double Y, int army)
    {
        Label label = new Label(String.valueOf(army));
        label.setLayoutX(X);
        label.setLayoutY(Y);
        getChildren().add(label);
        labels.put(tName,label);
    }


    protected void drawTerritory(String tName, List<double[]> tCoords) {

        List<Polygon> tPolys = tCoords.stream().map(coords
                -> createPolygon(coords, tName , TERR_COLOR, TERR_BRD_COLOR, TERR_BRD_WIDTH )).collect(toList());
        getChildren().addAll(tPolys);
        territories.put(tName,tPolys);

        tPolys.forEach(this::addEventHandler);

    }

    public void fillTerritory(String tName, Color color)
    {
        territories.get(tName).stream().forEach(polygon -> polygon.setFill(color));
    }

    public void setLabel(String tName, String value)
    {
        labels.get(tName).setText(value);
    }



    protected List<String> getAllTerritories()
    {
       return territories.keySet().stream().map(s -> s).collect(toList());
    }

    //handlers
    private void addEventHandler(Polygon polygon) {
        polygon.setOnMouseEntered(delegateHandler);
        polygon.setOnMouseClicked(delegateHandler);
        polygon.setOnMouseExited(delegateHandler);
    }


    //register external event handler ( max 1 register )
    public void registerEventHandler(EventHandler<MouseEvent> delegateHandler){
        this.delegateHandler = delegateHandler;
    }


    public Color getTerrColor(String terrName) {
        return (Color) territories.get(terrName).get(0).getFill();
    }
}
