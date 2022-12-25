/*
*   In order to be able to use in Terminal or GUI, internal symbol will be used
*   1. ALIVE/DEAD: use enum.ALIVE/enum.DEAD
*   2. NONE/PLAYER1/PLAYER2, hence requires mapping to user selected symbol/color
* */
public class Cell {
    private CellState cellState;
    private Ownership cellOwner;

    public Cell(CellState cellState, Ownership cellOwner) {
        this.cellState = cellState;
        this.cellOwner = cellOwner;
    }
    public void setCellOwner(Ownership cellOwner) { this.cellOwner = cellOwner; }
    public void setCellState(CellState cellState) { this.cellState = cellState; };
    public CellState getCellState() { return cellState; }
    public Ownership getCellOwner() { return cellOwner; }
}
