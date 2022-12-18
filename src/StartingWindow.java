import com.sun.jdi.ArrayReference;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class StartingWindow extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Conway's Game of Life");

        Label lblPlayer1 = new Label("Player 1 Name: ");
        TextField txtPlayer1 = new TextField("Enter Player 1 Name");
        Label lblPlayer2 = new Label("Player 2 Name: ");
        TextField txtPlayer2 = new TextField("Enter Player 2 Name");

        Label lblGridSize = new Label("Set Grid Size");
        TextField txtGridSize = new TextField(String.valueOf(GameSetting.instance().getGridSize()));

        Label lblOption = new Label("Play game in:");
        RadioButton rbGUI = new RadioButton("GUI");
        RadioButton rbTerminal = new RadioButton("Terminal");
        ToggleGroup group = new ToggleGroup();
        rbGUI.setToggleGroup(group);
        rbTerminal.setToggleGroup(group);

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
            String p1name = txtPlayer1.getText();
            GameSetting.instance().setPlayerName(1, p1name);
            String p2name = txtPlayer2.getText();
            GameSetting.instance().setPlayerName(2, p2name);
            int gs =  Integer.parseInt(txtGridSize.getText());
            GameSetting.instance().setGridSize(gs);
            RadioButton option = (RadioButton) group.getSelectedToggle();
            String platform = option.getText();
            GameSetting.instance().setPlatform(platform);
            stage.close();
            GameModel gm = new GameModel();
            gm.startGame();
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(400, 400);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(lblPlayer1, 0, 0);
        gridPane.add(txtPlayer1, 1, 0);
        gridPane.add(lblPlayer2, 0, 1);
        gridPane.add(txtPlayer2, 1, 1);
        gridPane.add(lblGridSize, 0,2);
        gridPane.add(txtGridSize,1,2);
        gridPane.add(lblOption, 0, 3);
        gridPane.add(rbGUI, 1, 3);
        gridPane.add(rbTerminal, 1, 4);
        gridPane.add(btnSubmit,0,5);
        Scene scene = new Scene(gridPane);
        stage.setScene(scene);
        stage.show();
    }
}
