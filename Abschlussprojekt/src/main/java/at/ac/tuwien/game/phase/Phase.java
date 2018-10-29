package at.ac.tuwien.game.phase;

import at.ac.tuwien.domain.map.Territory;
import at.ac.tuwien.domain.player.Player;
import at.ac.tuwien.game.Game;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static at.ac.tuwien.commons.utils.FXUtils.getData;

public class Phase {


    private Game game;
    private PhaseType currPhase = PhaseType.ACQUISITION;

    private AttackType currAttackType;


    // constructor ------------------------------------------------------------
    public Phase( Game game ) {
        this.game = game; // backreference on game
    }


    // play -------------------------------------------------------------------
    //waits on a click
    public void play( MouseEvent mouseEvent ) {

        // switch to current phase
        if ( currPhase == PhaseType.ACQUISITION )
            playAcquisition( mouseEvent );
        else if(currPhase == PhaseType.DISTRIBUTE) {
            playDistribute(mouseEvent);
        }
        else if( currPhase == PhaseType.ATTACK){
            game.getGameWindowCtrl().getPhaseInfo().setText("PLEASE CHOOSE A TERRITORY TO ATTACK");
            playAttack(mouseEvent);

        }


    }


   //Endround Button was clicked
    public void next() {

        game.getGameWindowCtrl().getPhaseInfo().setText("");

        if(currPhase == PhaseType.ACQUISITION)
        {

        }else if(currPhase == PhaseType.CONQUER)
        {
            game.getPlayer().resetRound();      //resets roundspecific attributes

            currPhase = PhaseType.DISTRIBUTE;
            prepareDistribute();
            if (isDistributionFinished()) {
                playDistributionCPU();

            }
        }else if(currPhase == PhaseType.DISTRIBUTE)
        {
            currPhase= PhaseType.ATTACK;
            game.getGameWindowCtrl().getPhaseInfo().setText("CHOOSE YOUR TERRITORY");

        }
        else if(currPhase == PhaseType.ATTACK) {

            playAttackCPU();
            game.getGameWindowCtrl().getPhaseInfo().setText("CPU ATTACKED -> NEW REINFORCEMENTS\nPRESS END ROUND");
            currPhase = PhaseType.CONQUER;

            isGameFinished().ifPresent(player -> {
                game.getGameWindowCtrl().getLabel().setText("Congratulation " + player + " won !");
                currPhase = PhaseType.END;
            });

        }
    }





    /*
     * ACQUISITION PHASE ------------------------------------------------------
     */
    private void playAcquisition( MouseEvent mouseEvent ) {

        String terrName = getData( mouseEvent );
        Territory terr = game.getMap().getTerritories().get( terrName );

        // if player move is legit?
        if ( verifyAcqusitionMove( terr )) {

            // then execute move in model & view
            execAcquisitionMove(game.getPlayer(),terr);

            // after play computer
            playAcqusitionCPU();

            // if phase is finished?
            if ( isAcquisitionFinished() ) {
                game.getGameWindowCtrl().getPhaseInfo().setText("ACQUISITION FINISHED -> PRESS END ROUND");
                currPhase = PhaseType.CONQUER;
            }

            // move not legit
        } else {
            System.out.println( terrName + " already claimed by " + terr.getOwner().getPlayer() );
        }

    }

    private void playAcqusitionCPU() {
        // claim the first free territory
        // and execute the move
        game.getMap().getTerritories().values().stream()
                .filter( Territory::isUnlcaimed )
                .findAny()
                .ifPresent( terr ->
                        execAcquisitionMove( game.getCPU(), terr )
                );
    }

    private void execAcquisitionMove( Player player, Territory terr ) {

        // change the model
        terr.setOwner( player );
        terr.setColor( player.getColor());
        terr.setArmies(1);

        // change the view
        game.getMapCtrl().getMapView().fillTerritory( terr.getName(), player.getColor() );
        game.getMapCtrl().getMapView().setLabel(terr.getName(),String.valueOf(terr.getArmies()));
    }

