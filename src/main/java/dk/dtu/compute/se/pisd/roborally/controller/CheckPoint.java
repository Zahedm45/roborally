package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class CheckPoint extends FieldAction {
    private int number;
    public int getNumber() {
        return number;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {

        int i = space.board.getCheckPointNumbers().size();



        if (space.getCheckPoint() != null && space.getPlayer() != null) {
            space.getPlayer().addLastCheckPoint(this.number);

            if (space.getPlayer().getLastCheckPoint() == i) {
                gameController.setWinner(space.getPlayer());
            }

            return true;
        }
        return false;
    }
}
