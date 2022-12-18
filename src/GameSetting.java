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
    private int gridSize = 10;
    private String platform;
    private static final GameSetting INSTANCE = new GameSetting();
    private GameSetting() {}

    public static GameSetting instance() { return INSTANCE; }

    public void setPlayerName(int playerNo, String name) { playerName[playerNo-1] = name; }
    public void setPlayerChosenColor(int playerNo, CellColor pColor) { cellColor[playerNo-1] = pColor; }
    public void setGridSize(int size) { gridSize = size; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getPlayerName(int playerNo) { return playerName[playerNo-1]; }
    public Color getColor(int playerNo) { return cellColor[playerNo-1].getColor(); }
    public int getGridSize() { return gridSize; }
    public String getPlatform() { return platform; }
}
