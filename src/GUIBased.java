import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class GUIBased extends Application {

    private static int noOfGrid = 10;   // initial noOfGrid x noOfGrid Rectangle
    private static int gSize = 20;  // grid (individual cell) = width =height
    private static final int LPAD = 20;
    private static final int RPAD = 20;
    private static final int TPAD = 20;
    private static final int BPAD = 20;

    public void start(Stage stage) {
        stage.setTitle("Conway's Game of Life");
        Scene scene = createScene();
        stage.setScene(scene);
        stage.show();
    }

    private Scene createScene() {
//        TextInputDialog dialog = showInputBox("Please enter grid size");
//        MenuBar menuBar = createMenu(dialog);
        Rectangle[][] grids = createGrid();

        Group root = new Group();
//        root.getChildren().addAll(menuBar);
        int noOfGrid = GameSetting.instance().getGridSize();    //note that this GridSize != gSize
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                root.getChildren().addAll(grids[i][j]);
            }
        }

        // noOfGrid*gSize gives total length of grid w/o gap, and 2nd gSize
        int windowWidth = LPAD+noOfGrid*gSize+gSize-1+RPAD;
        int windowHeight = TPAD+noOfGrid*gSize+gSize-1+BPAD+30;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.BLUE);
        return scene;
    }

//    private MenuBar createMenu(TextInputDialog dialog) {
//        Menu configMenu = new Menu("Game");
//        MenuItem item1 = new MenuItem("Set Grids Size");
//        configMenu.getItems().addAll(item1);
//        MenuItem item2 = new MenuItem("Get Player Names");
//        configMenu.getItems().addAll(item2);
//        item1.setOnAction(e -> {
//            dialog.showAndWait();
//            noOfGrid = Integer.valueOf(dialog.getEditor().getText());
//            createScene();
//        });
//        MenuBar menuBar = new MenuBar(configMenu);
//        menuBar.prefWidthProperty().bind(window.widthProperty());
//        menuBar.setStyle("-fx-background-color: yellow;");
//        return menuBar;
//    }

    private Rectangle[][] createGrid() {
        Rectangle[][] rect = new Rectangle[noOfGrid][noOfGrid];
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                rect[i][j] = new Rectangle();            //instantiating Rectangle
                rect[i][j].setX(i+LPAD+i*gSize);        //set X coordinate of upper left corner of rectangle
                rect[i][j].setY(j+30+TPAD+j*gSize);     //set Y coordinate of upper left corner of rectangle
                rect[i][j].setWidth(gSize);              //set the width of rectangle
                rect[i][j].setHeight(gSize);             //set the height of rectangle
                rect[i][j].setFill(Color.LIGHTPINK);
                Rectangle lRect = rect[i][j];
                rect[i][j].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        lRect.setFill(Color.RED);
                    }
                });
            }
        }
        return rect;
    }

//    private static TextInputDialog showInputBox(String asking) {
//        TextInputDialog inputDialog = new TextInputDialog(String.valueOf(noOfGrid));
//        inputDialog.setHeaderText(asking);
//
//        return inputDialog;
//    }
}