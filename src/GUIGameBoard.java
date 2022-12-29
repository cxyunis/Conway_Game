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


public class GUIGameBoard extends Application {

    //Rectangle[][] rect;
    private static final int GRID_SIZE = 20;  // grid (individual cell) = width = height
    private static final int LPAD = 20;
    private static final int RPAD = 20;
    private static final int TPAD = 140;
    private static final int BPAD = 20;
    private Label[] lblGeneration = new Label[2];
    private Label[] lblPlayerName = new Label[2];
    private Circle[] crcPlayerColor = new Circle[2];
    private Label[] lblPopulation = new Label[2];

    private  Rectangle[][] grids;
    private Label lblPlayerTurn; // = new Label();
    private List<GameBoardObserver> aObservers = new ArrayList<>();

    public void start(Stage stage) {
        stage.setTitle("Conway's Game of Life");
        Scene scene = createScene();
        stage.setScene(scene);
        stage.show();
        rearrangeNodes();
    }
    public void registerObserver(GameBoardObserver pObserver) {
        aObservers.add(pObserver);
    }
    private void observedCellSelected(int row, int col, String action) {
        for(GameBoardObserver observer : aObservers) {
            observer.cellSelected(row,col,action);
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

    public void updateGameBoard(Cell[][] cells) {
        int size = GameSetting.instance().getGridSize();
        Color p1Color = GameSetting.instance().getPlayerColor(1);
        Color p2Color = GameSetting.instance().getPlayerColor(2);
        for (int r=0; r<size; r++) {
            for (int c=0; c<size; c++) {
                if (cells[r][c].getCellState().equals(CellState.ALIVE)) {
                    if (cells[r][c].getCellOwner().equals(Ownership.PLAYER1)) {
                        grids[r][c].setFill(p1Color);
                    } else {
                        grids[r][c].setFill(p2Color);
                    }
                }
            }
        }
    }
    public void refreshEmptyCellUpdateOnGameBoard(int r, int c) {
        grids[r][c].setFill(GameSetting.instance().getBoardColor());
    }
    public void refreshSpringToLiveOnGameBoard(int playerNo, int r, int c) {
        grids[r][c].setFill(GameSetting.instance().getPlayerColor(playerNo));
    }
    public void refreshPopulation(int playerNo, int population) {
        lblPopulation[playerNo-1].setText("Population: "+population);
    }
    private Scene createScene() {
        Group root = new Group();

        grids = createGrid();
        int noOfGrid = GameSetting.instance().getGridSize();    //note that this GridSize != gSize
        for (int r=0; r<noOfGrid; r++) {
            for (int c=0; c<noOfGrid; c++) {
                root.getChildren().addAll(grids[r][c]);
            }
        }

        for (int i=0; i<2; i++) {
            lblPlayerName[i] = constructPlayerNameLabel(i+1,0,i*40,10);
            crcPlayerColor[i] = new Circle(80,i*40+20,10,GameSetting.instance().getPlayerColor(i+1));
            lblGeneration[i] = constructGenerationLabel(i+1,110,i*40,10);
            lblPopulation[i] = constructPopulationLabel(i+1,160,i*40,10);

            root.getChildren().addAll(lblPlayerName[i], crcPlayerColor[i],lblGeneration[i],lblPopulation[i]);
        }
        Label note = constructMouseClickNote(0,80,10);
        lblPlayerTurn = constructPlayerTurnLabel(1,0,120,10);
        root.getChildren().addAll(note, lblPlayerTurn);


        // noOfGrid*gSize gives total length of grid w/o gap, and 2nd gSize
        int windowWidth = LPAD+noOfGrid* GRID_SIZE + GRID_SIZE -1+RPAD;
        int windowHeight = 10+TPAD+noOfGrid* GRID_SIZE + GRID_SIZE -1+BPAD+30;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.LIGHTYELLOW);
        return scene;
    }
    private void rearrangeNodes() {
        double wPN1 = lblPlayerName[0].getWidth();
        double wPN2 = lblPlayerName[1].getWidth();
        double maxWPN = max(wPN1,wPN2);
        double wPC1 = crcPlayerColor[0].getRadius();
        crcPlayerColor[0].setCenterX(maxWPN+wPC1);
        double wPC2 = crcPlayerColor[1].getRadius();
        crcPlayerColor[1].setCenterX(maxWPN+wPC2);
        double wGen1 = lblGeneration[0].getWidth();
        lblGeneration[0].setLayoutX(maxWPN+2*wPC1);
        double wGen2 = lblGeneration[1].getWidth();
        lblGeneration[1].setLayoutX(maxWPN+2*wPC2);
        lblPopulation[0].setLayoutX(maxWPN+2*wPC1+wGen1);
        lblPopulation[1].setLayoutX(maxWPN+2*wPC2+wGen2);
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
    private double getCellXIndex(double x) { return ((x-LPAD)/(GRID_SIZE +1)); }
    private int getCellX(int index) { return index+LPAD+index* GRID_SIZE; }
    private double getCellYIndex(double y) { return ((y-TPAD-30)/(GRID_SIZE +1)); }
    private int getCellY(int index) { return index+30+TPAD+index* GRID_SIZE; }

    private Rectangle[][] createGrid() {
        int noOfGrid = GameSetting.instance().getGridSize();
        //Rectangle[][] rect;
        Rectangle[][] rect = new Rectangle[noOfGrid][noOfGrid];
        for (int r=0; r<noOfGrid; r++) {
            for (int c=0; c<noOfGrid; c++) {
                rect[r][c] = new Rectangle();            //instantiating Rectangle
                rect[r][c].setX(getCellX(c));            //set X coordinate of upper left corner of rectangle
                rect[r][c].setY(getCellY(r));            //set Y coordinate of upper left corner of rectangle
                rect[r][c].setWidth(GRID_SIZE);              //set the width of rectangle
                rect[r][c].setHeight(GRID_SIZE);             //set the height of rectangle
                rect[r][c].setFill(GameSetting.instance().getBoardColor());
                rect[r][c].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent e) {
                        Rectangle lRect = (Rectangle) e.getSource();
                        double x = lRect.getX();
                        double y = lRect.getY();
                        int r = (int) getCellYIndex(y);
                        int c = (int) getCellXIndex(x);

                        MouseButton btn = e.getButton();
                        if (btn==MouseButton.PRIMARY) {
                            observedCellSelected(r,c,"ALIVE");      // <7>.<e> inform Observer about which cell is selected
                        } else if (btn==MouseButton.SECONDARY) {
                            observedCellSelected(r,c,"KILL");      // <7>.<e> inform Observer about which cell is selected
                        }
                    }
                });
            }
        }
        return rect;
    }
}