/*
 * 1. keep the state of the board
 * 2. method getState to provide the state of the board to GameModel
 * 3. method to receive information from Conway to start the game
 * 4. method to update the cell state
 * 5. private method to decide which player play first
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
                this.gameBoard[i][j] = new Cell();
            }
        }
    }

    public Cell[][] getBoardState() {
        return gameBoard;
    }

    public void startGame() {

    }

    private void decidePlayerTurn() {

    }

    public void updateCellState(CellState cs, int row, int col) {

    }

    public CellState getCellState(int row, int col) {
        return CellState.ALIVE;
    }

}