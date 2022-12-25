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
    private GUIBased gui;
    private int liveCounter = 0;
    private int deadCounter = 0;
    private boolean gameOver = false;
    private int winner = 0;

    public GameModel() {
        // sort player to decide who start first
        sortPlayerInAlphabeticalOrder();    //<4>
        GameSetting.instance().increasePlayerGeneration(1);
        GameSetting.instance().increasePlayerGeneration(2);
    }
    public void sortPlayerInAlphabeticalOrder() {
        // to meet game requirement that the game controller to sort the player according to alphabetical order
        // once sorted, the staring player in GameSetting will change according to alphabetical order
        List<String> user = new ArrayList<>(2);
        user.add(GameSetting.instance().getPlayerName(1));
        user.add(GameSetting.instance().getPlayerName(2));
        String p1name = GameSetting.instance().getPlayerName(1);
        Collections.sort(user);
        if (!p1name.equals(user.get(0))) {
            GameSetting.instance().swapPlayerPlayingOrder();
            GameSetting.instance().swapPlayerColor();
        }
    }

    public void startGame() {
        // call back from SettingWindow after players selected the preference
        System.out.println(GameSetting.instance().getPlatform());
        if (GameSetting.instance().getPlatform().equals("GUI")) {
            Stage stage = new Stage();
            gui = new GUIBased();
            gui.registerObserver(this);
            gui.start(stage);   //<e>.<6>

            initializeGameBoard();  // initialize game board

            setInitialPattern(gui); // fill with initial pattern to start the game with
            updatePlayerPopulation();
            GameSetting.instance().increasePlayerGeneration(playerTurn);
            gui.refreshPlayerGeneration(playerTurn);
        } else {
            TerminalBased tbGame = new TerminalBased();
            tbGame.start();
        }
    }
    private void switchPlayerTurn() {
        playerTurn++;
        if (playerTurn>2) { playerTurn = 1; }
        liveCounter = 0;
        deadCounter = 0;
        GameSetting.instance().increasePlayerGeneration(playerTurn);
        gui.refreshPlayerGeneration(playerTurn);
        gui.refreshPlayerTurn();
        System.out.println("Switching to player "+playerTurn);
    }
    private void initializeGameBoard() {
        int gbSize = GameSetting.instance().getGridSize();
        gameBoard = new Cell[gbSize][gbSize];
        for (int i=0; i<gbSize; i++) {
            for (int j=0; j<gbSize; j++) {
                gameBoard[i][j] = new Cell(CellState.DEAD, Ownership.NONE);
            }
        }
    }
    private void setInitialPattern(GUIBased gui) {
        Cell[][] p1 = StartGamePattern.getRandomPattern(1);
        int x1 = getRandomPositionOnBoard();
        int y1 = getRandomPositionOnBoard();
        fillGameBoard(x1,y1,p1);      // local array to hold the game board status
        gui.showInitialPattern(1,x1,y1,p1); // show on GUI
        boolean overlap = true;
        int x2 = 0;
        int y2 = 0;
        Cell[][] p2 = new Cell[3][3];
        while(overlap) {
            x2 = getRandomPositionOnBoard();
            y2 = getRandomPositionOnBoard();
            p2 = StartGamePattern.getRandomPattern(2);
            overlap = isOverlap(x2,y2,p2);
        }
        fillGameBoard(x2,y2,p2);      // local array to hold the game board status
        gui.showInitialPattern(2,x2,y2,p2); // show on GUI
    }
    private void fillGameBoard(int x, int y, Cell[][] p) {
        int patSize = p.length;
        System.out.println(patSize);
        for (int i=x; i<x+3; i++) {
            for (int j=y; j<y+3; j++) {
                gameBoard[i][j] = p[i-x][j-y];
            }
        }
    }
    private boolean isOverlap(int x, int y, Cell[][] p) {
        int patSize = p.length;
        System.out.println(patSize);
        for (int i=x; i<x+3; i++) {
            for (int j=y; j<y+3; j++) {
                if (gameBoard[i][j].getCellState().equals(CellState.ALIVE)) {
                    if (p[i-x][j-y].getCellState().equals(CellState.ALIVE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    @Override
    public void cellSelected(int x, int y, String action) {
        // check if the grid cell belong to which player <8>.<e>
        if (gameOver) {
            checkForGameOver();
//            showAlertMessage("Game Over", "The selected cell can't be sprung into LIFE!");
            return;
        }

        printStatistics(x,y,action);

        Ownership player = Ownership.valueOf("PLAYER"+playerTurn);

        if (action.equals("ALIVE") && liveCounter==0) {
            if (isSpringToLive(player,x,y)) {
                updateSpringToLive(playerTurn,x,y);
                liveCounter++;
                if (liveCounter==1 && deadCounter==1) { switchPlayerTurn(); }
                updatePlayerPopulation();
            } else {
                showAlertMessage("Invalid Move", "The selected cell can't be sprung into LIFE!");
                return;
            }
        } else if (action.equals("KILL") && deadCounter==0) {
            if (isKillable(player,x,y)) {
                updateGameBoardCell(x,y,Ownership.NONE,CellState.DEAD);
                deadCounter++;
                if (liveCounter==1 && deadCounter==1) { switchPlayerTurn(); }
                updatePlayerPopulation();
            } else {
                showAlertMessage("Invalid Move", "The selected cell can't be put to DEAD!");
                return;
            }
        }
        checkForGameOver();

        printStatistics(x,y,action);
    }
    private void updatePlayerPopulation() {
        int cntPlayer1 = countPopulation(1);
        int cntPlayer2 = countPopulation(2);
        gui.refreshPopulation(1,cntPlayer1);
        gui.refreshPopulation(2,cntPlayer2);
    }
    private void updateGameBoardCell(int x, int y, Ownership owner, CellState cellState) {
        gameBoard[x][y].setCellOwner(owner);
        gameBoard[x][y].setCellState(cellState);
        gui.refreshEmptyCellUpdateOnGameBoard(x,y);
    }
    private void updateSpringToLive(int playerNo, int x, int y) {
        Ownership owner = Ownership.valueOf("PLAYER"+playerNo);
        gameBoard[x][y].setCellOwner(owner);
        gameBoard[x][y].setCellState(CellState.ALIVE);
        gui.refreshSpringToLiveOnGameBoard(playerNo,x,y);
    }
    private void showAlertMessage(String strTitle, String strMsg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText(strTitle);
        alert.setContentText(strMsg);
        alert.showAndWait();
    }
    private boolean isSpringToLive(Ownership player, int x, int y) {
        if (!gameBoard[x][y].getCellOwner().equals(Ownership.NONE)) { return false; }
        int[] neighbors = getNeighborCount(x,y);
        if (neighbors[player.ordinal()]==3) { return true; }
        return false;
    }
    private boolean isKillable(Ownership player, int x, int y) {
        Ownership xyOwner = gameBoard[x][y].getCellOwner();
        if (xyOwner.equals(Ownership.NONE) || xyOwner.equals(player)) { return false; }
        int[] neighbors = getNeighborCount(x,y);
        Ownership opponent = getOpponentOf(player);
        System.out.println("player "+player.ordinal());
        System.out.println("opponent "+opponent.ordinal());
        if (neighbors[opponent.ordinal()]<2) { return true; }
        if (neighbors[opponent.ordinal()]>3) { return true; }
        return false;
    }
    private Ownership getOpponentOf(Ownership owner) {
        if (owner.equals(Ownership.PLAYER1)) { return Ownership.PLAYER2; }
        return Ownership.PLAYER1;
    }
    private int countPopulation(int playerNo) {
        int gbSize = GameSetting.instance().getGridSize();
        Ownership player = Ownership.valueOf("PLAYER"+playerNo);
        int popCount = 0;
        for (int i=0; i<gbSize; i++) {
            for (int j = 0; j < gbSize; j++) {
                if (gameBoard[i][j].getCellOwner().equals(player)) { popCount++; }
            }
        }
        return popCount;
    }

    private int getRandomPositionOnBoard() {
        Random rnd = new Random();
        return rnd.nextInt(GameSetting.instance().getGridSize()-2);
    }
    private void checkForGameOver() {
        boolean player1KO = isPlayerKO(1);
        boolean player2KO = isPlayerKO(2);
        gameOver = player1KO && player2KO;
        if (playerTurn==1 && player1KO) { gameOver = true; }
        if (playerTurn==2 && player2KO) { gameOver = true; }
        if (gameOver) {
            int cntPlayer1 = countPopulation(1);
            int cntPlayer2 = countPopulation(2);
            if (cntPlayer1>cntPlayer2) {
                winner = 1;
                showAlertMessage("Game Over","Winner is "+GameSetting.instance().getPlayerName(winner));
            } else if (cntPlayer1<cntPlayer2) {
                winner = 2;
                showAlertMessage("Game Over","Winner is "+GameSetting.instance().getPlayerName(winner));
            } else {
                winner = 0;
                showAlertMessage("Game Over","No Winner");
            }
        }
    }
    private boolean isPlayerKO(int playerNo) {
        boolean canLive = false;
        boolean canKill = false;
        int opponent;
        if (playerNo==1) {
            opponent = 2;
        } else {
            opponent = 1;
        }
        canLive = isPlayerLivable(playerNo);
        canKill = isKillable(opponent);
        return !(canLive && canKill);
    }
    private boolean isPlayerLivable(int playerNo) {
        Ownership player = Ownership.valueOf("PLAYER"+playerNo);
        int gbSize = GameSetting.instance().getGridSize();
        for (int i=0; i<gbSize; i++) {
            for (int j=0; j<gbSize; j++) {
                int[] neighbors = getNeighborCount(i,j);
                Ownership cij = gameBoard[i][j].getCellOwner();
                if (cij.equals(Ownership.NONE)) {
                    if (neighbors[playerNo]==3) { return true; }
                }
            }
        }
        return false;
    }
    private boolean isKillable(int playerNo) {
        int gbSize = GameSetting.instance().getGridSize();
        Ownership player = Ownership.valueOf("PLAYER"+playerNo);
        Ownership opponent = getOpponentOf(player);
        for (int i=0; i<gbSize; i++) {
            for (int j=0; j<gbSize; j++) {
                int[] neighbors = getNeighborCount(i,j);
                Ownership cij = gameBoard[i][j].getCellOwner();
                if (cij.equals(opponent)) {
                    if (neighbors[opponent.ordinal()]<2) { return true; }
                    if (neighbors[opponent.ordinal()]>3) { return true; }
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
    private int[] getNeighborCount(int x, int y) {
        int[] owners = {0,0,0};

        int gbSize = GameSetting.instance().getGridSize();
        int leftX = x - 1;
        if (leftX<0) { leftX = x; }
        int rightX = x + 1;
        if (rightX >= gbSize) { rightX = x; }
        int topY = y - 1;
        if (topY<0) { topY = y; }
        int bottomY = y + 1;
        if (bottomY >= gbSize) { bottomY = y; }


        for (int i=leftX; i<rightX+1; i++) {
            for (int j=topY; j<bottomY+1; j++) {
                if (i==x && j==y) continue; // skip center cell check
                if (gameBoard[i][j].getCellOwner().equals(Ownership.NONE)) {
                    owners[0]++;
                } else if (gameBoard[i][j].getCellOwner().equals(Ownership.PLAYER1)) {
                    owners[1]++;
                } else {
                    owners[2]++;    //PLAYER2
                }
            }
        }
        String s = String.valueOf(owners[0])+","+ String.valueOf(owners[1])+","+String.valueOf(owners[2]);
        System.out.println(s);
        return owners;
    }
    private void printStatistics(int x, int y, String action) {
        System.out.println("-------------------------------------------");
        System.out.println("Current Player: "+playerTurn);
        System.out.println("Current Player Name: "+GameSetting.instance().getPlayerName(playerTurn));
        System.out.println("Current Player Generation: "+GameSetting.instance().getPlayerGeneration(playerTurn));
        System.out.println("Current Live Counter: "+liveCounter);
        System.out.println("Current Dead Counter: "+deadCounter);

        System.out.println(action+" ("+x+", "+y+")");
        System.out.println("===========================================");
    }
}