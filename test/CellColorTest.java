import static org.junit.Assert.*;

import org.junit.Test;
import javafx.scene.paint.Color;

public class CellColorTest {
	AuxiliaryCellColorTest aux = new AuxiliaryCellColorTest();
	CellColor c, c2;
	Color pColor, pColor2;

	@Test
	public void testGetColor() {
		for(int i = 0; i < 9;i++) {
			c = aux.getCellColorAux(i);
			pColor = c.getColor();
			pColor2 = aux.getColorAux(i);
			assertEquals(pColor, pColor2);
		}
	}
	
	@Test
	public void testGetCellColor() {
		for(int i = 0; i < 10;i++) {
			if(i == 9) {
				pColor = aux.getColorAux(i);
				c2 = CellColor.getCellColor(pColor);
				assertEquals(null, c2);
			}else {
				c = aux.getCellColorAux(i);
				pColor = aux.getColorAux(i);
				c2 = CellColor.getCellColor(pColor);
				assertEquals(c, c2);
			}
			
		}
	}

}
