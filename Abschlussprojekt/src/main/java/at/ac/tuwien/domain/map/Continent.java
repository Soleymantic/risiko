package at.ac.tuwien.domain.map;

import at.ac.tuwien.domain.player.Player;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Continent {

    private String name;
    private List<Territory> territories;
    private Integer bonus;

    public Continent( String name ) {
        this.name = name;
        territories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Continent addTerritory( Territory territory ) {
        Objects.requireNonNull( territory );
        territories.add( territory );
        return this;
    }

    public Continent addTerritory( List<Territory> territories ) {
        Objects.requireNonNull( territories );
        this.territories.addAll( territories );
        return this;
    }

    public Continent setBonus( Integer bonus ) {
        this.bonus = bonus;
        return this;
    }

    public Optional<Player> fullyOwnedBy(){
        List<Player> playersOwned = territories.stream().map(Territory::getOwner).distinct().collect(toList());

        return playersOwned.size() > 1 ? Optional.of(null): Optional.of(playersOwned.get(0));
    }

    public int fullyOwnedBy(Player player)
    {
        boolean isOwnedByPlayer = territories.stream()
                .allMatch(t -> t.isClaimedBy(player));

        return isOwnedByPlayer ? bonus: 0;
    }

    public Integer getBonus() {
        return bonus;
    }

    @Override
    public String toString() {
        return "Continent{" +
                "name='" + name + '\'' +
                ", bonus=" + bonus +
                ", territories=[" + territories.stream()
                    .map( Territory::getName ).collect( joining( ", " )) +"]"+
                '}';
    }
}
