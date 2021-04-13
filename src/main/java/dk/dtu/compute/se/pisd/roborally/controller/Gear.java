package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gear extends FieldAction{
    private int degree;




    @Override
    public boolean doAction(GameController gameController, Space space) {

        if (space.getGear() != null && space.getPlayer() != null) {
            Player player  = space.getPlayer();

            if (degree == 90) {
                gameController.turnRight(player);
                return true;

            } else if (degree == 270) {
                gameController.turnLeft(player);
                return true;
            }
        }

        return false;
    }

    public int getDegree() {
        return degree;
    }
}
