
public class AuxiliaryCellTest {
	
	public AuxiliaryCellTest() {
		
	}
	
	public CellState returnCellState(int n) {
		if(n%2 == 0) {
			return CellState.ALIVE;
		}else {
			return CellState.DEAD;
		}
		
	}
	
	public Ownership returnCellOwner(int n) {
		if(n%3 == 0) {
			return Ownership.NONE;
		}else if(n%3 == 1) {
			return Ownership.PLAYER1;
		}else {
			return Ownership.PLAYER2;
		}
		
	}
}
