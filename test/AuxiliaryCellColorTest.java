import javafx.scene.paint.Color;

public class AuxiliaryCellColorTest {
	public AuxiliaryCellColorTest() {
		
	}
	public CellColor getCellColorAux(int n) {
		switch (n) { 
	    case 0:
	    	return CellColor.GREY;
		case 1:return CellColor.BLUE;
	    case 2:return CellColor.GREEN;
	    case 3:return CellColor.INDIGO;
	    case 4:return CellColor.ORANGE;
	    case 5:return CellColor.RED;
	    case 6:return CellColor.VIOLET;
	    case 7:return CellColor.YELLOW;
	    case 8:return CellColor.CYAN;
	    
	  }
		return null;
	}
	public Color getColorAux(int n) {
		switch (n) { 
	    case 0:
	    	return Color.GREY;
		case 1:return Color.BLUE;
	    case 2:return Color.GREEN;
	    case 3:return Color.INDIGO;
	    case 4:return Color.ORANGE;
	    case 5:return Color.RED;
	    case 6:return Color.VIOLET;
	    case 7:return Color.YELLOW;
	    case 8:return Color.CYAN;
	    case 9:return Color.AZURE;
	    
	  }
		return null;
	}

}
