package at.ac.tuwien.components.map;

import at.ac.tuwien.commons.utils.FXUtils;
import at.ac.tuwien.domain.map.Map;
import at.ac.tuwien.domain.map.Territory;
import at.ac.tuwien.domain.player.Player;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

import static at.ac.tuwien.commons.Props.getProp;
import static at.ac.tuwien.commons.utils.FXUtils.getData;

/**
 * Created by soli on 24.02.16.
 */
public class MapController {

    private MapView mapView;
    private Map map;
    private static final Color TERR_HOV_COLOR = Color.valueOf(getProp("territory_hover_color"));

    private EventHandler<MouseEvent> delegateHandler;
    private Color terrColor;

    public MapController(Map map){

        //this controller handles a MapView
        this.map = map;
        mapView = new MapView();
        mapView.registerEventHandler(this::handleMapEvents);
    }

    //returns finished drawn map
    public Parent render() {
        //1.Draw neighborlines
        map.getTerritories().values()
                .forEach(territory -> {
            double fromX = territory.getCapX();
            double fromY = territory.getCapY();
            territory.getNeighbors().forEach(n -> {
                double toX = n.getCapX();
                double toY = n.getCapY();
                mapView.drawNeighborLines(fromX,fromY,toX,toY);
            });
        });


        //2.Draw Territories

        map.getTerritories().values().forEach(t -> {
            String tName = t.getName();
            List<double[]> tCoords = t.getCoordinates();
            mapView.drawTerritory(tName, tCoords);
        });

        //3. Draw ArmyLabels
        map.getTerritories().values().forEach(territory -> {
            String tName = territory.getName();
            double capX = territory.getCapX();
            double capY = territory.getCapY();
            int army = territory.getArmies();
            mapView.drawLabel(tName,capX,capY,army);
        });

        return mapView;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public void registerEventHandler(EventHandler<MouseEvent> delegateHandler)
    {
        this.delegateHandler = delegateHandler;
    }

    private void handleMapEvents(MouseEvent mouseEvent) {
        String eventName = mouseEvent.getEventType().getName();
        switch (eventName)
        {
            case "MOUSE_ENTERED": handleMapMouseEnter(mouseEvent);break;
            case "MOUSE_EXITED": handleMapMouseExited(mouseEvent);break;
        }

        delegateHandler.handle( mouseEvent );

    }



    private void handleMapMouseExited(MouseEvent mouseEvent) {

        String tName = getData(mouseEvent);
        Color terrColor = map.getTerritories().get(tName).getColor();
        mapView.fillTerritory(tName,terrColor);

    }

    private void handleMapMouseEnter(MouseEvent mouseEvent) {

        String tName = getData(mouseEvent);
        mapView.fillTerritory(tName,TERR_HOV_COLOR);
    }


}
