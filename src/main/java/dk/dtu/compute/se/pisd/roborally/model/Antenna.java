package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Antenna extends FieldAction {
    public Space space;


    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}
