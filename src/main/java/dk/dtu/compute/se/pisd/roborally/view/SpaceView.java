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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.CheckPoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.Gear;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 75; // 60; // 75;
    final public static int SPACE_WIDTH = 75;  // 60; // 75;

    public final Space space;
    //public final SpaceTemplate spaceMap;


    public SpaceView(@NotNull Space space) {
        this.space = space;
        //this.spaceMap = spaceMap;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: #aba7a7;");

        } else {
            this.setStyle("-fx-background-color: #212020;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            this.getChildren().add(arrow);
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            this.getChildren().clear();
            //addImage("image/square/s2.png", 0);
            //addImage("image/square/s5.png", 0);
            addImage("image/square/s4.png", 0);


            for (FieldAction action : this.space.getActions()) {
                if (action instanceof CheckPoint) {
                    addImage("image/checkpoint" + ((CheckPoint) action).getNumber() + ".png", -90);
                }
                if (action instanceof Gear) {
                    addImage("image/gears/gear" + ((Gear) action).getDegree() + ".png");
                }
            }
            updateBelt();
            if (!this.space.getWalls().isEmpty()) {
                updateWalls();
            }

            updatePlayer();
        }
    }


    /**
     * This method draws the walls on the spaces. The reason why if-statement has
     * been used throughout the method is because there could be more than one
     * walls on the same space.
     * @author Zahed(s186517)
     */

    private void updateWalls(){
        for (Heading wall : space.getWalls()) {
            Pane pane = new Pane();
            Rectangle rectangle =
                    new Rectangle(0.0, 0.0, SPACE_WIDTH, SPACE_HEIGHT);
            rectangle.setFill(Color.TRANSPARENT);
            pane.getChildren().add(rectangle);

            if (wall == SOUTH) {
                Line line =
                        new Line(2, SPACE_HEIGHT-2, SPACE_WIDTH-2,
                                SPACE_HEIGHT-2);
                line.setStroke(Color.RED);
                line.setStrokeWidth(5);
                pane.getChildren().add(line);
                this.getChildren().add(pane);
            }

            if (wall == WEST) {
                Line line =
                        new Line(2,  2, 2, SPACE_HEIGHT-2);
                line.setStroke(Color.RED);
                line.setStrokeWidth(5);
                pane.getChildren().add(line);
                this.getChildren().add(pane);
            }

            if (wall == NORTH) {
                Line line =
                        new Line(2, 2, SPACE_WIDTH-2,
                                2);
                line.setStroke(Color.RED);
                line.setStrokeWidth(5);
                pane.getChildren().add(line);
                this.getChildren().add(pane);
            }

            if (wall == EAST) {
                Line line = new Line(SPACE_HEIGHT-2, 2, SPACE_WIDTH-2,
                        SPACE_HEIGHT-2);
                line.setStroke(Color.RED);
                line.setStrokeWidth(5);
                pane.getChildren().add(line);
                this.getChildren().add(pane);
            }
        }
    }


    private void updateBelt(){
        ConveyorBelt belt = space.getConveyorBelt();
        if (belt != null) {

            int rotation =  belt.getHeading().ordinal();

            switch (rotation) {
                case 0 -> addImage("image/triangle/t8.png", 180);
                case 1 -> addImage("image/triangle/t8.png", 270);
                case 2 -> addImage("image/triangle/t8.png", 0);
                case 3 -> addImage("image/triangle/t8.png", 90);
            }

//            Polygon fig = new Polygon(0.0, 0.0,
//                    60.0, 0.0,
//                    30.0, 60.0);
//
//            fig.setFill(Color.LIGHTGRAY);
//
//            fig.setRotate((90*belt.getHeading().ordinal())%360);
//            this.getChildren().add(fig);
        }
    }





    private ImageView addImage(String name) {
        Image img = null;
        try {
            img = new Image(SpaceView.class.getClassLoader().getResource(name).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView imgView = new ImageView(img);
        imgView.setImage(img);
        imgView.setFitHeight(SPACE_HEIGHT);
        imgView.setFitWidth(SPACE_WIDTH);
        imgView.setVisible(true);
        this.getChildren().add(imgView);

        return imgView;
    }

    private ImageView addImage(String name, double rotation) {
        ImageView imageView = addImage(name);
        imageView.setRotate(rotation);

        return imageView;
    }

}
