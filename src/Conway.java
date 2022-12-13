import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//configure to run window
//https://stackoverflow.com/questions/51478675/error-javafx-runtime-components-are-missing-and-are-required-to-run-this-appli

//to create grids
//https://stackoverflow.com/questions/36369224/creating-an-editable-paintable-grid-in-javafx

public class Conway extends Application {
    public static final int WIDTH = 200;

    private static final int GAP = 10;
    private static final int MARGIN = 20;

    public static void main(String[] pArgs)
    {
        launch(pArgs);
    }

    @Override
    public void start(Stage pPrimaryStage)
    {
        GridPane root = createPane(); // The root of the GUI component graph
        pPrimaryStage.show();
    }
    private static GridPane createPane()
    {
        GridPane root = new GridPane();
        root.setHgap(GAP);
        root.setVgap(GAP);
        root.setPadding(new Insets(MARGIN));
        return root;
    }

    private static void drawGrid() {

    }
}