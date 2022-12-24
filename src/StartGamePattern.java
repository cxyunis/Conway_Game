import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGamePattern {
    private static final int[][] a = {{0,1,0},{1,0,0},{1,0,0}};
    private static final int[][] b = {{1,0,0},{1,0,0},{1,0,0}};
    private static final int[][] c = {{1,1,0},{1,0,0},{0,0,0}};
    private static final int[][] d = {{0,0,1},{0,1,0},{1,0,0}};
    private static final int[][] e = {{1,1,0},{1,1,0},{0,0,0}};
    private static final int[][] f = {{1,1,1},{1,0,0},{0,0,0}};
    private static final int[][] g = {{0,1,0},{1,1,1},{0,0,0}};
    private static final int[][] h = {{0,0,1},{1,1,0},{0,1,0}};
    private static final List<int[][]> rawPatterns = new ArrayList<int[][]>() {
        {
            add(a);
            add(b);
            add(c);
            add(d);
            add(e);
            add(f);
            add(g);
            add(h);
        }
    };

    public static Cell[][] getRandomPattern(int playerNo) {
        // represents sets of pattern (2x2, 2x3, 3x2 or 3x3) stored in ArrayList
        // to get a random initial pattern, just supply a random int in [0,1,...,8,9]
        Cell[][] aPattern = new Cell[3][3];
        Random rnd = new Random();
        int r = rnd.nextInt(8);

        Ownership owner;
        if (playerNo==1) {
            owner = Ownership.PLAYER1;
        } else {
            owner = Ownership.PLAYER2;
        }
        int[][] selPattern = rawPatterns.get(r);
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (selPattern[i][j]==0) {
                    aPattern[i][j] = new Cell(CellState.DEAD, Ownership.NONE);
                } else {
                    aPattern[i][j] = new Cell(CellState.ALIVE, owner);
                }
            }
        }
        return aPattern;
    }
}
