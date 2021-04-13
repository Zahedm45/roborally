package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Space;

public class Gear extends FieldAction{
    private int degree;




    @Override
    public boolean doAction(GameController gameController, Space space) {



        return false;
    }

    public int getDegree() {
        return degree;
    }
}
