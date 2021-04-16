package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class BoardLaser extends FieldAction {

    Heading heading;
    @Override
    public boolean doAction(GameController gameController, Space space) {

        Player player = space.getPlayer();

        if (player != null && !player.hasEnergyBank()) {
            player.setSurvivingMode(true);
            return true;
        }
        return false;
    }


    public Heading getHeading() {
        return heading;
    }
}
