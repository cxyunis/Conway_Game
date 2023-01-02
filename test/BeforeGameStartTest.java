import javafx.scene.paint.Color;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class BeforeGameStartTest {
    /*
    * this test is to ensure that all input values of the game are tested before
    * GameModel.startGame is called.
    * */

    private void runBeforeTest() {
        // Alice should start first
        GameSetting.instance().setPlayerName(1,"Bob");
        GameSetting.instance().setPlayerName(2,"Alice");
        // Color should be sorted correctly
        GameSetting.instance().setPlayerChosenColor(1, Color.BLUE);
        GameSetting.instance().setPlayerChosenColor(2, Color.RED);
        // Initial Pattern should be sorted too
        Cell[][] cellP1 = getPlayerInitPattern(1);
        GameSetting.instance().setPlayerChosenInitialPattern(1,cellP1);

        Cell[][] cellP2 = getPlayerInitPattern(2);
        GameSetting.instance().setPlayerChosenInitialPattern(2,cellP2);

        GameModel gm = new GameModel();
    }
    private Cell[][] getPlayerInitPattern(int playerNo) {
        int row = playerNo; // player 1 occupied 1st row, player 2 occupied 2nd row
        Cell[][] cellPatt = new Cell[5][5];
        for (int r=0; r<5; r++) {
            for (int c=0; c<5; c++) {
                cellPatt[r][c] = new Cell(CellState.DEAD, Ownership.NONE);
            }
        }
        // fill/overwrite with player pattern at row = playerNo
        for (int c=0; c<5; c++) {
            cellPatt[row][c] = new Cell(CellState.ALIVE, Ownership.valueOf("PLAYER"+playerNo));
        }
        return cellPatt;
    }

    @Test
    public void startingPlayerNameAssignedCorrectly() {
        runBeforeTest();

        String p1 = GameSetting.instance().getPlayerName(1);
        assertNotSame("Alice",p1);
    }
    @Test
    public void startingPlayerColorAssignedCorrectly() {
        runBeforeTest();

        Color c1 = GameSetting.instance().getPlayerColor(1);
        assertEquals(Color.RED,c1);
    }
    @Test
    public void startingPlayerInitialPatternAssignedCorrectly() {
        Cell[][] initPatt = getPlayerInitPattern(2);    // player 2 ALIVE is expected to become 1st player
        runBeforeTest();
        Cell[][] player1InitialPattern = GameSetting.instance().getPlayerChosenInitialPattern(1);
        for (int r=0; r<5; r++) {
            for (int c=0; c<5; c++) {
                assertEquals(initPatt[r][c].getCellState(),player1InitialPattern[r][c].getCellState());
            }
        }
    }

}
