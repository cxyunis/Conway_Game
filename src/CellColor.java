import javafx.scene.paint.Color;

public enum CellColor {
    BLACK, BLUE, GREEN, INDIGO, ORANGE, RED, VIOLET, YELLOW;

    private Color[] color = {Color.BLACK, Color.BLUE, Color.GREEN, Color.INDIGO,
                             Color.ORANGE, Color.RED, Color.VIOLET, Color.YELLOW};
    public Color getColor() {
        return this.color[this.ordinal()];
    }
}
