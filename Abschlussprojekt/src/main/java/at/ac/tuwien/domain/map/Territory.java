package at.ac.tuwien.domain.map;

import at.ac.tuwien.domain.player.Player;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static at.ac.tuwien.commons.Props.getProp;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Territory {

    private static final Color TERR_COLOR = Color.valueOf( getProp( "territory_color" ));

    private String name;
    private double capX;
    private double capY;
    private List<Patch> patches; // also possible Set<Patch>
    private List<Territory> neighbors; // also possible Set<Patch>
    private Player owner;
    private int armies;
    private Color color = TERR_COLOR;


    // constructor ------------------------------------------------------------

    public Territory( String name ) {
        this.name = requireNonNull( name ); // defensive programming
        patches = new ArrayList<>();
        neighbors = new ArrayList<>();
        neighbors.stream().forEach(territory -> {

        });
    }




    // getter // setter -------------------------------------------------------

    public String getName() {
        return name;
    }

    public Territory setName( String name ) {
        this.name = requireNonNull( name );
        return this;
    }

    public double getCapX() {
        return capX;
    }

    public Territory setCapX( double capX ) {
        this.capX = capX;
        return this;
    }

    public double getCapY() {
        return capY;
    }

    public Territory setCapY( double capY ) {
        this.capY = capY;
        return this;
    }

    public List<Patch> getPatches() {
        return Collections.unmodifiableList(patches);
    }

    public Territory setPatches( List<Patch> patches ) {
        this.patches = requireNonNull( patches );
        return this;



    }

    public List<Territory> getNeighbors() {
        return Collections.unmodifiableList(neighbors);
    }

    public Territory setNeighbors(List<Territory> neighbors) {
        this.neighbors = requireNonNull( neighbors );
        return this;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner( Player owner ) {
        this.owner = owner;
    }

    public int getArmies() {
        return armies;
    }

    public void setArmies( int armies ) {
        this.armies = armies;
    }

    public Color getColor() {
        return color;
    }

    public Territory setColor( Color color ) {
        this.color = color;
        return this;
    }

    // methods ----------------------------------------------------------------


    public boolean isClaimed() {
        return owner != null;
    }

    public boolean isUnlcaimed() {
        return owner == null;
    }


    public Territory addPatch( Patch patch ) {
        requireNonNull( patch );
        patches.add( patch );
        return this;
    }


    public Territory addPatch( List<Patch> patch ) {
        requireNonNull( patch );
        patches.addAll( patch );
        return this;
    }


    public Territory addNeighbor( Territory neighbor ) {
        requireNonNull( neighbor );
        this.neighbors.add(neighbor);
        return this;
    }


    public Territory addNeighbor( List<Territory> neighbors ) {
        requireNonNull( neighbors );
        this.neighbors.addAll( neighbors );
        return this;
    }

    public double[] getCapCoords()
    {
        double[] capCoords = new double[2];
        capCoords[0] = capX;
        capCoords[1] = capY;
        return capCoords;
    }

    public List<double[]> getCoordinates() {
        return patches.stream()
                .map( Patch::getCoordinates )
                .collect( toList() );
    }

    public boolean isNeighborOf(Territory terr)
    {
        return  neighbors.stream().anyMatch(t -> t.equals(terr));
    }

    public boolean isClaimedBy(Player player)
    {
        return (owner.equals(player));
    }

    public boolean anyEnemyAround(Player enemy)
    {
        return neighbors.stream().anyMatch(t -> t.isClaimedBy(enemy));
    }


    // toString ---------------------------------------------------------------

    @Override
    public String toString() {
        return "Territory{" +
                "name='" + name + '\'' +
                ", capX=" + capX +
                ", capY=" + capY +
                ", patches=" + patches +
                ", neighbors=[" + neighbors.stream()
                .map( Territory::getName).collect( joining( ", " )) +"]"+
                '}';
    }
}
