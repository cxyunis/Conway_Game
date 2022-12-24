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
    int playerTurn = 1;
    int[] generation = {1,1};
    private Cell[][] gameBoard;

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
            GUIBased gui = new GUIBased();
            gui.registerObserver(this);
            gui.start(stage);   //<e>.<6>

            initializeGameBoard();  // initialize game board

            setInitialPattern(gui); // fill with initial pattern to start the game with

        } else {
            TerminalBased tbGame = new TerminalBased();
            tbGame.start();
        }
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
        gui.fillInitialPattern(1,x1,y1,p1); // show on GUI
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
        gui.fillInitialPattern(2,x2,y2,p2); // show on GUI
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
    public void cellSelected(int x, int y) {
        // check if the grid cell belong to which player <8>.<e>
        System.out.println(x+","+y);
        // decide which player
        // decide empty - > live
        // decide live - > dead
        // respond back caller what to do to the cell
    }
    private boolean isValidMove(int playerNo, int x, int y) {
        Ownership owner = gameBoard[x][y].getCellOwner();
        CellState state = gameBoard[x][y].getCellState();
        if (owner.ordinal()==playerNo) { return false; }
        return true;
    }

    private int getRandomPositionOnBoard() {
        Random rnd = new Random();
        return rnd.nextInt(GameSetting.instance().getGridSize()-2);
    }

    /*
    *  Count how many self (owner) neighbors and opponent neighbors
    *  @param checkGrids is 3x3 grids, and center cell is to be checked for own/opponent neighbors
    *  @param owner: note center gird can be NONE, count is still valid to see if DEAD cell spring into life
    *  @return: ArrayList<int> contains 2 elements, 1st element is owner's neighbors, 2nd element is opponent's neighbors
    *  */
    private ArrayList<Integer> getNeighborCount(Cell[][] checkGrids, Ownership owner) {
        ArrayList<Integer> neighbours = new ArrayList<Integer>(2);
        int ownerNeighbor = 0;
        int opponentNeighbor = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (i==1 && j==1) continue; // skip center cell check
                if (checkGrids[i][j].getCellOwner()==Ownership.NONE) continue;  // skip
                if (checkGrids[i][j].getCellState()==CellState.DEAD) continue;  // skip
                if (checkGrids[i][j].getCellOwner()==owner) {
                    ownerNeighbor++;
                } else {
                    opponentNeighbor++;
                }
            }
        }
        neighbours.add(0, ownerNeighbor);
        neighbours.add(1,opponentNeighbor);
        return neighbours;
    }


}