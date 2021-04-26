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
package dk.dtu.compute.se.pisd.roborally.controller;


import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.*;


import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;


import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "brown", "magenta");

    final private List<String> DIFFERENT_BOARD_OPTION = Arrays.asList("standard", "10x10");

    final private RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }


    public void newGame() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Load a board");
        alert.setContentText("Do you want to load a board from PC?");
        alert.getDialogPane().getButtonTypes().remove(0);
        alert.getDialogPane().getButtonTypes().remove(0);

        alert.getDialogPane().getButtonTypes().add(ButtonType.YES);
        alert.getDialogPane().getButtonTypes().add(ButtonType.NO);

        setAlertPosition(alert);
        Optional<ButtonType> result1 = alert.showAndWait();
        if (result1.isPresent() && result1.get() == ButtonType.YES ) {
            loadBoard();
            return;
        }
        if (!result1.isPresent()) {
            return;
        }

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        setDialogPosition(dialog);
        Optional<Integer> result = dialog.showAndWait();


        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            String boardName = boardOption();

             Board board = LoadBoard.loadBoard(boardName);
            initializePlayers(result, board);

            //LoadBoard.loadBoard(null);
            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));

            gameController.startProgrammingPhase();

            roboRally.createBoardView(gameController);


        }
    }


    public String boardOption() {
        List<String> boards = new ArrayList<>();
        boards.add("defaultboard");
        boards.add("board2");



        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.setContentText("Choose a board: ");
        dialog.getItems().addAll(boards);
        dialog.setSelectedItem(boards.get(0));
        dialog.showAndWait();

        return dialog.getResult();

    }


    /**
     * Initializes players (from 1 to 6) with colors and positions.
     * @param result user selected item
     * @param board current board
     * @author Zahed(s186517)
     */

    private void initializePlayers(Optional<Integer> result, Board board) {
        gameController = new GameController(board, this);

        int no = result.get();
        for (int i = 0; i < no; i++) {
            Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
            board.addPlayer(player);
            player.setSpace(board.getSpace(i % board.width, i));
        }
    }


//    public void saveGame() {
//        // XXX needs to be implemented eventually
//        IRepository repository = RepositoryAccess.getRepository();
//        Integer currentGameID = this.gameController.board.getGameId();
//        // need to rewrite
//        if (currentGameID == null && !repository.getGames().contains(currentGameID)) {
//            repository.createGameInDB(this.gameController.board);
//        } else {
//            repository.updateGameInDB(this.gameController.board);
//        }
//    }

    /**
     * Loads user desired games from database with all board-elements and players conditions.
     * @author Zahed(s186517)
     */
    public void loadGame() {

        // XXX needs to be implememted eventually
        IRepository repository = RepositoryAccess.getRepository();

        ChoiceDialog dialog = new ChoiceDialog();
        dialog.setContentText("Choose a game:");
        setDialogPosition(dialog);
        dialog.getItems().addAll(repository.getGames());
        if (!repository.getGames().isEmpty()) {
            dialog.setSelectedItem(repository.getGames().get(0));
        }

        dialog.showAndWait();
        if (dialog.getResult() != null) {

            Integer playerChosenGID = ((GameInDB) dialog.getSelectedItem()).getId();

            if (playerChosenGID != null) {
                this.gameController =
                        new GameController(repository.loadGameFromDB(playerChosenGID), this);
                this.roboRally.createBoardView(this.gameController);
//            if (this.gameController.board.getPhase() == Phase.INITIALISATION) {
//                this.gameController.board.setPhase();
//            }
            }
        }

    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            // if there is a winner then there is no need for save the game.
            if (!gameController.board.winnerFound()) {
                gameController.saveOrUpdateGame();
            }
            gameController = null;
            roboRally.start(this.roboRally.getStage());
            //roboRally.createBoardView(null);
            return true;
        }
        return false;
    }


    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            setAlertPosition(alert);
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    /**
     * Loads a game-board (only Json file) from pc and initializes a new game.
     * It takes care of all board-elements that exist in the loaded board.
     * @author Zahed(s186517)
     */
    public void loadBoard(){

        Board loadBoard = LoadBoard.loadBoardFromPC(LoadBoard.getFileSource());
        if (loadBoard != null) {
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
            dialog.setTitle("Player number");
            dialog.setHeaderText("Select number of players");
            setDialogPosition(dialog);

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                initializePlayers(result, loadBoard);

                gameController.startProgrammingPhase();
                roboRally.createBoardView(gameController);

            }
        }
    }

    public void saveBoardToPC(){
        if (this.gameController != null) {
            LoadBoard.saveCurrentBoardToPC(this.gameController.board);
        }

    }

    protected void setGameOverDialog(Player player ) {
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle( " Game is over!!! ");
        ButtonType playAgain = new ButtonType("Play again", ButtonBar.ButtonData.OTHER);
        ButtonType exitGame = new ButtonType("Exit", ButtonBar.ButtonData.OTHER);
        ButtonType goToLobby = new ButtonType("Game lobby", ButtonBar.ButtonData.OTHER);

        confirmation.setContentText(player.getName() +" has won the game!");
        confirmation.getDialogPane().getButtonTypes().remove(0);
        confirmation.getDialogPane().getButtonTypes().remove(0);
        confirmation.getDialogPane().getButtonTypes().addAll(playAgain, exitGame, goToLobby);
        setAlertPosition(confirmation);
        Optional<ButtonType> pressedButton = confirmation.showAndWait();


        if (pressedButton.isPresent() && pressedButton.get() == playAgain) {
            this.newGame();
        } else if (pressedButton.isPresent() && pressedButton.get() == exitGame) {
            gameController = null;
            this.exit();
        } else if (pressedButton.isPresent() && pressedButton.get() == goToLobby) {
            this.stopGame();
        }

    }

    public void setDialogPosition(Dialog dialog){
        dialog.setX(roboRally.getStage().getX() + 150);
        dialog.setY(roboRally.getStage().getY() + 150);
    }

    public void setAlertPosition(Alert alert){
        alert.setX(roboRally.getStage().getX() + 150);
        alert.setY(roboRally.getStage().getY() + 150);
    }



}
