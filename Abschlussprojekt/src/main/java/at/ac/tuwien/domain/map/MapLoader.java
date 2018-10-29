package at.ac.tuwien.domain.map;

import static at.ac.tuwien.commons.utils.FileUtils.processFile;
import static at.ac.tuwien.commons.utils.ConversionUtils.parseDouble;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

public class MapLoader {

    private static Map world;

    private final static Pattern entryTypePattern = Pattern.compile( "(patch-of|capital-of|neighbors-of|continent) (.+)" );
    private static final Pattern patchPattern = Pattern.compile( "([a-zA-Z]+[a-zA-Z ]*) (.+)" );
    private static final Pattern capitalPattern = Pattern.compile( "([a-zA-Z]+[a-zA-Z ]*) (\\d+) (\\d+)" );
    private static final Pattern neighborsPattern = Pattern.compile( "([a-zA-Z]+[a-zA-Z ]*) : (.+)" );
    private static final Pattern continentPattern = Pattern.compile( "([a-zA-Z]+[a-zA-Z ]*) (\\d+) : (.+)" );


    // factory design pattern
    public static Map load( String path ) {

        // create new MapView
        world = new Map();

        // parse map and add entries to MapView
        parseMap( path );

        return world;
    }


    private static void  parseMap( String path ) {

        processFile( path, (BufferedReader br) -> {
            // turn buffer reader into a stream
            br.lines()
                // do operations on the stream
                .filter( MapLoader::filterEntry ) // filter operation -> filters an elemnt out of the stream
                .forEach( MapLoader::parseEntry ); // forEach operation -> for each element on the stream do someting
        });
    }

    // filters an element out of a stream
    // true = keep the element in the stream
    // false = delete the element out of the stream
    private static boolean filterEntry( String entry ) {
        // filters empty lines out of a stream
        return ! entry.isEmpty();
    }

    // DATA FORMAT: <entry-types> <entry-data>
    private static void parseEntry( String entry ) {

        // use entryType Regex Pattern on the entry String
        Matcher matcher = entryTypePattern.matcher( entry );
        matcher.find();
        String entryType = matcher.group( 1 ); // collect group 1 from regex pattern
        String entryData = matcher.group( 2 ); // collect group 2 from regex pattern

        switch( entryType ) {
            case "patch-of": parsePatch( entryData ); break;
            case "capital-of": parseCapital( entryData ); break;
            case "neighbors-of": parseNeighbours( entryData); break;
            case "continent": parseContinent( entryData ); break;
        }
    }

    // DATA FORMAT: patch-of <Terr> <x0> <y0> <x1> <y1> ... <xn> <yn>
    private static void parsePatch( String patchEntry ) {

        // extract patch data from "patchEntry" ------
        Matcher matcher = patchPattern.matcher( patchEntry );
        matcher.find();
        String patchName = matcher.group( 1 );
        String coordinatesRaw = matcher.group( 2 );

        // convert patch data ------
        double[] coordinates = parseDouble( coordinatesRaw.split( " " ));

        // add patch data to MapView ------
        world.addPatchOf(patchName, coordinates);
    }


    private static void parseCapital( String capitalEntry ) {

        // extract capital data from "capitalEntry" ------
        Matcher matcher = capitalPattern.matcher( capitalEntry );
        matcher.find();
        String terrName = matcher.group( 1 );
        String capXRaw = matcher.group( 2 );
        String capYRaw = matcher.group( 3 );

        // convert capital data ------
        double capX = parseDouble( capXRaw );
        double capY = parseDouble( capYRaw );

        // add capital data to MapView
        world.addCapitalOf(terrName, capX, capY);
    }


    private static void parseNeighbours( String neighbhorsEntry ) {

        // extract capital data from "capitalEntry" ------
        Matcher matcher = neighborsPattern.matcher( neighbhorsEntry );
        matcher.find();
        String terrName = matcher.group( 1 );
        String neighborNamesRaw = matcher.group( 2 );

        // convert neighbors data ------
        List<String> neighborNames = Arrays.asList( neighborNamesRaw.split( " - " ));

        // add neighbors data to MapView ------
        world.addNeighborOf(terrName, neighborNames);
    }


    private static void parseContinent( String continentEntry ) {

        // extract continent data from "continentEntry" ------
        Matcher matcher = continentPattern.matcher( continentEntry );
        matcher.find();
        String contName = matcher.group( 1 );
        String bonusRaw = matcher.group( 2 );
        String terrNamesRaw = matcher.group( 3 );

        // convert continent data ------
        Integer bonus = Integer.valueOf( bonusRaw );
        List<String> terrNames = Arrays.asList( terrNamesRaw.split( " - " ));

        // add continent data to MapView ------
        world.addContinentOf(contName, bonus, terrNames);
    }
}
