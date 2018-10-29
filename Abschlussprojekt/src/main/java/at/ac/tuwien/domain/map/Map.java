package at.ac.tuwien.domain.map;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Map {

    private java.util.Map<String, Territory> territories;
    private java.util.Map<String, Continent> continents;


    // constructor ------------------------------------------------------------

    public Map() {
        territories = new HashMap<>();
        continents = new HashMap<>();
    }


    // getter -----------------------------------------------------------------

    public java.util.Map<String, Territory> getTerritories() {
        return Collections.unmodifiableMap( territories );
    }

    public java.util.Map<String, Continent> getContinents() {
        return Collections.unmodifiableMap( continents );
    }


    // methods ----------------------------------------------------------------

    public List<double[]> getCapitalCoords()
    {
        return territories.values().stream().map(Territory::getCapCoords).collect(toList());

    }

    public List<Patch> getAllPatches() {
        return territories.values().stream()
            .flatMap( territory -> territory.getPatches().stream() )
            .collect( toList() );
    }


    // add patch
    public void addPatchOf( String patchName, double[] coordinates ) {
        Territory territory = createTerritoryIfNotExists(patchName);
        territory.addPatch(new Patch(coordinates, territory));
    }


    // add capital
    public void addCapitalOf( String terrName, double capX, double capY ) {
        createTerritoryIfNotExists( terrName )
            .setCapX( capX )
            .setCapY( capY );
    }


    // add neighbor
    public void addNeighborOf( String terrName, List<String> neighborNames ) {
        Territory terr = createTerritoryIfNotExists( terrName );
        List<Territory> neighbors = createTerritoryIfNotExist( neighborNames );
        addNeighborTo( terr, neighbors );
        addNeighborTo( neighbors, terr );
    }


    private void addNeighborTo( Territory terr, List<Territory> neighbors ) {
        terr.addNeighbor( neighbors );
    }


    private void addNeighborTo( List<Territory> territories, Territory neighbor ) {
        territories.stream()
            .forEach( territory -> territory.addNeighbor( neighbor ));
    }


    // add continent
    public void addContinentOf( String contName, Integer bonus, List<String> contNames ) {
        createContinentIfNotExist( contName )
            .setBonus( bonus )
            .addTerritory( createTerritoryIfNotExist( contNames ));
    }


    private Territory createTerritoryIfNotExists( String terrName ) {
        // if no key exists -> returns null
        Territory terr = territories.get( terrName );
        if ( terr == null ) {
            // create a territory
            terr = new Territory( terrName );
            // and add it to map
            territories.put( terrName, terr );
        }
        return terr;
    }


    private List<Territory> createTerritoryIfNotExist( List<String> terrNames ) {
        // create a stream of <String>
        return terrNames.stream()
            // the map operation converts(maps) a <String> to a <Territory>
            .map( this::createTerritoryIfNotExists )
            // collect all <Territory> to List<Territory
            .collect( toList() );
    }


    private Continent createContinentIfNotExist( String contName ) {
        Continent cont = continents.get( contName );
        if ( cont == null ) {
            cont = new Continent( contName );
            continents.put(contName, cont);
        }
        return cont;
    }


    // toString ---------------------------------------------------------------

    public String toString() {
        return Stream.concat(
            territories.values().stream(),
            continents.values().stream() )
                .map( Object::toString )
                .collect( joining( "\n" ));
    }

    public String toStringAllPatches() {
//        return territories.values().stream()
//            .flatMap( territory -> territory.getPatches().stream() )
//            .map( Object::toString )
//            .collect( joining( "\n" ));

        return getAllPatches().stream()
            .map( Patch::toString )
            .collect( joining( "\n" ));

    }
}