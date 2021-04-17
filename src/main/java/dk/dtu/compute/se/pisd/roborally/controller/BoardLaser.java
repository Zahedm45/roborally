package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class BoardLaser extends FieldAction {

    Heading heading;



    @Override
    public boolean doAction(GameController gameController, Space space) {

        Player player = space.getPlayer();

        if (player != null && space.getBoardLaser() != null) {
            player.setSubSurvivingMode(true);
            return true;
        }

//        if (player != null && space.getBoardLaser() != null) {
//
//            if (player.hasEnergyBank()) {
//                player.setEnergyBank(false);
//
//            } else {
//                player.setSubSurvivingMode(true);
//            }
//
//            return true;
//
//        }
        return false;
    }


    public Heading getHeading() {
        return heading;
    }
}
