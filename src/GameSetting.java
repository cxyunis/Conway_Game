import javafx.scene.paint.Color;

/*
    Used Design Pattern: SINGLETON to ensure one instance only
    Initial game settings obtained from players:
    1. players name
    2. color chosen by each players
    3. nxn grids to be used in the game board
 */
public class GameSetting {
    private String[] playerName = new String[2];
    private CellColor[] cellColor = new CellColor[2];
    private Color boardColor = Color.LIGHTPINK;
    private int[] generation = {0,0};
    private int gridSize = 10;  // game board dimension (e.g. default: 10x10)
    private String platform;    // either GUI/Terminal
    private static final GameSetting INSTANCE = new GameSetting();
    private GameSetting() {
        // default color for players
        cellColor[0] = CellColor.BLUE;
        cellColor[1] = CellColor.RED;
    }

    public static GameSetting instance() { return INSTANCE; }
    public void setPlayerName(int playerNo, String name) { playerName[playerNo-1] = name; }
    public void setPlayerChosenColor(int playerNo, CellColor pColor) { cellColor[playerNo-1] = pColor; }    //for GUI
    public void increasePlayerGeneration(int playerNo) { generation[playerNo-1]++; }    // switch to next player too
    public void setGridSize(int size) { gridSize = size; }
    public void setPlatform(String platform) { this.platform = platform; }

    public void swapPlayerPlayingOrder() {
        // since there are two players only, when this method is called, it will swap players order
        String p = playerName[0];
        playerName[0] = playerName[1];
        playerName[1] = p;
    }
    public void swapPlayerColor() {
        // since there are two players only, when this method is called, it will swap players color order
        CellColor p = cellColor[0];
        cellColor[0] = cellColor[1];
        cellColor[1] = p;
    }
    public String getPlayerName(int playerNo) { return playerName[playerNo-1]; }
    public Color getPlayerColor(int playerNo) { return cellColor[playerNo-1].getColor(); }
    public int getPlayerGeneration(int playerNo) { return generation[playerNo-1]; }
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
}