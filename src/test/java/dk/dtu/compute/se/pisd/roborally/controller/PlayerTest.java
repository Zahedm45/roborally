package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
}
