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

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RoboRallyMenuBar extends MenuBar {

    private AppController appController;

    private Menu controlMenu;

    //private MenuItem saveGame;

    private MenuItem newGame;

    private MenuItem loadGame;

    private MenuItem stopGame;

    private MenuItem exitApp;

   // private MenuItem downloadBoard;
    private MenuItem saveBoardPC;


    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        controlMenu = new Menu("File");
        this.getMenus().add(controlMenu);

        newGame = new MenuItem("New Game");
        newGame.setOnAction( e -> this.appController.newGame());
        controlMenu.getItems().add(newGame);

        stopGame = new MenuItem("Stop Game");
        stopGame.setOnAction( e -> this.appController.stopGame());
        controlMenu.getItems().add(stopGame);

//        saveGame = new MenuItem("Save Game");
//        saveGame.setOnAction( e -> this.appController.saveGame());
//        controlMenu.getItems().add(saveGame);

        loadGame = new MenuItem("Load Game");
        loadGame.setOnAction( e -> this.appController.loadGame());
        controlMenu.getItems().add(loadGame);


//        downloadBoard = new MenuItem("Load Board");
//        downloadBoard.setOnAction(e -> this.appController.loadBoard());
//        controlMenu.getItems().add(downloadBoard);

        saveBoardPC = new MenuItem("Save Board");
        saveBoardPC.setOnAction(e -> this.appController.saveBoardPC());
        controlMenu.getItems().add(saveBoardPC);


        exitApp = new MenuItem("Exit");
        exitApp.setOnAction( e -> this.appController.exit());
        controlMenu.getItems().add(exitApp);

        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());

        update();
    }

//    private int game() {
//        gameToDownLoad.add(1);
//        gameToDownLoad.add(2);
////        controlMenu = new Menu("Maya");
////        this.getMenus().add(controlMenu);
//        MenuItem ne = new MenuItem("SELECT");
//        ne.setText("dlældæs");
//        controlMenu.getItems().add(ne);
//
//        System.out.println("helr");
//        update();
//
//        return 0;
//    }

    private void update() {
        if (appController.isGameRunning()) {
            newGame.setVisible(false);
            stopGame.setVisible(true);
           // saveGame.setVisible(true);
            loadGame.setVisible(false);
            saveBoardPC.setVisible(true);
        } else {
            newGame.setVisible(true);
            stopGame.setVisible(false);
            //saveGame.setVisible(false);
            loadGame.setVisible(true);
            saveBoardPC.setVisible(false);
        }
    }

}
