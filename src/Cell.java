public class Cell {
    /*
     *  record the cell status either alive, dead (or
     * */

    private CellState cellState;

    public Cell() {
        cellState = CellState.EMPTY;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }
    public CellState getCellState() { return this.cellState; }
}