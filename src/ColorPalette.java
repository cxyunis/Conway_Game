import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ColorPalette extends Application {
    /*
     * Show Color Palette for player to choose color to represent them on the game board
     * */
    private int playerNo = 0;
    private Button outsideBtnOK;
    public ColorPalette(int playerNo, Button btnOK) {
        this.playerNo = playerNo;
        this.outsideBtnOK = btnOK;
    }

    @Override
    public void start(Stage stage) throws Exception {
        final int radius = 25;

        Label lblSelectedColor = new Label("CYAN");
        lblSelectedColor.setTextFill(Color.CYAN);
        CellColor[] cellColors = CellColor.values();
        int k = 0;
        int padding = 5;
        int x = 0, y= 0;
        Group root = new Group();
        Circle[][] crc = new Circle[3][3];
        for (int i=0; i<3; i++) {
            for (int j = 0; j <3; j++) {
                x = padding+i+(2*i+1)*radius;
                y = padding+j+(2*j+1)*radius;
                crc[i][j] = new Circle(x,y,radius);
                crc[i][j].setFill(cellColors[k].getColor());
                crc[i][j].setAccessibleText(cellColors[k].name());
                crc[i][j].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    /*
                     * choose color based on mouse click
                     * */
                    @Override
                    public void handle(MouseEvent t) {
                        Circle circle = (Circle) (t.getSource());
                        Color sc = (Color) circle.getFill();
                        lblSelectedColor.setTextFill(sc);
                        lblSelectedColor.setText(circle.getAccessibleText());
                        outsideBtnOK.setTextFill(sc);
                    }
                });
                root.getChildren().addAll(crc[i][j]);
                k++;
            }
        }
        Button btnOK = new Button("OK");
        btnOK.setMinWidth(2*radius);
        btnOK.setLayoutX(padding);
        btnOK.setLayoutY(y+radius+padding);
        btnOK.setOnAction(e -> {
            String clr = lblSelectedColor.getText();
            CellColor cc = CellColor.valueOf(clr);
            GameSetting.instance().setPlayerChosenColor(playerNo, cc);
            stage.close();
        });
        root.getChildren().addAll((Node) btnOK);
        lblSelectedColor.setLayoutX(x-radius);
        lblSelectedColor.setLayoutY(y+radius+padding);
        root.getChildren().addAll(lblSelectedColor);
        x += radius+padding;
        y += radius+padding+35;
        Scene scene = new Scene(root,x,y);
        stage.setScene(scene);
        stage.show();
    }
}
