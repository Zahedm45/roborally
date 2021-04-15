package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Pit extends FieldAction {

    @Override
    public boolean doAction(GameController gameController, Space space) {
        boolean playerInPit = space.board.getPlayersInPit().contains(space.getPlayer());

        if (space.getPit() != null && space.getPlayer() != null && !playerInPit) {
            gameController.board.setPlayersInPit(space.getPlayer());
            return true;
        }
        return false;
    }



}
