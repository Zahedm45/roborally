package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.List;

public class BoardLaser extends FieldAction {

    Heading heading;

    private List<Player> players = new ArrayList<>();


    @Override
    public boolean doAction(GameController gameController, Space space) {


        Player player = space.getPlayer();

        if (player != null && space.getBoardLaser() != null) {
            if (players.contains(player)) {
                return false;
            }

            if ( player.hasEnergyBank() ) {
                player.setEnergyBank(false);

            } else player.setSurvivalMode(true);
            players.add(player);
            return true;
        }

        return false;
    }


    public Heading getHeading() {
        return heading;
    }

    public List<Player> getPlayers() {
        return players;
    }


}
