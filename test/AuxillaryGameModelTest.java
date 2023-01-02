import javafx.scene.paint.Color;

public class AuxillaryGameModelTest {

    public void prepareTest() {
        // setup players name
        GameSetting.instance().setPlayerName(1, "Bob");
        GameSetting.instance().setPlayerName(2, "Alice");

        // setup color
        GameSetting.instance().setPlayerChosenColor(1, Color.BLUE);
        GameSetting.instance().setPlayerChosenColor(2, Color.RED);

        // set up Initial Pattern
        Cell[][] cellP1 = getPlayerInitPattern(1);
        GameSetting.instance().setPlayerChosenInitialPattern(1, cellP1);

        Cell[][] cellP2 = getPlayerInitPattern(2);
        GameSetting.instance().setPlayerChosenInitialPattern(2, cellP2);


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
}
