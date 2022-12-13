public class Cell {
    /*
    *  record the cell status either alive, dead (or
    * */

    private CellState cellState;

    public Cell(CellState cellState) {
        this.cellState = cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }
}
