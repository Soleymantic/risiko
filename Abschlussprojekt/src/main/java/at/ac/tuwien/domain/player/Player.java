package at.ac.tuwien.domain.player;

import at.ac.tuwien.domain.map.Territory;
import javafx.scene.paint.Color;

/**
 * Created by soli on 27.02.16.
 */
public class Player {

    private PlayerType player;
    private int reinforcements;
    private Color color;
    private Territory lastOwnTerrClicked; // needed during attack phase
    private Territory dragTo; // needed during attack phase because only 1 drag allowed
    private Territory dragFrom;



    public Player(PlayerType player, Color color)
    {
        this.player = player;
        this.color = color;
    }


    public void resetRound()
    {
        lastOwnTerrClicked = null;
        dragTo = null;
        dragFrom = null;
    }


    public boolean isOwnTerritorySelected()
    {
        return lastOwnTerrClicked== null;
    }


    public Territory getDragTo() {
        return dragTo;
    }

    public void setDragTo(Territory dragTo) {
        this.dragTo = dragTo;
    }

    public Territory getDragFrom() {
        return dragFrom;
    }

    public void setDragFrom(Territory dragFrom) {
        this.dragFrom = dragFrom;
    }

    public Territory getLastTerrClicked() {
        return lastOwnTerrClicked;
    }

    public void setLastTerrClicked(Territory lastTerrClicked) {
        this.lastOwnTerrClicked = lastTerrClicked;
    }

    public PlayerType getPlayer() {
        return player;
    }

    public void setPlayer(PlayerType player) {
        this.player = player;
    }

    public int getReinforcements() {
        return reinforcements;
    }

    public void setReinforcements(int reinforcements) {
        this.reinforcements = reinforcements;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return ""+player;
    }
}
