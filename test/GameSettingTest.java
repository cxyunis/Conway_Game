import static org.junit.Assert.*;

import org.junit.Test;

import javafx.scene.paint.Color;

public class GameSettingTest {

	@Test
	public void testInstance() {
		assertEquals(CellColor.BLUE, GameSetting.instance().getPlayerCellColor()[0]);
		assertEquals(CellColor.RED, GameSetting.instance().getPlayerCellColor()[1]);

	}

	@Test
	public void testSetPlayerChosenColor() {
		AuxiliaryCellColorTest aux = new AuxiliaryCellColorTest();
		Color c;
		for(int i = 1; i<3;i++) {
			for(int j = 0; j<8;j++) {
				c = aux.getColorAux(j);
				GameSetting.instance().setPlayerChosenColor(i, c);
				assertEquals(aux.getCellColorAux(j), GameSetting.instance().getPlayerCellColor()[i-1]);
			}
		}
	}

	@Test
	public void testSwapPlayerPlayingOrder() {
		GameSetting.instance().setPlayerName(1, "Mario");
		GameSetting.instance().setPlayerName(2, "Jhon");
		GameSetting.instance().setPlayerChosenColor(1, Color.BLUE);
		GameSetting.instance().setPlayerChosenColor(2, Color.RED);
		
		Cell[][] p1 = new Cell[6][6];
		AuxiliaryCellTest aux = new AuxiliaryCellTest();
		CellState cellState;
	    Ownership cellOwner;
		for(int i = 0; i < 6;i++) {
			cellState = aux.returnCellState(i);
			for(int j = 0; j < 6;j++) {
				cellOwner = aux.returnCellOwner(j);
				p1[i][j]= new Cell(cellState, cellOwner);
			}
		}
		
		Cell[][] p2 = new Cell[6][6];
		for(int i = 5; i > -1;i--) {
			cellState = aux.returnCellState(i);
			for(int j = 5; j > -1;j--) {
				cellOwner = aux.returnCellOwner(j);
				p2[i][j]= new Cell(cellState, cellOwner);
			}
		}
		GameSetting.instance().setPlayerChosenInitialPattern(1, p1);
		GameSetting.instance().setPlayerChosenInitialPattern(2, p2);
		GameSetting.instance().swapPlayerPlayingOrder();
		
		assertEquals("Jhon", GameSetting.instance().getPlayerName(1));
		assertEquals("Mario", GameSetting.instance().getPlayerName(2));
		
		assertEquals(Color.RED, GameSetting.instance().getPlayerColor(1));
		assertEquals(Color.BLUE, GameSetting.instance().getPlayerColor(2));
	
//		assertEquals(p2, GameSetting.instance().getPlayerChosenInitialPattern(1));
//		assertEquals(p1, GameSetting.instance().getPlayerChosenInitialPattern(2));
	}

}
