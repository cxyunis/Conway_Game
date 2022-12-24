import javafx.scene.paint.Color;

public enum CellColor {
    GREY, BLUE, GREEN, INDIGO, ORANGE, RED, VIOLET, YELLOW, CYAN;

    private Color[] color = {Color.GREY, Color.BLUE, Color.GREEN, Color.INDIGO,
            Color.ORANGE, Color.RED, Color.VIOLET, Color.YELLOW, Color.CYAN};
    public Color getColor() {
        return this.color[this.ordinal()];
    }
}