    private boolean verifyAcqusitionMove( Territory terr ) {
        // if its an unclaimed territory
        //   then move is valid (return true)
        //   otherwise unvalid (return false)
        return terr.isUnlcaimed();
    }

    private boolean isAcquisitionFinished() {
        // if no unclaimed territories left
        //   then phase is finished (return true)
        //   otherwise not finished (return false)
        return not( game.getMap().getTerritories().values().stream()
                .filter( Territory::isUnlcaimed )
                .findAny()
                .isPresent() );

        /*
        return game.getMap().getTerritories().values().stream()
                .noneMatch(Territory::isUnlcaimed);
        */
    }

    /*
     * DISTRIBUTION PHASE ------------------------------------------------------
     */

    private void prepareDistribute() {
        //player / cpu reinforcements (each 3 terrs +1)
        long numPlayerTerrs = game.getMap().getTerritories().values().stream()
                .filter(t -> t.isClaimedBy(game.getPlayer()))
                .count();

        long numCPUTerrs = game.getMap().getTerritories().values().stream()
                .filter(t -> t.isClaimedBy(game.getCPU()))
                .count();

        int bonusPlayer = (int) numPlayerTerrs/3;
        int bonusCPU = (int) numCPUTerrs/3;

        bonusPlayer+=
                game.getMap().getContinents().values().stream()
                        .mapToInt(c -> c.fullyOwnedBy(game.getPlayer()))
                        .sum();

        bonusCPU+=
                game.getMap().getContinents().values().stream()
                        .mapToInt(c -> c.fullyOwnedBy(game.getCPU()))
                        .sum();

        game.getPlayer().setReinforcements(bonusPlayer);
        game.getCPU().setReinforcements(bonusCPU);

        game.getGameWindowCtrl().getPhaseInfo().setText("Player can distribute "+bonusPlayer+" reinforcements"
                +"\nCPU can distribute " +bonusCPU+" reinforcements");

    }

    private void playDistribute( MouseEvent mouseEvent ) {

        String terrName = getData( mouseEvent );
        Territory terr = game.getMap().getTerritories().get( terrName );

        // if player move is legit?
        if ( verifyDistributionMove(terr)){

            // then execute move in model & view
            execDistributionMove(game.getPlayer(),terr);

            // if phase is finished?
            if ( isDistributionFinished() ) {
                playDistributionCPU();
                game.getGameWindowCtrl().getPhaseInfo().setText("DISTRIBUTION FINISHED -> PRESS END ROUND\nTO START ATTACK");
                //    currPhase = PhaseType.ATTACK;
            }

            // move not legit
        }
        else {
            //    System.out.println( terrName + " already claimed by " + terr.getOwner().getPlayer() );
        }

    }

    private boolean verifyDistributionMove(Territory terr){
        return terr.isClaimedBy(game.getPlayer()) && game.getPlayer().getReinforcements() > 0;
    }

    private void execDistributionMove(Player player, Territory terr) {
        // change the model
        terr.setArmies(terr.getArmies()+1);
        player.setReinforcements(player.getReinforcements()-1);

        // change the view
        game.getMapCtrl().getMapView().setLabel(terr.getName(), String.valueOf(terr.getArmies()));
    }


    private void playDistributionCPU() {

        game.getMap().getTerritories().values().stream()
                .filter(territory -> territory.isClaimedBy(game.getCPU()))
                .forEach( t -> {
                    if(game.getCPU().getReinforcements() > 0 )
                    {
                        execDistributionMove(game.getCPU(),t);
                    }
                });

        if(game.getCPU().getReinforcements() > 0)
        {
            playDistributionCPU();
        }
    }
    private boolean isDistributionFinished() {
        return game.getPlayer().getReinforcements() == 0;
    }

    /*
     * ATTACK PHASE ------------------------------------------------------
     */

