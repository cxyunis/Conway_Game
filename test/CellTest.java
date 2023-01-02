import static org.junit.Assert.*;

import org.junit.Test;

public class CellTest {
	CellState cellState;
    Ownership cellOwner;
    CellState cellState2;
    Ownership cellOwner2;
    AuxiliaryCellTest aux = new AuxiliaryCellTest();
	@Test
	public void testCell() {
		for(int i = 0; i <6; i++) {
			cellState = aux.returnCellState(i);
			cellOwner = aux.returnCellOwner(i);
			Cell cell = new Cell(cellState, cellOwner);
			assertEquals(cellState, cell.getCellState());
			assertEquals(cellOwner, cell.getCellOwner());
			for(int j = 0; j <3; j++) {
				cellOwner2 = aux.returnCellOwner(j);
				cell.setCellOwner(cellOwner2);
				assertEquals(cellOwner2, cell.getCellOwner());
				cell.setCellOwner(cellOwner);
			}
			for(int k = 0; k <2; k++) {
				cellState2 = aux.returnCellState(k);
				cell.setCellState(cellState2);
				assertEquals(cellState2, cell.getCellState());
				cell.setCellState(cellState);
			}
		}
		
	}

}
