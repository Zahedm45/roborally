package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Pit extends FieldAction {

    @Override
    public boolean doAction(GameController gameController, Space space) {

        if (space.getPit() != null && space.getPlayer() != null) {
            gameController.board.setPlayersInPit(space.getPlayer());

        }
        return false;
    }



}