    private void playAttack(MouseEvent mouseEvent) {
        String terrName = getData( mouseEvent );
        Territory terr = game.getMap().getTerritories().get( terrName );
        MouseButton button = mouseEvent.getButton();

        // if player move is legit?
        if ( verifyAttackMove(terr, button)){

            // then execute move in model & view
            execAttackMove(game.getPlayer(),terr);

            isGameFinished().ifPresent(player -> {
                game.getGameWindowCtrl().getPhaseInfo().setText("CONGRATULATION "+player +" WON !");
                currPhase = PhaseType.END;
            });
        }

    }

    private boolean verifyAttackMove(Territory terr, MouseButton button) {
        //SELECT
        if(terr.isClaimedBy(game.getPlayer()) && button == MouseButton.PRIMARY){
            currAttackType = AttackType.SELECT;
            return true;
        }

        Territory selected = game.getPlayer().getLastTerrClicked();

        //DRAG
        if(selected != null
                && terr.isClaimedBy(game.getPlayer())
                && terr.getNeighbors().contains(selected)
                && selected.getArmies() > 1
                && ((game.getPlayer().getDragTo() == null )   //never dragged before
                        || (selected.equals(game.getPlayer().getDragTo()) //or reverse drag
                            && terr.equals(game.getPlayer().getDragFrom()))
                        ||(selected.equals(game.getPlayer().getDragFrom()) //or reverse drag
                            && terr.equals(game.getPlayer().getDragTo())))
                && button == MouseButton.SECONDARY )
        {
            currAttackType = AttackType.DRAG;
            return true;
        }

        //ATTACK
        if(selected != null
                && terr.isClaimedBy(game.getCPU())
                && terr.getNeighbors().contains(selected)
                && selected.getArmies() > 1
                && button == MouseButton.PRIMARY )
        {
            currAttackType = AttackType.FIGHT;
            return true;
        }

        return false;
    }

    private void execAttackMove(Player player, Territory terr) {
        switch (currAttackType)
        {
            case SELECT:execAttackSelect(player,terr); break;
            case DRAG: execAttackDrag(player,terr); break;
            case FIGHT:execAttackFight(player,terr); break;
        }

    }

    private void execAttackFight(Player player, Territory terr) {

        Random rnd = new Random();
        boolean attackerWon = false;

        int numArmiesAttack = 1;
        if(player.getLastTerrClicked().getArmies() == 3)
             numArmiesAttack = 2;
        else if (player.getLastTerrClicked().getArmies() >= 4)
        numArmiesAttack = 3;

        int numArmiesDefender = 1;
        if(terr.getArmies() >=2 )
            numArmiesDefender = 2;

        int[] dicesOfAttacker = IntStream.range(0, numArmiesAttack).map(index -> rnd.nextInt(6) + 1).sorted().toArray();
        int[] dicesOfDefender = IntStream.range(0, numArmiesDefender).map(index -> rnd.nextInt(2) + 1).sorted().toArray();

        //FIGHT ROUND 1

        int maxAttackFirst = dicesOfAttacker[dicesOfAttacker.length-1];
        int maxDefenderFirst = dicesOfDefender[dicesOfDefender.length-1];

        if( maxAttackFirst > maxDefenderFirst) {
            if (numArmiesDefender > 1 && numArmiesAttack > 1) {
                //FIGHT ROUND 2
                int maxAttackSecond = dicesOfAttacker[dicesOfAttacker.length - 2];
                int maxDefenderSecond = dicesOfDefender[dicesOfDefender.length - 2];

                if (maxAttackSecond > maxDefenderSecond) {
                    attackerWon = true;
                }
                else
                {
                    attackerWon = false;
                }

            } else {
                attackerWon = true;
            }
        }
        else
        {
            attackerWon = false;
        }

        if(attackerWon)
        {
            int armies = terr.getArmies()-numArmiesDefender;

            //change the model
            if(armies == 0) // Enemy has no armies anymore
            {
                terr.setOwner(player);
                terr.setColor(player.getColor());
                armies = 1;
                player.getLastTerrClicked().setArmies(player.getLastTerrClicked().getArmies()-1);
            }
            terr.setArmies(armies);

            //change the view
            game.getGameWindowCtrl().getSideInfo().setText("Dice of Attacker: "+Arrays.toString(dicesOfAttacker)+"\nDice of Defender: "+Arrays.toString(dicesOfDefender));
            game.getGameWindowCtrl().getEnemyInfo().setText(player.getLastTerrClicked().getName()+" won against \n"+ terr.getName());
            game.getMapCtrl().getMapView().setLabel(terr.getName(),String.valueOf(armies));
            game.getMapCtrl().getMapView().setLabel(player.getLastTerrClicked().getName(),String.valueOf(player.getLastTerrClicked().getArmies()));
        }
        else
        {

            //change the model
            int armies = player.getLastTerrClicked().getArmies()-numArmiesAttack;
            player.getLastTerrClicked().setArmies(armies);


            //change the view
            game.getGameWindowCtrl().getSideInfo().setText("Dice of Attacker: "+Arrays.toString(dicesOfAttacker)+"\nDice of Defender: "+Arrays.toString(dicesOfDefender));
            game.getGameWindowCtrl().getEnemyInfo().setText(player.getLastTerrClicked().getName()+" lost against \n"+ terr.getName());
            game.getMapCtrl().getMapView().setLabel(player.getLastTerrClicked().getName(),String.valueOf(armies));
        }
    }

