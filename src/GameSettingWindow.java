import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class GameSettingWindow extends Application implements ColorPaletteObserver {
    /*
     * GUI based settings for the game
     * Obtained settings from players
     * */
    private Button[] btnSelectID = new Button[2];
    private Button[] btnSelectPlayerPattern = new Button[2];
    private Color[] trackPreColor = new Color[2];  // for each player; to synchronize button color
    @Override
    public void start(Stage stage) {
        // deciding whether to play in GUI or terminal
        decidePlatform(stage);  //<1>
    }
    private Button selectPreferColor(int playerNo) {
        // call color palette for each player to choose color
        Button btnSelectID = new Button("Choose color for player "+playerNo);
        btnSelectID.setOnAction(e -> {
            trackPreColor[playerNo-1] = (Color) btnSelectID.getTextFill();     // get color before change
            ColorPalette cp = new ColorPalette(playerNo);
            cp.registerObserver(this);      // call back method: colorChange
            Stage s = new Stage();
            try {
                cp.start(s);    // start the color palette for player to choose color
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return btnSelectID;
    }
    private Button selectInitialPattern(int playerNo) {
        int size = GameSetting.instance().getMaxCellSelection();
        Button btnSelectPattern = new Button("Choose Pattern for player "+playerNo);
        btnSelectPattern.setOnAction(e -> {
            try {
                Stage s = new Stage();
                InitialPattern initialPattern = new InitialPattern(size,playerNo);
                initialPattern.start(s);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return btnSelectPattern;
    }
    private void displayGUISetting(Stage stage) {
        stage.setTitle("Conway's Game of Life: Game Settings");

        String platform = GameSetting.instance().getPlatform();

        Label[] lblPlayerName = new Label[2];
        TextField[] txtPlayerName = new TextField[2];
        Label[] lblPlayerColorPreference = new Label[2];
        Color[] defaultPlayerColor = new Color[2];
        Label[] lblPlayerStartingPattern = new Label[2];
        String[] pname = {"Haru", "Bora"};
        for (int playerNo=1; playerNo<3; playerNo++) {
            lblPlayerName[playerNo-1] = new Label("Player "+playerNo+" Name");
            txtPlayerName[playerNo-1] = new TextField(pname[playerNo-1]);   //"Enter Player 1 Name"
            lblPlayerColorPreference[playerNo-1] = new Label("Player "+playerNo+" Preference Color/Symbol");
            defaultPlayerColor[playerNo-1] = GameSetting.instance().getPlayerColor(playerNo);
            btnSelectID[playerNo-1] = selectPreferColor(playerNo);
            btnSelectID[playerNo-1].setTextFill(defaultPlayerColor[playerNo-1]);
            lblPlayerStartingPattern[playerNo-1] = new Label("Player "+playerNo+" Starting Pattern");
            btnSelectPlayerPattern[playerNo-1] = selectInitialPattern(playerNo);
        }

        // grid dimension
        Label lblGridSize = new Label("Set Grid Size");
        TextField txtGridSize = new TextField(String.valueOf(GameSetting.instance().getGridSize()));  // fetch default value

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
            for (int pNo=1; pNo<3; pNo++) {
                GameSetting.instance().setPlayerName(pNo, txtPlayerName[pNo-1].getText());    // update to game setting record
            }

            int gbSize =  Integer.parseInt(txtGridSize.getText());
            GameSetting.instance().setGridSize(gbSize);     // update the game board dimension
            stage.close();  // close the setting window

            // pass back to GameModel to control the flow of game
            GameModel gm = new GameModel();
            gm.startGame();
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(280, 280);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(lblPlayerName[0], 0, 0);
        gridPane.add(txtPlayerName[0], 1, 0);
        gridPane.add(lblPlayerColorPreference[0],0,1);
        gridPane.add(btnSelectID[0],1,1);
        gridPane.add(lblPlayerStartingPattern[0],0,2);
        gridPane.add(btnSelectPlayerPattern[0],1,2);
        gridPane.add(lblPlayerName[1], 0, 3);
        gridPane.add(txtPlayerName[1], 1, 3);
        gridPane.add(lblPlayerColorPreference[1],0,4);
        gridPane.add(btnSelectID[1],1,4);
        gridPane.add(lblPlayerStartingPattern[1],0,5);
        gridPane.add(btnSelectPlayerPattern[1],1,5);
        gridPane.add(lblGridSize, 0,6);
        gridPane.add(txtGridSize,1,6);
        gridPane.add(btnSubmit,0,7);
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }
    private void decidePlatform(Stage stage) {
        stage.setTitle("Ask user to play game in GUI/Terminal");

        Label lblOption = new Label("Play game in:");
        RadioButton rbGUI = new RadioButton("GUI");
        RadioButton rbTerminal = new RadioButton("Terminal");
        ToggleGroup group = new ToggleGroup();
        rbGUI.setToggleGroup(group);
        rbTerminal.setToggleGroup(group);
        Button btnOK = new Button("OK");
        btnOK.setOnAction(event -> {
            RadioButton option = (RadioButton) group.getSelectedToggle();
            String platform = option.getText();
            GameSetting.instance().setPlatform(platform);
            stage.close();
            if (platform.equals("GUI")) {
                displayGUISetting(stage);   //<2>
            } else {
                TerminalGameBoard tb = new TerminalGameBoard(); // then pass to GameModel
                tb.getTerminalSetting();
            }
        });
        rbGUI.setSelected(true);
        GridPane gridPane = new GridPane();
        gridPane.setMinSize(100, 100);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(lblOption, 0, 0);
        gridPane.add(rbGUI, 1, 1);
        gridPane.add(rbTerminal, 1, 2);
        gridPane.add(btnOK,0,3);
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void colorChange(int playerNo, Color pColor) {
        // color change due to player make changes to the color at ColorPalette
        GameSetting.instance().setPlayerChosenColor(playerNo, pColor);
        if (!trackPreColor[playerNo-1].equals(pColor)) {  // only when there is a change in color
            btnSelectID[playerNo-1].setTextFill(pColor);
            if (GameSetting.instance().isPatternFilled(playerNo)) {
                btnSelectPlayerPattern[playerNo - 1].setTextFill(pColor);
            }
        }
    }
}
