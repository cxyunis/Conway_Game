/*
* OBSERVER to GameModel
* */
public class GameBoard {
    private int numberOfRow;
    private int numberOfCol;
    private Cell[][] gameBoard;

    /*
     * to initialize cells state on board
     */

    public GameBoard(int numberOfRow, int numberOfCol) {
        this.numberOfRow = numberOfRow;
        this.numberOfCol = numberOfCol;
        this.gameBoard = new Cell[numberOfRow][numberOfCol];
        for (int i = 0; i<this.numberOfRow; i++) {
            for (int j=0; j<this.numberOfCol; j++) {
                this.gameBoard[i][j] = new Cell(CellState.EMPTY);
            }
        }
    }

    // method to register OBSERVER request from client

    // method to be OBSERVER to others, e.g. Player
    // need to register to Player to listen for inputs
}
