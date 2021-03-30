package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class CheckPoint extends FieldAction {
    private int number;
    public int getNumber() {
        return number;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return space.getPlayer().addLastCheckPoint(this.number);
    }
}
