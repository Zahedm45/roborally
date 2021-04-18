package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class CheckPoint extends FieldAction {
    private int number;
    public int getNumber() {
        return number;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {

        Player player = space.getPlayer();

        if (space.getCheckPoint() != null && player != null) {
            int lastCheckPoint = player.getLastCheckPoint() +1;

            if (lastCheckPoint == space.getCheckPoint().getNumber()) {
                if (!player.isSurvivalMode()) {
                    player.addLastCheckPoint(this.number);
                }

            }


            int i = space.board.getCheckPointNumbers().size();
            if (player.getLastCheckPoint() == i) {
                gameController.setWinner(player);
            }

            return true;
        }
        return false;
    }
}
