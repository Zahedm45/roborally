package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class EnergySpace extends FieldAction {
    @Override
    public boolean doAction(GameController gameController, Space space) {

        Player player = space.getPlayer();
        if (player != null && space.getEnergySpace() != null) {

//            if (player.isSurvivingMode()) {
//                player.setSurvivingMode(false);
//
//
//            } else {
//                player.setSubEnergyBank(true);
//            }

        }

        return false;
    }
}
