import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;


public class GUIBased extends Application {

    //Rectangle[][] rect;
    private static int gSize = 20;  // grid (individual cell) = width = height
    private static final int LPAD = 20;
    private static final int RPAD = 20;
    private static final int TPAD = 140;
    private static final int BPAD = 20;
    private Label[] lblGeneration = new Label[2];
    private Label[] lblPName = new Label[2];
    private Circle[] crcPColor = new Circle[2];
    private Label[] lblPopulation = new Label[2];

    private  Rectangle[][] grids;
    private Label lblPlayerTurn; // = new Label();
    private List<GameBoardObserver> aObservers = new ArrayList<>();

    public void start(Stage stage) {
        stage.setTitle("Conway's Game of Life");
        Scene scene = createScene();
        stage.setScene(scene);
        stage.show();
        rearrangeNodes(lblPName,crcPColor,lblGeneration,lblPopulation);
    }
    public void registerObserver(GameBoardObserver pObserver) {
        aObservers.add(pObserver);
    }
    public void removeObserver(GameBoardObserver pObserver) {
        aObservers.remove(pObserver);
    }
    void observedCellSelected(int x, int y, String action) {
        for(GameBoardObserver observer : aObservers) {
            observer.cellSelected(x,y,action);
        }
    }
    public void refreshPlayerGeneration(int playerNo) {
        int g = GameSetting.instance().getPlayerGeneration(playerNo);
        String s = "Generation: "+g;
        lblGeneration[playerNo-1].setText(s);

    }
    public void refreshPlayerTurn() {
        int playerTurn = GameSetting.instance().getPlayerTurn();
        System.out.println("refreshPlayerTurn: "+playerTurn);
        lblPlayerTurn.setText("It is "+GameSetting.instance().getPlayerName(playerTurn)+"'s turn");
        lblPlayerTurn.setTextFill(GameSetting.instance().getPlayerColor(playerTurn));
    }
    public void showInitialPattern(int playerNo, int startPosX, int startPosY, Cell[][] cells) {
        // call from outsider to set initial pattern on board
        Color c = GameSetting.instance().getPlayerColor(playerNo);
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (cells[i][j].getCellState().equals(CellState.ALIVE)) {
                    grids[startPosX + i][startPosY + j].setFill(c);
                }
            }
        }
    }
    public void refreshEmptyCellUpdateOnGameBoard(int x, int y) {
        grids[x][y].setFill(GameSetting.instance().getBoardColor());
    }
    public void refreshSpringToLiveOnGameBoard(int playerNo, int x, int y) {
        grids[x][y].setFill(GameSetting.instance().getPlayerColor(playerNo));
    }
    public void refreshPopulation(int playerNo, int population) {
        lblPopulation[playerNo-1].setText("Population: "+population);
    }
    private Scene createScene() {
        Group root = new Group();

        grids = createGrid();
        int noOfGrid = GameSetting.instance().getGridSize();    //note that this GridSize != gSize
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                root.getChildren().addAll(grids[i][j]);
            }
        }

        for (int i=0; i<2; i++) {
            lblPName[i] = constructPlayerNameLabel(i+1,0,i*40,10);
            crcPColor[i] = new Circle(80,i*40+20,10,GameSetting.instance().getPlayerColor(i+1));
            lblGeneration[i] = constructGenerationLabel(i+1,110,i*40,10);
            lblPopulation[i] = constructPopulationLabel(i+1,160,i*40,10);

            root.getChildren().addAll(lblPName[i],crcPColor[i],lblGeneration[i],lblPopulation[i]);
        }
        Label note = constructMouseClickNote(0,80,10);
        lblPlayerTurn = constructPlayerTurnLabel(1,0,120,10);
        root.getChildren().addAll(note, lblPlayerTurn);


        // noOfGrid*gSize gives total length of grid w/o gap, and 2nd gSize
        int windowWidth = LPAD+noOfGrid*gSize+gSize-1+RPAD;
        int windowHeight = 10+TPAD+noOfGrid*gSize+gSize-1+BPAD+30;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.LIGHTYELLOW);
        return scene;
    }
    private void rearrangeNodes(Label[] lPN, Circle[] cPC, Label[] lGen, Label[] lPop) {
        double wPN1 = lPN[0].getWidth();
        //System.out.println(wPN1);
        double wPN2 = lPN[1].getWidth();
        //System.out.println(wPN2);
        double maxWPN = max(wPN1,wPN2);
        //System.out.println(maxWPN);
        double wPC1 = cPC[0].getRadius();
        cPC[0].setCenterX(maxWPN+wPC1);
        //System.out.println(maxWPN+wPC1);
        double wPC2 = cPC[1].getRadius();
        cPC[1].setCenterX(maxWPN+wPC2);
        //System.out.println(maxWPN+wPC2);
        double wGen1 = lGen[0].getWidth();
        lGen[0].setLayoutX(maxWPN+2*wPC1);
        //System.out.println(maxWPN+2*wPC1);
        double wGen2 = lGen[1].getWidth();
        lGen[1].setLayoutX(maxWPN+2*wPC2);
        //System.out.println(maxWPN+2*wPC2);
        lPop[0].setLayoutX(maxWPN+2*wPC1+wGen1);
        lPop[1].setLayoutX(maxWPN+2*wPC2+wGen2);
    }
    private Label constructPopulationLabel(int playerNo,int x,int y,int padding) {
        String s = "Population: 0";
        Label lblPopulation = new Label(s);
        lblPopulation.setLayoutX(x);
        lblPopulation.setLayoutY(y);
        lblPopulation.setPadding(new Insets(padding,padding,padding,padding));
        return lblPopulation;
    }
    private Label constructGenerationLabel(int playerNo,int x,int y,int padding) {
        String s = "Generation: "+String.valueOf(GameSetting.instance().getPlayerGeneration(playerNo));
        Label lblGen = new Label(s);
        lblGen.setLayoutX(x);
        lblGen.setLayoutY(y);
        lblGen.setPadding(new Insets(padding,padding,padding,padding));
        return lblGen;
    }
    private Label constructMouseClickNote(int x,int y,int padding) {
        // instruction to user how to provide action for 'turning cell to dead and springs into live
        Label note = new Label("Left click: ALIVE, Right click: DEAD");
        note.setLayoutX(x);
        note.setLayoutY(y);
        note.setPadding(new Insets(padding,padding,padding,padding));
        return note;
    }
    private Label constructPlayerTurnLabel(int playerNo,int x,int y,int padding) {
        Label lblPT = new Label("It is "+GameSetting.instance().getPlayerName(playerNo)+"'s turn");
        lblPT.setTextFill(GameSetting.instance().getPlayerColor(playerNo));
        lblPT.setLayoutX(x);
        lblPT.setLayoutY(y);
        lblPT.setPadding(new Insets(padding,padding,padding,padding));
        return lblPT;
    }
    private Label constructPlayerNameLabel(int playerNo,int x,int y,int padding) {
        Label lblPlayerName = new Label(GameSetting.instance().getPlayerName(playerNo));
        lblPlayerName.setLayoutX(x);
        lblPlayerName.setLayoutY(y);
        lblPlayerName.setPadding(new Insets(padding,padding,padding,padding));
        return lblPlayerName;
    }
    private double getCellXIndex(double x) { return ((x-LPAD)/(gSize+1)); }
    private int getCellX(int index) { return index+LPAD+index*gSize; }
    private double getCellYIndex(double y) { return ((y-TPAD-30)/(gSize+1)); }
    private int getCellY(int index) { return index+30+TPAD+index*gSize; }

    private Rectangle[][] createGrid() {
        int noOfGrid = GameSetting.instance().getGridSize();
        //Rectangle[][] rect;
        Rectangle[][] rect = new Rectangle[noOfGrid][noOfGrid];
        for (int i=0; i<noOfGrid; i++) {
            for (int j=0; j<noOfGrid; j++) {
                rect[i][j] = new Rectangle();            //instantiating Rectangle
                rect[i][j].setX(getCellX(i));        //set X coordinate of upper left corner of rectangle
                rect[i][j].setY(getCellY(j));     //set Y coordinate of upper left corner of rectangle
                rect[i][j].setWidth(gSize);              //set the width of rectangle
                rect[i][j].setHeight(gSize);             //set the height of rectangle
                rect[i][j].setFill(GameSetting.instance().getBoardColor());
                rect[i][j].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent e) {
                        Rectangle lRect = (Rectangle) e.getSource();
                        double x = lRect.getX();
                        double y = lRect.getY();
                        int i = (int) getCellXIndex(x);
                        int j = (int) getCellYIndex(y);

                        MouseButton btn = e.getButton();
                        if (btn==MouseButton.PRIMARY) {
                            observedCellSelected(i,j,"ALIVE");      // <7>.<e> inform Observer about which cell is selected
                        } else if (btn==MouseButton.SECONDARY) {
                            observedCellSelected(i,j,"KILL");      // <7>.<e> inform Observer about which cell is selected
                        }
                    }
                });
            }
        }
        return rect;
    }
}