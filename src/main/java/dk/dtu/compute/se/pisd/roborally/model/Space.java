/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Space extends Subject {

    private Player player;

    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

    public final Board board;

    public final int x;
    public final int y;

    private boolean antenna = false;



    private ConveyorBelt belt = null;
    private CheckPoint checkPoint = null;
    private Gear gear = null;
    private Pit pit = null;
    private PushPanel pushPanel = null;
    private BoardLaser boardLaser = null;
    private EnergySpace energySpace = null;


    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    public List<Heading> getWalls() {
        return walls;
    }

    public List<FieldAction> getActions() {
        return actions;
    }

    public void setActions(List<FieldAction> actions) {
        this.actions = actions;

        for (FieldAction action : this.actions) {
            if (action instanceof CheckPoint && checkPoint == null) {
                checkPoint = (CheckPoint) action;
                this.board.setCheckPointNumbers( checkPoint.getNumber() );
            }

            if (action instanceof ConveyorBelt && belt == null) {
                belt = (ConveyorBelt) action;

            }

            if (action instanceof Gear && gear == null) {
                gear = (Gear) action;
            }

            if (action instanceof Pit && pit == null) {
                pit = (Pit) action;
            }

            if (action instanceof PushPanel && pushPanel == null) {
                pushPanel = (PushPanel) action;
            }

            if (action instanceof BoardLaser && boardLaser == null) {
                boardLaser = (BoardLaser) action;
            }

            if (action instanceof EnergySpace && energySpace == null) {
                energySpace = (EnergySpace) action;
            }


        }

    }

    public ConveyorBelt getConveyorBelt() {
//        ConveyorBelt belt = null;
//        for (FieldAction action : this.actions) {
//            if (action instanceof ConveyorBelt && belt == null) {
//                belt = (ConveyorBelt) action;
//            }
//        }
        return belt;
    }

    public CheckPoint getCheckPoint() {
        //CheckPoint checkPoint = null;
//        for (FieldAction action : this.actions) {
//            if (action instanceof CheckPoint && checkPoint == null) {
//                checkPoint = (CheckPoint) action;
//                this.board.setCheckPointNumbers( checkPoint.getNumber() );
//            }
//        }
        return checkPoint;
    }




    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    public Gear getGear() {
        return gear;
    }

    public Pit getPit() {
        return pit;
    }

    public PushPanel getPushPanel() {
        return pushPanel;
    }

    public BoardLaser getBoardLaser() {
        return boardLaser;
    }

    public EnergySpace getEnergySpace() {
        return energySpace;
    }

    public void setEnergySpace(EnergySpace energySpace) {
        this.energySpace = energySpace;
    }

    public boolean isAntenna() {
        return antenna;
    }

    public void setAntenna(@NotNull boolean antenna) {
        if (this.antenna == false) {
            this.antenna = antenna;
        }
    }
}
