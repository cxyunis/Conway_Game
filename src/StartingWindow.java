import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class StartingWindow extends Application {
    /*
     * GUI based settings for the game
     * Obtained settings from players
     * */
    @Override
    public void start(Stage stage) {
        // deciding whether to play in GUI or terminal
        decidePlatform(stage);  //<1>
    }

    private Button selectPreferColor(int playerNo) {
        // call color palette for each player to choose color
        Button btnSelectID = new Button("Choose color for player "+playerNo);
        btnSelectID.setOnAction(e -> {
            ColorPalette cp = new ColorPalette(playerNo,btnSelectID);
            Stage s = new Stage();
            try {
                cp.start(s);    // start the color palette for player to choose color
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        return btnSelectID;
    }
    private void getGUISetting(Stage stage) {
        stage.setTitle("Conway's Game of Life: Game Settings");

        String platform = GameSetting.instance().getPlatform();

        // player 1
        Label lblPlayer1Name = new Label("Player 1 Name");
        TextField txtPlayer1Name = new TextField("Player 1");   //"Enter Player 1 Name"
        Label lblPlayer1Preference = new Label("Player 1 Preference Color/Symbol");
        Color defaultPlayer1Color = GameSetting.instance().getPlayerColor(1);
        Button btnSelectID1 = selectPreferColor(1);
        btnSelectID1.setTextFill(defaultPlayer1Color);

        // player 2
        Label lblPlayer2Name = new Label("Player 2 Name");
        TextField txtPlayer2Name = new TextField("Player 2");   //"Enter Player 2 Name"
        Label lblPlayer2Preference = new Label("Player 2 Preference Color/Symbol");
        Color defaultPlayer2Color = GameSetting.instance().getPlayerColor(2);
        Button btnSelectID2 = selectPreferColor(2);
        btnSelectID2.setTextFill(defaultPlayer2Color);

        // grid dimension
        Label lblGridSize = new Label("Set Grid Size");
        TextField txtGridSize = new TextField(String.valueOf(GameSetting.instance().getGridSize()));  // fetch default value

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
            String p1name = txtPlayer1Name.getText();
            GameSetting.instance().setPlayerName(1, p1name);    // update to game setting record
            String p2name = txtPlayer2Name.getText();
            GameSetting.instance().setPlayerName(2, p2name);    // update to game setting record

            int gbSize =  Integer.parseInt(txtGridSize.getText());
            GameSetting.instance().setGridSize(gbSize);     // update the game board dimension
            stage.close();  // close the setting window

            // pass back to GameModel to control the flow of game
            GameModel gm = new GameModel(); //<e>.<3>
            gm.startGame();     //<e>.<5>
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(280, 280);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(lblPlayer1Name, 0, 0);
        gridPane.add(txtPlayer1Name, 1, 0);
        gridPane.add(lblPlayer1Preference,0,1);
        gridPane.add(btnSelectID1,1,1);
        gridPane.add(lblPlayer2Name, 0, 2);
        gridPane.add(txtPlayer2Name, 1, 2);
        gridPane.add(lblPlayer2Preference,0,3);
        gridPane.add(btnSelectID2,1,3);
        gridPane.add(lblGridSize, 0,4);
        gridPane.add(txtGridSize,1,4);
        gridPane.add(btnSubmit,0,5);
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
                getGUISetting(stage);   //<2>
            } else {
                TerminalBased tb = new TerminalBased(); // then pass to GameModel
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

//    private void getTerminalSetting() {
//        System.out.println("Conway's Game of Life: Terminal settings");
//        System.out.println("Get players settings");
//    }
}