package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends FieldAction {
    Heading heading;

    @Override
    public boolean doAction(GameController gameController, Space space) {

        int exCounter = space.board.getStep();



        if (space.getPushPanel() != null &&
                space.getPlayer() != null &&
                exCounter == 2 || exCounter == 4 ) {
            System.out.println(exCounter);
            Heading heading = space.getPushPanel().heading;
            Space target = space.board.getNeighbour(space, heading);

            switch (heading) {
                case WEST -> {
                    try {
                        gameController.moveToSpace(space.getPlayer(), target, heading);
                    } catch (ImpossibleMoveException e) {
                        e.printStackTrace();
                    }
                }

            }


        }

        return false;
    }

    public Heading getHeading() {
        return heading;
    }
}
