package at.ac.tuwien.game;

import at.ac.tuwien.App;
import at.ac.tuwien.components.gamewindow.GameWindowController;
import at.ac.tuwien.components.map.MapController;
import at.ac.tuwien.domain.map.Map;
import at.ac.tuwien.domain.player.Player;
import at.ac.tuwien.domain.player.PlayerType;
import at.ac.tuwien.game.phase.Phase;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionListener;

import static at.ac.tuwien.commons.Props.getProp;
import static at.ac.tuwien.commons.utils.FXUtils.getData;

/**
 * Created by soli on 24.02.16.
 */
public class Game {
    //model
    private Map map;

    private GameWindowController gameWindowCtrl;
    private MapController mapCtrl;

    private Phase phase;

    private Player player;
    private Player cpu;

    private static final Color PLAYER_COLOR = Color.valueOf( getProp( "player_color" ));
    private static final Color COMPUTER_COLOR = Color.valueOf( getProp( "computer_color" ));




    public Game( GameWindowController gameWindowCtrl, MapController mapCtrl)
    {
        this.gameWindowCtrl = gameWindowCtrl;
        this.mapCtrl = mapCtrl;

          gameWindowCtrl.registerEventHandler(this::handleGameWindowEvents);
          mapCtrl.registerEventHandler(this::handleMapEvents);

        //init gamephase
        phase = new Phase(this);

        player = new Player(PlayerType.PLAYER,PLAYER_COLOR);
        cpu = new Player(PlayerType.CPU, COMPUTER_COLOR);

    }

    public Player getPlayer()
    {
        return player;
    }

    public Player getCPU()
    {
        return cpu;
    }

    public GameWindowController getGameWindowCtrl() {
        return gameWindowCtrl;
    }

    public void setGameWindowCtrl(GameWindowController gameWindowCtrl) {
        this.gameWindowCtrl = gameWindowCtrl;
    }

    public void setMap(Map map)
    {
        this.map =map;
    }

    public Map getMap()
    {
        return map;
    }

    public MapController getMapCtrl()
    {
        return mapCtrl;
    }

    private void handleMapEvents(MouseEvent mouseEvent) {
        String eventName = mouseEvent.getEventType().getName();
        switch (eventName)
        {
            case "MOUSE_CLICKED": handleMapMouseClicked(mouseEvent); break;
            case "MOUSE_ENTERED": handleMapMouseEntered(mouseEvent);break;
            case "MOUSE EXITED": handleMapMouseExited(mouseEvent);break;
        }
    }
    private void handleGameWindowEvents(ActionEvent actionEvent) {
        phase.next();
    }


    private void handleMapMouseExited(MouseEvent mouseEvent) {
    }



    private void handleMapMouseEntered(MouseEvent mouseEvent) {
        String tName = getData(mouseEvent);
        gameWindowCtrl.getLabel().setText(tName);
    }

    private void handleMapMouseClicked(MouseEvent mouseEvent) {

        phase.play( mouseEvent );


    }

    public void startGame(Stage stage){
        Parent window = gameWindowCtrl.render();
        stage.setScene(new Scene(window,1350,750));
        stage.setFullScreen(true);
        stage.show();
    }

}
