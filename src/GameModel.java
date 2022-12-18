import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameModel {
    /*
     * The GameModel is the CPU of the game. It is designed to be able to either use GUI or Terminal
     * To achieve this goal, we need to implements OBSERVER so that it can notify methods defined when state changed
     * So, the TerminalBased class need to have one-to-one corresponding method/concern/responsibility as the GUI
     * For the GUI part, any state change will be passed back to GameModel to decide next course of action (flow of
     * the game.
     * */
    public static void startGame() {
        System.out.println(GameSetting.instance().getPlatform());
        if (GameSetting.instance().getPlatform().equals("GUI")) {
            Stage stage = new Stage();
            GUIBased gui = new GUIBased();
            gui.start(stage);
        } else {
            TerminalBased tbGame = new TerminalBased();
            tbGame.start();
        }
    }
    /*
     *  Count how many self (owner) neighbors and opponent neighbors
     *  @param checkGrids is 3x3 grids, and center cell is to be checked for own/opponent neighbors
     *  @param owner: note center gird can be NONE, count is still valid to see if DEAD cell spring into life
     *  @return: ArrayList<int> contains 2 elements, 1st element is owner's neighbors, 2nd element is opponent's neighbors
     *  */
    private ArrayList<Integer> getNeighborCount(Cell[][] checkGrids, Ownership owner) {
        ArrayList<Integer> neighbours = new ArrayList<Integer>(2);
        int ownerNeighbor = 0;
        int opponentNeighbor = 0;
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (i==1 && j==1) continue; // skip center cell check
                if (checkGrids[i][j].getCellOwner()==Ownership.NONE) continue;  // skip
                if (checkGrids[i][j].getCellState()==CellState.DEAD) continue;  // skip
                if (checkGrids[i][j].getCellOwner()==owner) {
                    ownerNeighbor++;
                } else {
                    opponentNeighbor++;
                }
            }
        }
        neighbours.add(0, ownerNeighbor);
        neighbours.add(1,opponentNeighbor);
        return neighbours;
    }
}