    private void execAttackDrag(Player player, Territory terr) {


        int armiesSelected = player.getLastTerrClicked().getArmies()-1;
        int armiesTo = terr.getArmies()+1;

        //change the model
        player.getLastTerrClicked().setArmies(armiesSelected);
        terr.setArmies(armiesTo);
        player.setDragFrom(player.getLastTerrClicked());
        player.setDragTo(terr);


        //change the view
        game.getMapCtrl().getMapView().setLabel(player.getLastTerrClicked().getName(),String.valueOf(armiesSelected));
        game.getMapCtrl().getMapView().setLabel(terr.getName(),String.valueOf(armiesTo));
        game.getGameWindowCtrl().getLabel().setText(player.getLastTerrClicked().getName()+" dragged to "+terr.getName());
    }

    private void execAttackSelect(Player player, Territory terr) {

        game.getPlayer().setLastTerrClicked( terr);

        game.getGameWindowCtrl().getLabel().setText(terr.getName()+" is selected");

    }


    private void playAttackCPU() {

        game.getMap().getTerritories().values().stream()
                .filter(t -> t.isClaimedBy(game.getCPU()))
                .filter(t -> t.getArmies()>1)
                .filter(t -> t.getNeighbors().stream().anyMatch(terr -> terr.isClaimedBy(game.getPlayer())))
                .findFirst()
                .ifPresent(ownTerr ->{
                    ownTerr.getNeighbors().stream().filter(territory -> territory.isClaimedBy(game.getPlayer())).findAny()
                            .ifPresent(target-> {
                                game.getCPU().setLastTerrClicked(ownTerr);
                                execAttackFight(game.getCPU(),target);
                                });
                });
    }



    private Optional<Player> isGameFinished()



    {
        if(  game.getMap().getTerritories().values().stream()
                .allMatch(territory -> territory.isClaimedBy(game.getPlayer())))
        {
            return Optional.of(game.getPlayer());
        }

        if(game.getMap().getTerritories().values().stream()
                .allMatch(territory1 -> territory1.isClaimedBy(game.getCPU())))
        {
            return Optional.of(game.getCPU());
        }

        return Optional.empty();
    }


    // helper -----------------------------------------------------------------

    private boolean not( boolean expr ) {
        return ! expr;
    }


}
