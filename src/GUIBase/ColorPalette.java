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

import java.util.ArrayList;
import java.util.List;

public class ColorPalette extends Application {
    /*
     * Show Color Palette for player to choose color to represent them on the game board
     * */
    private int playerNo = 0;
    private List<ColorPaletteObserver> aObservers = new ArrayList<>();
    public List<ColorPaletteObserver> getaObservers() {
        //This getter is only for testing
        return aObservers;
    }
    public int getPlayerNo() {
        //This getter is only for testing
        return playerNo;
    }

    public ColorPalette(int playerNo) {
        this.playerNo = playerNo;
    }
    public void registerObserver(ColorPaletteObserver pObserver) { aObservers.add(pObserver); }
    @Override
    public void start(Stage stage) throws Exception {
        final int radius = 25;

        CellColor c = CellColor.getCellColor(GameSetting.instance().getPlayerColor(playerNo));
        Label lblSelectedColor = new Label(c.name());
        lblSelectedColor.setTextFill(c.getColor());
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
                        //colorPreferenceChange(sc);  // notify observer
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
            colorPreferenceChange(cc.getColor());   // notify observer of color change
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
    private void colorPreferenceChange(Color pColor) {
        for(ColorPaletteObserver observer : aObservers) {
            observer.colorChange(playerNo, pColor);     // call observer about the change in color
        }
    }
}
