package at.ac.tuwien.commons.utils;

public class ConversionUtils {

    public static double[] parseDouble( String[] values ) {

        double[] coordinates = new double[ values.length ];

        for ( int i = 0; i < values.length; i++ ) {
            coordinates[i] = Double.parseDouble( values[i] );
        }

        return coordinates;
    }
}
