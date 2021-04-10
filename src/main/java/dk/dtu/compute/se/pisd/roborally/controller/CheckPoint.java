package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class CheckPoint extends FieldAction {
    private int number;
    public int getNumber() {
        return number;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {


        if (space.getCheckPoint() != null && space.getPlayer() != null) {
            int lastCheckPoint = space.getPlayer().getLastCheckPoint() +1;

            if (lastCheckPoint == space.getCheckPoint().getNumber()) {
                space.getPlayer().addLastCheckPoint(this.number);
            }


            int i = space.board.getCheckPointNumbers().size();
            if (space.getPlayer().getLastCheckPoint() == i) {
                gameController.setWinner(space.getPlayer());
            }

            return true;
        }
        return false;
    }
}
