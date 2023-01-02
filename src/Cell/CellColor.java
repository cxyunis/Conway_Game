package cell;

import javafx.scene.paint.Color;

public enum CellColor {
    GREY, BLUE, GREEN, INDIGO, ORANGE, RED, VIOLET, YELLOW, CYAN;
    private Color[] color = {Color.GREY, Color.BLUE, Color.GREEN, Color.INDIGO,
            Color.ORANGE, Color.RED, Color.VIOLET, Color.YELLOW, Color.CYAN};
    public Color getColor() {
        return this.color[this.ordinal()];
    }
    /*
    * @pre pColor is the colors from Color[] color
    * */
    public static CellColor getCellColor(Color pColor) {
        for (CellColor cc: CellColor.values()) {
            if (cc.getColor().equals(pColor)) { return cc; }
        }
        return null;
    }
}
