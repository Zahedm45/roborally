package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void player_checkPoint_inOrder() {

        Board board = new Board(8,8);
        Player player = new Player(board, "red", "first_player");

        player.addLastCheckPoint(1);

        assertEquals(1, player.getLastCheckPoint());

        player.addLastCheckPoint(3);

        assertEquals(1, player.getLastCheckPoint());
        assertNotEquals(3, player.getLastCheckPoint());

    }

    @Test
    public void conveyorBelt() {

//        RoboRally roboRally = new RoboRally();
//        Board board = new Board(8,8);
//        GameController gm = new GameController(board, new AppController(roboRally));
//        Player player = new Player(board,"yellow", "player2");
//
//        player.setSpace(board.getSpace(3, 3));
//        //board.getSpace(3,4).getConveyorBelt();
//
//        assertEquals(player.getSpace().getConveyorBelt(),  board.getSpace(3, 3).getConveyorBelt());
//      //  assertSame(board.getSpace(3, 4).getActions(), new ConveyorBelt() );
//        gm.executePrograms();
//       assertEquals(player.getSpace(),  board.getSpace(3, 3));
//
//       // assertNotEquals(player.getSpace(),  board.getSpace(3, 4));



    }
}
