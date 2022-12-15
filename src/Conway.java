import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static javafx.scene.paint.Color.LIGHTBLUE;

//configure to run window
//https://stackoverflow.com/questions/51478675/error-javafx-runtime-components-are-missing-and-are-required-to-run-this-appli

//to create grids
//https://stackoverflow.com/questions/36369224/creating-an-editable-paintable-grid-in-javafx

// this is the main window that deal with all the GUI stuff, like
// 1. detection of mouse hover over a cell,
// 2. detection of mouse click on the empty cell to live - GameModel will be the OBSERVER
// 3. detection of mouse click on opponent occupied cell to dead - GameModel will be the OBSERVER
// 4. paint the cell to live - GameModel will be the OBSERVER
// 5. set the cell as dead - GameModel will be the OBSERVER
// 6. show the statistics data obtained from GameModel - Conway becomes OBSERVER to GameModel
// 7. menu to ask player name, and pass the players name to the GameModel
// 8. menu to start the game, trigger the GameModel to orchestra the game - - GameModel will be the OBSERVER
// 9. menu to close the game
// 10. menu to ask players to choose color
public class Conway extends Application {
    Stage window;

    private static int noOfGrid = 10;   // initial noOfGrid x noOfGrid Rectangle
    private static int gSize = 20;  // grid (cell) size
    private static final int LPAD = 20;
    private static final int RPAD = 20;
    private static final int TPAD = 20;
    private static final int BPAD = 20;

    public static void main(String[] pArgs)
    {
        launch(pArgs);
    }

    @Override
    public void start(Stage pPrimaryStage)
    {
        window = pPrimaryStage;
        createScene();
    }

    private void createScene() {
        TextInputDialog dialog = showInputBox("Please enter grid size");
        MenuBar menuBar = createMenu(dialog);
        Rectangle[][] grids = createGrid();

        Group root = new Group();
        root.getChildren().addAll(menuBar);
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                root.getChildren().addAll(grids[i][j]);
            }
        }

        // noOfGrid*gSize gives total length of grid w/o gap, and 2nd gSize
        int windowWidth = LPAD+noOfGrid*gSize+gSize-1+RPAD;
        int windowHeight = TPAD+noOfGrid*gSize+gSize-1+BPAD+30;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.BLUE);
        window.setScene(scene);
        window.show();
    }

    private MenuBar createMenu(TextInputDialog dialog) {
        Menu configMenu = new Menu("Game");
        MenuItem item1 = new MenuItem("Set Grids Size");
        configMenu.getItems().addAll(item1);
        MenuItem item2 = new MenuItem("Get Player Names");
        configMenu.getItems().addAll(item2);
        item1.setOnAction(e -> {
            dialog.showAndWait();
            noOfGrid = Integer.valueOf(dialog.getEditor().getText());
            createScene();
        });
        MenuBar menuBar = new MenuBar(configMenu);
        menuBar.prefWidthProperty().bind(window.widthProperty());
        menuBar.setStyle("-fx-background-color: lightblue;");
        return menuBar;
    }

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
            }
        }
        return rect;
    }

    private static TextInputDialog showInputBox(String asking) {
        TextInputDialog inputDialog = new TextInputDialog(String.valueOf(noOfGrid));
        inputDialog.setHeaderText(asking);

        return inputDialog;
    }


    // method for detecting the mouse click for new cell live



}