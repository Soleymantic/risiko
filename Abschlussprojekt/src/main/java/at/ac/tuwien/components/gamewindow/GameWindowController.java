package at.ac.tuwien.components.gamewindow;


import at.ac.tuwien.commons.FXView;
import at.ac.tuwien.components.map.MapController;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import static at.ac.tuwien.commons.FXView.load;

/**
 * Created by soli on 24.02.16.
 */
public class GameWindowController {

    //injected from gamewindowView.fxml
    @FXML
    private BorderPane gamewindow;
    @FXML
    private Label info;
    @FXML
    private Button endround;
    @FXML
    private Label sideInfo;
    @FXML
    private Label enemyInfo;
    @FXML
    private Label phaseInfo;

    //The map component
    private MapController mapCtrl;
    private EventHandler<ActionEvent> delegateHandler;


    public GameWindowController(MapController mapCtrl)
    {
        this.mapCtrl = mapCtrl;
        load("components/gamewindow/gamewindowView.fxml",this);

    }



    public Parent render()
    {
       gamewindow.setCenter(mapCtrl.render());
        return gamewindow;
    }

    public Label getPhaseInfo(){return phaseInfo;}

    public Label getSideInfo(){return sideInfo;}


    public Label getLabel()
    {
        return info;
    }

    public Button getEndround(){return endround;}

    public Label getEnemyInfo(){return enemyInfo;}


    public void handleEndroundClicked(ActionEvent actionEvent)
    {
        delegateHandler.handle( actionEvent );
    }

    public void registerEventHandler(EventHandler<ActionEvent> delegateHandler)
    {
        this.delegateHandler = delegateHandler;
    }

}
