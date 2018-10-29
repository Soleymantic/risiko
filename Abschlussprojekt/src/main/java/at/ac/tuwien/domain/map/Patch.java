package at.ac.tuwien.domain.map;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

public class Patch {

    private double[] coordinates;
    private Territory territory;

    public Patch( double[] coordinates, Territory territory ) {
        this.coordinates = requireNonNull( coordinates );
        this.territory = requireNonNull( territory );
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public Territory getTerritory() {
        return territory;
    }

    @Override
    public String toString() {
        return "Patch{" +
            "coordinates=" + Arrays.toString( coordinates ) +
            '}';
    }
}
