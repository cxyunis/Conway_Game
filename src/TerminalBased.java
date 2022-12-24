import java.util.ArrayList;
import java.util.List;

public class TerminalBased {
    /*
    * Terminal Based class is to mimic the behaviors of GUI such that it does the same functionality as GUI.
    * most, if not all, of the behaviors will be called by GameModel
    * */
    private List<GameBoardObserver> aObservers = new ArrayList<>();
    public TerminalBased() {
        System.out.println("Terminal Based Game");
    }
    public void start() {
        System.out.println("Start terminal game");

        System.out.println("Create game board and show in terminal with initial patterns for both player");
        System.out.println("Show initial statistics/generation no");

        System.out.println("Enter into loop for the following steps until game over is declared");
        System.out.println("> Asks player 1 to select one cell to kill opponent");
        System.out.println("> Inform GameModel, GameModel valid the move");
        System.out.println("> Asks player 1 to select one cell to come into life");
        System.out.println("> Inform GameModel, GameModel valid the move");
        System.out.println("> Show updated game board");
        System.out.println("> Show player 1 updated statistics for this generation");
        System.out.println("> If it is game over, exit loop");
        System.out.println("> Asks player 2 to select one cell to kill opponent");
        System.out.println("> Asks player 2 to select one cell to come into life");
        System.out.println("> Show updated game board");
        System.out.println("> Show player 2 updated statistics for this generation");
        System.out.println("> If it is game over, exit loop");
    }

    public void registerObserver(GameBoardObserver pObserver) {
        aObservers.add(pObserver);
    }

    public void displayBoard() {
        // display board with symbol
    }
    public void inputForLive() {
        // same action as user click the grid using mouse
    }
    public void inputForKill() {
        // same action as user click the grid using mouse
    }

    public boolean checkForGameOver() {
        // call from GameModel to check if it is game over???
        return true;
    }
}
