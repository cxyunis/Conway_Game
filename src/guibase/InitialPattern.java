package guibase;

import cell.Cell;
import cell.CellState;
import cell.Ownership;
import core.GameSetting;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InitialPattern extends Application {
    private static final int PADDING = 30;
    private int dimension;
    private int playerNo;
    private Color playerColor;
    private int[] playerCounter = {0,0};
    public InitialPattern(int dimension, int playerNo) throws Exception {
        this.dimension = dimension;
        this.playerNo = playerNo;
        this.playerColor = GameSetting.instance().getPlayerColor(playerNo);
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Initial Pattern Maker");
        Rectangle grids[][] = new Rectangle[dimension][dimension];
        int maxNoOfCell = GameSetting.instance().getMaxCellSelection();

        Group root = new Group();
        Color boardColor = Color.LIGHTPINK;
        for (int r=0; r<dimension; r++) {
            for (int c=0; c<dimension; c++) {
                grids[r][c] = new Rectangle();
                grids[r][c].setFill(boardColor);
                grids[r][c].setWidth(20);
                grids[r][c].setHeight(20);
                grids[r][c].setX(PADDING +c+c*20);
                grids[r][c].setY(PADDING +r+r*20);
                root.getChildren().addAll(grids[r][c]);
                grids[r][c].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent e) {
                        Rectangle lRect = (Rectangle) e.getSource();
                        double x = lRect.getX();
                        double y = lRect.getY();
                        int r = (int) (y- PADDING)/21;
                        int c = (int) (x- PADDING)/21;

                        if (grids[r][c].getFill()==boardColor) {
                            if (playerCounter[playerNo-1]<maxNoOfCell) {
                                grids[r][c].setFill(playerColor);
                                playerCounter[playerNo-1]++;
                            }
                        } else {
                            grids[r][c].setFill(boardColor);
                            playerCounter[playerNo-1]--;
                        }
                    }
                });
            }
        }
        Label note = new Label("Choose at most "+maxNoOfCell+" cells");
        note.setTextFill(Color.RED);
        note.setPadding(new Insets(5,5,5,5));
        note.setLayoutX(PADDING+maxNoOfCell*dimension+dimension);
        root.getChildren().add(note);

        Button btnOK = new Button("OK");
        Cell[][] pattern = new Cell[dimension][dimension];
        btnOK.setOnAction(e -> {
            for (int r=0; r<dimension; r++) {
                for (int c=0; c<dimension; c++) {
                    if (grids[r][c].getFill()== playerColor) {
                        pattern[r][c] = new Cell(CellState.ALIVE, Ownership.valueOf("PLAYER"+playerNo));
                    } else {
                        pattern[r][c] = new Cell(CellState.DEAD, Ownership.NONE);
                    }
                }
            }
            GameSetting.instance().setPlayerChosenInitialPattern(playerNo,pattern); // probably need to implement observer pattern
            stage.close();
        });
        //GameSetting.instance().setPlayerChosenInitialPattern(playerNo,pattern);
        btnOK.setLayoutX(PADDING +dimension*20+dimension+20+5);
        btnOK.setLayoutY(PADDING +20+2*dimension);
        root.getChildren().add(btnOK);

        int width = PADDING +(dimension-1)+dimension*20+3* PADDING;  // padding+gap+dimension*width+padding
        int height = PADDING +(dimension-1)+dimension*20+ PADDING;
        Scene scene = new Scene(root,width,height, Color.LIGHTYELLOW);
        stage.setScene(scene);
        stage.show();
    }
}
