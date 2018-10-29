package at.ac.tuwien;


import at.ac.tuwien.components.gamewindow.GameWindowController;
import at.ac.tuwien.game.Game;
import at.ac.tuwien.domain.map.Map;
import at.ac.tuwien.domain.map.MapLoader;
import at.ac.tuwien.components.map.MapController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;

import static at.ac.tuwien.commons.Props.getProp;
import static at.ac.tuwien.commons.utils.FileUtils.getResourcePath;

public class App extends Application {

    private Game game;
    /*
     * JavaFX Application Thread ------
     * INITIALIZE AND SHOW PRIMARY APPLICATION STAGE
     */
    @Override
    public void start( Stage stage ) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bitte Map auswaehlen:(world/continents/squares/africa)");
        String line = sc.next();
        String mapName = "";
        boolean valid = true;

        switch (line)
        {
            case "squares": mapName=getProp("squares"); break;
            case "world": mapName=getProp("world"); break;
            case "continents": mapName=getProp("continents"); break;
            case "africa": mapName=getProp("africa"); break;
            default: valid = false;break;
        }


        if(valid) {
            // model
            Map map = MapLoader.load(getResourcePath(mapName));

            // map component
            MapController mapCtrl = new MapController(map);

            // game window
            GameWindowController gameWindowCtrl = new GameWindowController(mapCtrl);

            // game controller
            game = new Game(gameWindowCtrl, mapCtrl);
            game.setMap(map);


            game.startGame(stage);
        }
        else
        {
            System.out.println("Wrong Map File");
            System.exit(0);
        }
    }


    /*
     * Java Main Thread ------
     * LAUNCH JAVAFX APPLICATION
     */
    public static void main( String[] args ) {
        launch( App.class, args );
    }
}