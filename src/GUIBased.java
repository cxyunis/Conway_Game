import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;


public class GUIBased extends Application {

    Rectangle[][] rect;
    private static int gSize = 20;  // grid (individual cell) = width =height
    private static final int LPAD = 20;
    private static final int RPAD = 20;
    private static final int TPAD = 100;
    private static final int BPAD = 20;
    private List<GameBoardObserver> aObservers = new ArrayList<>();

    public void start(Stage stage) {
        stage.setTitle("Conway's Game of Life");
        Scene scene = createScene();
        stage.setScene(scene);
        stage.show();
    }
    public void registerObserver(GameBoardObserver pObserver) {
        aObservers.add(pObserver);
    }
    public void removeObserver(GameBoardObserver pObserver) {
        aObservers.remove(pObserver);
    }
    void cellSelected(int x, int y) {
        for(GameBoardObserver observer : aObservers) {
            observer.cellSelected(x,y);
        }
    }
    public void fillInitialPattern(int playerNo, int startPosX, int startPosY, Cell[][] cells) {
        // call from outsider to set initial pattern on board
        Color c = GameSetting.instance().getPlayerColor(playerNo);
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (cells[i][j].getCellState().equals(CellState.ALIVE)) {
                    rect[startPosX + i][startPosY + j].setFill(c);
                }
            }
        }
    }
    private Scene createScene() {
        Group root = new Group();

        Rectangle[][] grids = createGrid();
        int noOfGrid = GameSetting.instance().getGridSize();    //note that this GridSize != gSize
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                root.getChildren().addAll(grids[i][j]);
            }
        }

        Label[] lblPName = new Label[2];
        Circle[] crcPColor = new Circle[2];
        Label[] lblGeneration = new Label[2];
        String[] generation = new String[2];
        for (int i=0; i<2; i++) {
            lblPName[i] = new Label(GameSetting.instance().getPlayerName(i+1));
            lblPName[i].setLayoutX(0);
            lblPName[i].setLayoutY(i*40);
            lblPName[i].setPadding(new Insets(10,10,10,10));
            crcPColor[i] = new Circle(80,i*40+20,10,GameSetting.instance().getPlayerColor(i+1));
            generation[i] = "Generation "+String.valueOf(GameSetting.instance().getPlayerGeneration(i+1));
            lblGeneration[i] = new Label(generation[i]);
            lblGeneration[i].setLayoutX(110);
            lblGeneration[i].setLayoutY(i*40);
            lblGeneration[i].setPadding(new Insets(10,10,10,10));
            root.getChildren().addAll(lblPName[i],crcPColor[i],lblGeneration[i]);
        }


        // noOfGrid*gSize gives total length of grid w/o gap, and 2nd gSize
        int windowWidth = LPAD+noOfGrid*gSize+gSize-1+RPAD;
        int windowHeight = 10+TPAD+noOfGrid*gSize+gSize-1+BPAD+30;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.LIGHTYELLOW);
        return scene;
    }
    private int getCellXIndex(double x) { return (int) ((x-LPAD)/(gSize-1)); }
    private int getCellX(int index) { return index+LPAD+index*gSize; }
    private int getCellYIndex(double y) { return (int) ((y-TPAD-30)/(gSize-1)); }
    private int getCellY(int index) { return index+30+TPAD+index*gSize; }

    private Rectangle[][] createGrid() {
        int noOfGrid = GameSetting.instance().getGridSize();
        rect = new Rectangle[noOfGrid][noOfGrid];
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                rect[i][j] = new Rectangle();            //instantiating Rectangle
                rect[i][j].setX(getCellX(i));        //set X coordinate of upper left corner of rectangle
                rect[i][j].setY(getCellY(j));     //set Y coordinate of upper left corner of rectangle
                rect[i][j].setWidth(gSize);              //set the width of rectangle
                rect[i][j].setHeight(gSize);             //set the height of rectangle
                rect[i][j].setFill(Color.LIGHTPINK);
                rect[i][j].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent t) {
                        Rectangle lRect = (Rectangle) t.getSource();
                        double x = lRect.getX();
                        double y = lRect.getY();
                        int i = getCellXIndex(x);
                        int j = getCellYIndex(y);
                        cellSelected(i,j);      // <7>.<e> inform Observer about which cell is selected
                    }
                });
            }
        }
        return rect;
    }
}