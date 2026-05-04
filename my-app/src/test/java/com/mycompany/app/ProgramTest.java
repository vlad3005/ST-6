package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class ProgramTest {

    @BeforeAll
    public static void setup() {
        TicTacToePanel.testMode = true;
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void testPlayerClass() {
        Player p = new Player();
        p.symbol = 'X';
        p.move = 1;
        p.selected = true;
        p.win = true;
        assertEquals('X', p.symbol);
        assertEquals(1, p.move);
        assertTrue(p.selected);
        assertTrue(p.win);
    }

    @Test
    public void testGameAndUtility() {
        Game g = new Game();
        assertNotNull(g.board);
        assertEquals(State.PLAYING, g.state);

        // Test checkState XWIN
        g.symbol = 'X';
        g.board[0] = 'X';
        g.board[1] = 'X';
        g.board[2] = 'X';
        assertEquals(State.XWIN, g.checkState(g.board));

        // Test checkState OWIN
        g.symbol = 'O';
        g.board[0] = 'O';
        g.board[1] = 'O';
        g.board[2] = 'O';
        assertEquals(State.OWIN, g.checkState(g.board));

        // Test checkState DRAW
        g.board = new char[]{
            'X', 'O', 'X',
            'X', 'X', 'O',
            'O', 'X', 'O'
        };
        g.symbol = 'X';
        assertEquals(State.DRAW, g.checkState(g.board));

        // Test generateMoves
        g.board[0] = ' ';
        g.board[1] = ' ';
        ArrayList<Integer> moves = new ArrayList<>();
        g.generateMoves(g.board, moves);
        assertEquals(2, moves.size());
        assertTrue(moves.contains(0));
        assertTrue(moves.contains(1));

        // Test evaluatePosition
        Player p = new Player();
        p.symbol = 'X';
        g.board = new char[]{'X','X','X',' ',' ',' ',' ',' ',' '};
        g.symbol = 'X';
        assertEquals(Game.INF, g.evaluatePosition(g.board, p));

        p.symbol = 'O';
        assertEquals(-Game.INF, g.evaluatePosition(g.board, p));

        g.board = new char[]{'X','O','X','X','O','O','O','X','X'};
        g.symbol = 'X';
        assertEquals(0, g.evaluatePosition(g.board, p));

        // Test Utility print methods
        Utility.print(new char[]{'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '});
        Utility.print(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        ArrayList<Integer> m = new ArrayList<>();
        m.add(1); m.add(2);
        Utility.print(m);
    }

    @Test
    public void testMiniMaxAndGUI() {
        // Run test in Headless mode
        TicTacToePanel.testMode = true;
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        assertNotNull(panel.getGame());
        assertNotNull(panel.cells);

        Game g = panel.getGame();
        g.board = new char[]{'X','O','X',' ','O',' ',' ',' ',' '};
        // Let MiniMax compute move
        int move = g.MiniMax(g.board, g.player2);
        assertTrue(move >= 1 && move <= 9);

        // Test TicTacToeCell
        TicTacToeCell cell = panel.cells[0];
        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertEquals(0, cell.getNum());
        assertEquals(0, cell.getRow());
        assertEquals(0, cell.getCol());

        // Call actionPerformed
        ActionEvent ae = new ActionEvent(panel.cells[3], ActionEvent.ACTION_PERFORMED, "");
        panel.actionPerformed(ae);

        // Test more checkState patterns for complete branch coverage
        Game g2 = new Game();
        g2.symbol = 'X';
        // check horizontal, vertical, diagonal
        char[][] wins = {
            {'X','X','X',' ',' ',' ',' ',' ',' '},
            {' ',' ',' ','X','X','X',' ',' ',' '},
            {' ',' ',' ',' ',' ',' ','X','X','X'},
            {'X',' ',' ','X',' ',' ','X',' ',' '},
            {' ','X',' ',' ','X',' ',' ','X',' '},
            {' ',' ','X',' ',' ','X',' ',' ','X'},
            {'X',' ',' ',' ','X',' ',' ',' ','X'},
            {' ',' ','X',' ','X',' ','X',' ',' '}
        };
        for (char[] winBoard : wins) {
            assertEquals(State.XWIN, g2.checkState(winBoard));
        }
    }
}
