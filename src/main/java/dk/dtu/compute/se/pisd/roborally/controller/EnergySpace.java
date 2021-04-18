package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.util.ArrayList;
import java.util.List;

public class EnergySpace extends FieldAction {
    private List<Player> players = new ArrayList<>();

    @Override
    public boolean doAction(GameController gameController, Space space) {


        Player player = space.getPlayer();
        if (player != null && space.getEnergySpace() != null) {

            if (players.contains(player)) {
                return false;
            }

            if (player.isSurvivalMode()) {
                player.setSurvivalMode(false);

            } else {
                player.setEnergyBank(true);
            }

            players.add(player);
            return true;

        }

        return false;
    }


    public List<Player> getPlayers() {
        return players;
    }
}
