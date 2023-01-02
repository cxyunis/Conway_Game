import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameModel implements GameBoardObserver {
    /*
     * The GameModel is the CPU of the game. It is designed to be able to either use GUI or Terminal
     * To achieve this goal, we need to implements OBSERVER so that it can notify methods defined when state changed
     * So, the TerminalBased class need to have one-to-one corresponding method/concern/responsibility as the GUI
     * For the GUI part, any state change will be passed back to GameModel to decide next course of action (flow of
     * the game.
     * */
    private int playerTurn = 1;     // shall I move this to GameSetting???
    private Cell[][] gameBoard;
    private GUIGameBoard gui;
    private boolean livePlayed = false;    //
    private boolean deadPlayed = false;
    private boolean gameOver = false;
    private int winner = 0;
    public static void main(String[] args) {
        Application.launch(GameSettingWindow.class, args);
    }
    public GameModel() {
        // sort player name in alphabetical order
        sortPlayerInAlphabeticalOrder();
    }
    public void startGame() {
        // call back from SettingWindow after players selected the preference
        if (GameSetting.instance().getPlatform().equals("GUI")) {
            Stage stage = new Stage();
            gui = new GUIGameBoard();
            gui.registerObserver(this);
            gui.start(stage);   //<e>.<6>

            initializeGameBoard();  // initialize game board
            setInitialPattern();    // fill with initial pattern to start the game with
            for (int pn=1; pn<3; pn++) {
                playerTurn = pn;
                updatePlayerGeneration(gui);
            }
            playerTurn = 1;
            updatePlayerPopulation();
            updatePlayerGeneration(gui);
            checkForGameOver();
        } else {
            TerminalGameBoard tbGame = new TerminalGameBoard();
            tbGame.start();
        }
    }
    @Override
    public void cellSelected(int row, int col, String action) {
        // check if the grid cell belong to which player <8>.<e>
        //printStatistics(row,col,action);

        if (gameOver) {
            if (winner==0) {
                showAlertMessage("Game Over","No Winner");
            } else {
                showAlertMessage("Game Over","Winner is "+GameSetting.instance().getPlayerName(winner));
            }
            return;
        }

        Ownership player = Ownership.valueOf("PLAYER"+playerTurn);

        if (action.equals("ALIVE") && !livePlayed) {
            if (isSpringToLive(player,row,col)) {
                livePlayed = true;
                updateSpringToLive(playerTurn,row,col);
                updatePlayerPopulation();
                if (livePlayed && deadPlayed) {
                    switchPlayerTurn();
                    checkForGameOver();
                }
            } else {
                String title = "Cell[r="+row+", c"+col+"] can't be sprung into LIFE!";
                showAlertMessage("Invalid Move", title);
                return;
            }
        } else if (action.equals("KILL") && !deadPlayed) {
            if (isOpponentOfPlayerKillable(player,row,col)) {
                deadPlayed = true;
                updateGameBoardCell(row,col,Ownership.NONE,CellState.DEAD);
                updatePlayerPopulation();
                if (livePlayed && deadPlayed) {
                    switchPlayerTurn();
                    checkForGameOver();
                }
            } else {
                String title = "Cell[r="+row+", c="+col+"] can't be KILL!";
                showAlertMessage("Invalid Move", title);
                return;
            }
        }

        //printStatistics(row,col,action);
    }
    private void sortPlayerInAlphabeticalOrder() {
        // to meet game requirement that the game controller to sort the player according to alphabetical order
        // once sorted, the starting player in GameSetting will change according to alphabetical order, including
        // their settings will be reassigned accordingly
        String p1name = GameSetting.instance().getPlayerName(1);

        List<String> player = new ArrayList<>(2);
        player.add(new String(GameSetting.instance().getPlayerName(1)));
        player.add(new String(GameSetting.instance().getPlayerName(2)));
        Collections.sort(player);
        if (!p1name.equals(player.get(0))) {
            GameSetting.instance().swapPlayerPlayingOrder();
        }
    }
    private void initializeGameBoard() {
        int gbSize = GameSetting.instance().getGridSize();
        gameBoard = new Cell[gbSize][gbSize];
        for (int r=0; r<gbSize; r++) {
            for (int c=0; c<gbSize; c++) {
                gameBoard[r][c] = new Cell(CellState.DEAD, Ownership.NONE);
            }
        }
    }
    private void setInitialPattern() {
        Cell[][] p1 = GameSetting.instance().getPlayerChosenInitialPattern(1);
        int size = p1.length;
        int r1 = getRandomPositionOnBoard();
        int c1 = getRandomPositionOnBoard();
        fillGameBoard(r1,c1,p1);      // local array to hold the game board status

        boolean overlap = true;
        int r2 = 0;
        int c2 = 0;
        Cell[][] p2 = new Cell[size][size];
        while(overlap) {    // ensure no overlap with another player
            r2 = getRandomPositionOnBoard();
            c2 = getRandomPositionOnBoard();
            p2 = GameSetting.instance().getPlayerChosenInitialPattern(2);
            overlap = isOverlap(r2,c2,p2);
        }
        fillGameBoard(r2,c2,p2);      // local array to hold the game board status

        gui.updateGameBoard(gameBoard);
    }
    private void fillGameBoard(int startRow, int startCol, Cell[][] pattern) {
        // startRow is the starting row, startCol is the starting column
        int size = pattern.length;
        for (int r=0; r<size; r++) {
            for (int c=0; c<size; c++) {
                if (pattern[r][c].getCellState().equals(CellState.ALIVE)) {
                    gameBoard[r + startRow][c + startCol] = pattern[r][c];
                }
            }
        }
    }
    private void updatePlayerPopulation() {
        int cntPlayer1 = countPopulation(1);
        int cntPlayer2 = countPopulation(2);
        gui.refreshPopulation(1,cntPlayer1);
        gui.refreshPopulation(2,cntPlayer2);
    }
    private void updatePlayerGeneration(GUIGameBoard gui) {
        GameSetting.instance().increasePlayerGeneration(playerTurn);
        gui.refreshPlayerGeneration(playerTurn);
    }
    private void switchPlayerTurn() {
        // used after sorting the players name in alphabetical order
        playerTurn++;
        if (playerTurn>2) { playerTurn = 1; }
        livePlayed = false;
        deadPlayed = false;
        GameSetting.instance().increasePlayerGeneration(playerTurn);
        gui.refreshPlayerGeneration(playerTurn);
        gui.refreshPlayerTurn();
        //System.out.println("Switching to player "+playerTurn);
    }
    private boolean isOverlap(int row, int col, Cell[][] p) {
        int patSize = p.length;
        //System.out.println(patSize);
        for (int i=row; i<row+patSize; i++) {
            for (int j=col; j<col+patSize; j++) {
                if (gameBoard[i][j].getCellState().equals(CellState.ALIVE)) {
                    if (p[i-row][j-col].getCellState().equals(CellState.ALIVE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    private void updateGameBoardCell(int r, int c, Ownership owner, CellState cellState) {
        gameBoard[r][c].setCellOwner(owner);
        gameBoard[r][c].setCellState(cellState);
        gui.refreshEmptyCellUpdateOnGameBoard(r,c);
    }
    private void updateSpringToLive(int playerNo, int r, int c) {
        Ownership owner = Ownership.valueOf("PLAYER"+playerNo);
        gameBoard[r][c].setCellOwner(owner);
        gameBoard[r][c].setCellState(CellState.ALIVE);
        gui.refreshSpringToLiveOnGameBoard(playerNo,r,c);
    }
    private void showAlertMessage(String strTitle, String strMsg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(strTitle);
        alert.setContentText(strMsg);
        alert.showAndWait();
    }
    private boolean isSpringToLive(Ownership player, int r, int c) {
        if (!gameBoard[r][c].getCellOwner().equals(Ownership.NONE)) { return false; }
        int[] neighbors = getNeighborCount(r,c);
        if (neighbors[player.ordinal()]==3) { return true; }
        return false;
    }
    private boolean isOpponentOfPlayerKillable(Ownership player, int r, int c) {
        // check if player opponent is killable
        Ownership xyOwner = gameBoard[r][c].getCellOwner();
        if (xyOwner.equals(Ownership.NONE) || xyOwner.equals(player)) { return false; }
        int[] neighbors = getNeighborCount(r,c);
        Ownership opponent = getOpponentOf(player);
        if (neighbors[opponent.ordinal()]<2) { return true; }
        if (neighbors[opponent.ordinal()]>3) { return true; }
        return false;
    }
    private Ownership getOpponentOf(Ownership player) {
        if (player.equals(Ownership.PLAYER1)) { return Ownership.PLAYER2; }
        return Ownership.PLAYER1;
    }
    private int countPopulation(int playerNo) {
        int gbSize = GameSetting.instance().getGridSize();
        Ownership player = Ownership.valueOf("PLAYER"+playerNo);
        int popCount = 0;
        for (int r=0; r<gbSize; r++) {
            for (int c=0; c<gbSize; c++) {
                if (gameBoard[r][c].getCellOwner().equals(player)) { popCount++; }
            }
        }
        return popCount;
    }
    private int getRandomPositionOnBoard() {
        // since the dimension of the board are rectangle, hence same module is used to get row/col
        Random rnd = new Random();
        int size = GameSetting.instance().getMaxCellSelection();
        return rnd.nextInt(GameSetting.instance().getGridSize()-size+1);
    }
    private void checkForGameOver() {
        /*
         * After finished the turn, check for next player possible move, i.e.
         * the player can spring into life and able to kill opponent
         * */
        gameOver = isPlayerUnableToMove(playerTurn);
        if (gameOver) {
            int cntPlayer1 = countPopulation(1);
            int cntPlayer2 = countPopulation(2);
            String declaredWinner = "";
            if (cntPlayer1>cntPlayer2) {
                winner = 1;
                declaredWinner = GameSetting.instance().getPlayerName(winner)+" Win!";
            } else if (cntPlayer1<cntPlayer2) {
                winner = 2;
                declaredWinner = GameSetting.instance().getPlayerName(winner)+" Win!";
            } else {
                winner = 0;
                declaredWinner = "No Winner";
            }
            showAlertMessage("Game Over",declaredWinner);
        }
    }
    private boolean isPlayerUnableToMove(int playerNo) {
        // must be able to live and kill opponent
        boolean canLive = false;
        boolean canKill = false;
        int opponent = 2;
        if (playerNo==2) { opponent = 1; }
        canLive = isPlayerLivable(playerNo);
        canKill = isKillable(opponent);
        return !(canLive && canKill);
    }
    private boolean isPlayerLivable(int playerNo) {
        // player surrounded by 3 neighbors of same player color
        Ownership player = Ownership.valueOf("PLAYER"+playerNo);
        int gbSize = GameSetting.instance().getGridSize();
        for (int r=0; r<gbSize; r++) {
            for (int c=0; c<gbSize; c++) {
                int[] neighbors = getNeighborCount(r,c);
                Ownership crc = gameBoard[r][c].getCellOwner();
                if (crc.equals(Ownership.NONE)) {
                    if (neighbors[playerNo]==3) { return true; }
                }
            }
        }
        return false;
    }
    private boolean isKillable(int playerNo) {
        // is playerNo can be killed by opponent?
        int gbSize = GameSetting.instance().getGridSize();
        Ownership player = Ownership.valueOf("PLAYER"+playerNo);
        //Ownership opponent = getOpponentOf(player);
        for (int r=0; r<gbSize; r++) {
            for (int c=0; c<gbSize; c++) {
                int[] neighbors = getNeighborCount(r,c);
                Ownership crc = gameBoard[r][c].getCellOwner();
                if (crc.equals(player)) {
                    if (neighbors[player.ordinal()]<2) { return true; }
                    if (neighbors[player.ordinal()]>3) { return true; }
                }
            }
        }
        return false;
    }
    /*
     *  Count how many PLAYER1 and PLAYER2 surround the cell at x,y
     *  @param x: the grid position x
     *  @param y: the grid position y
     *  @return: int[3] to identify 1st element is count of NONE
     *                              2nd element is count of PLAYER1
     *                              3rd element is count of PLAYER2
     *  Note: the grid at (x,y) is not counted
     *  */
    private int[] getNeighborCount(int row, int col) {
        int[] owners = {0,0,0};
        int gbSize = GameSetting.instance().getGridSize();
        int topRow = row - 1;
        if (topRow<0) { topRow = row; }
        int bottomRow = row + 1;
        if (bottomRow >= gbSize) { bottomRow = row; }
        int leftCol = col - 1;
        if (leftCol<0) { leftCol = col; }
        int rightCol = col + 1;
        if (rightCol >= gbSize) { rightCol = col; }

        for (int r=topRow; r<bottomRow+1; r++) {
            for (int c = leftCol; c < rightCol + 1; c++) {
                if (r == row && c == col) continue; // skip center cell check
                if (gameBoard[r][c].getCellOwner().equals(Ownership.NONE)) {
                    owners[0]++;
                } else if (gameBoard[r][c].getCellOwner().equals(Ownership.PLAYER1)) {
                    owners[1]++;
                } else {
                    owners[2]++;    //PLAYER2
                }
            }
        }
        //String s = String.valueOf(owners[0])+","+ String.valueOf(owners[1])+","+String.valueOf(owners[2]);
        //System.out.println(s);
        return owners;
    }
//    private void printStatistics(int r, int c, String action) {
//        System.out.println("-------------------------------------------");
//        System.out.println("Current Player No: "+playerTurn);
//        System.out.println("Current Player Name: "+GameSetting.instance().getPlayerName(playerTurn));
//        System.out.println("Current Player Generation: "+GameSetting.instance().getPlayerGeneration(playerTurn));
//        System.out.println("Current Live action: "+ livePlayed);
//        System.out.println("Current Dead action: "+ deadPlayed);
//
//        System.out.println(action+" (r="+r+", c="+c+")");
//        int[] a = getNeighborCount(r,c);
//        String s = "";
//        for (int i=0; i<3; i++) {
//            s += a[i]+", ";
//        }
//        System.out.println("neighabor: "+s.strip());
//        System.out.println("===========================================");
//    }
}