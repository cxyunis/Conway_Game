import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/*
    Used Design Pattern: SINGLETON to ensure one instance only
    Initial game settings obtained from players:
    1. players name
    2. color chosen by each player
    3. Initial pattern to be used in the game for each player
    4. nxn grids to be used as the game board
 */
public class GameSetting {
    private final int MAX_CELLS_SELECTION = 5;
    private String[] playerName = new String[2];
    private CellColor[] playerCellColor = new CellColor[2];     // player preferred color
    private List<Cell[][]> initialPattern = new ArrayList<>(2);     //initial pattern hold by player
    private Cell[][] startPattern1 = new Cell[MAX_CELLS_SELECTION][MAX_CELLS_SELECTION];
    private Cell[][] startPattern2 = new Cell[MAX_CELLS_SELECTION][MAX_CELLS_SELECTION];
    private Color boardColor = Color.LIGHTPINK;     // color of game board (aka, the empty grid color)
    private int[] generation = {0, 0};  // generation of each player
    private int gridSize = 20;  // game board dimension (e.g. default: 10x10)
    private String platform;    // either GUI/Terminal
    private boolean[] trackPattern = {false, false};    // to synchronize between color button in preferred color and pattern color
    private static final GameSetting INSTANCE = new GameSetting();
    private GameSetting() {
        // default color for players
        playerCellColor[0] = CellColor.BLUE;
        playerCellColor[1] = CellColor.RED;
    }
    //This is for testing
    public CellColor[] getPlayerCellColor() {
        return playerCellColor;
    }
    public static GameSetting instance() { return INSTANCE; }
    public void setPlayerName(int playerNo, String name) { playerName[playerNo-1] = name; }
    public void setPlayerChosenColor(int playerNo, Color pColor) {
        playerCellColor[playerNo - 1] = CellColor.getCellColor(pColor);
    }
    public void setPlayerChosenInitialPattern(int playerNo, Cell[][] initialPattern) {
        //this.initialPattern.add(playerNo-1,initialPattern);
        trackPattern[playerNo-1] = true;
        if (playerNo==1) {
            startPattern1 = initialPattern;
        } else {
            startPattern2 = initialPattern;
        }
    }
    public void increasePlayerGeneration(int playerNo) { generation[playerNo-1]++; }    // switch to next player too
    public void setGridSize(int size) { gridSize = size; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void swapPlayerPlayingOrder() {
        // since there are two players only, when this method is called, it will swap players order
        swapPlayerName();
        swapPlayerColor();
        swapPlayerChosenInitialPattern();
    }
    private void swapPlayerName() {
        String p = new String(playerName[0]);
        playerName[0] = new String(playerName[1]);
        playerName[1] = p;
    }
    private void swapPlayerColor() {
        // since there are two players only, when this method is called, it will swap players color order
        String c1 = playerCellColor[0].name();
        String c2 = playerCellColor[1].name();
        playerCellColor[0] = CellColor.valueOf(c2);
        playerCellColor[1] = CellColor.valueOf(c1);
    }
    private void swapPlayerChosenInitialPattern() {
        Cell[][] p1 = startPattern1;
        Cell[][] p2 = startPattern2;
        List<Cell[][]> initPattern = new ArrayList<>(2);
        Cell[][] player1Cell = new Cell[p2.length][p2.length];
        for (int r=0; r<p2.length; r++) {
            for (int c = 0; c< MAX_CELLS_SELECTION; c++) {
                if (p2[r][c].getCellState().equals(CellState.DEAD)) {
                    player1Cell[r][c] = new Cell(CellState.DEAD,Ownership.NONE);
                } else {
                    player1Cell[r][c] = new Cell(CellState.ALIVE,Ownership.PLAYER1);
                }
            }
        }

        Cell[][] player2Cell = new Cell[p1.length][p1.length];
        for (int r=0; r<p1.length; r++) {
            for (int c = 0; c< MAX_CELLS_SELECTION; c++) {
                if (p1[r][c].getCellState().equals(CellState.DEAD)) {
                    player2Cell[r][c] = new Cell(CellState.DEAD,Ownership.NONE);
                } else {
                    player2Cell[r][c] = new Cell(CellState.ALIVE,Ownership.PLAYER2);
                }
            }
        }
        startPattern1 = player1Cell;
        startPattern2 = player2Cell;
    }
    public String getPlayerName(int playerNo) { return playerName[playerNo-1]; }
    public Color getPlayerColor(int playerNo) { return playerCellColor[playerNo-1].getColor(); }
    public int getPlayerGeneration(int playerNo) { return generation[playerNo-1]; }
    public Cell[][] getPlayerChosenInitialPattern(int playerNo) {
        //return initialPattern.get(playerNo-1);
        if (playerNo==1) { return startPattern1; }
        return startPattern2;
    }
    public int getPlayerTurn() {
        // related to generation
        if (generation[0]==generation[1]) {
            return 2;
        } else {
            return 1;
        }
    }
    public int getGridSize() { return gridSize; }
    public String getPlatform() { return platform; }
    public Color getBoardColor() { return boardColor; }
    public int getMaxCellSelection() { return MAX_CELLS_SELECTION; }
    public boolean isPatternFilled(int playerNo) { return trackPattern[playerNo-1]; }
}
