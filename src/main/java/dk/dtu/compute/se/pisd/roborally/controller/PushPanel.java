package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

public class PushPanel extends FieldAction {
    Heading heading;

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }

    public Heading getHeading() {
        return heading;
    }
}
