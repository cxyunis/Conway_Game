import java.util.Scanner;

public class GameBoard {

    /*
    * GameBoard is designed to handle display only. It should have one-to-one corresponding state as the GUI board
    * */

//    private Cell[][] gameBoard;
//    private String[] player = new String[2];
//
//    public GameBoard(int dimension) {
//        gameBoard = new Cell[dimension][dimension];
//        for (int i=0; i<dimension; i++) {
//            for (int j=0; j<dimension; j++) {
//                gameBoard[i][j] = new Cell(CellState.DEAD, Ownership.NONE);
//            }
//        }
//    }
//    public void displayBoard() {
//        int n = GameSetting.instance().getGridSize();
//        String[] board = new String[2*n+1];
//        board[0] = "╔"+constructString("═╦",n-1)+"═╗";
//        for (int i=0; i<n; i++) {
//            board[2*i+1] = "║"+constructString(" ║",n-1)+" ║";
//            board[2*i+2] = "╠"+constructString("═╬",n-1)+"═╣";
//        }
//        board[2*n] = "╚"+constructString("═╩",n-1)+"═╝";
//        for (int i=0; i<2*n+1; i++) {
//            System.out.println(board[i]);
//        }
//    }
//    private static String constructString(String s, int n) {
//        // concatenate n times of string s
//        String conStr = "";
//        for (int i=0; i<n; i++) {
//            conStr += s;
//        }
//        return conStr;
//    }
//
//    public void displayStatistics() {
//        // trigger by end of generation
//    }
//    public String displayGetInput() {
//        // window-based: move mouse to selected grid and click
//        Scanner inputReader = new Scanner(System.in);
//        System.out.print("Please select grid: ");
//        String s = inputReader.nextLine();
//        return s;
//    }
//
//    public void displayGameOver() {
//
//    }
}
