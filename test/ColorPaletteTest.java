import static org.junit.Assert.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.Test;

public class ColorPaletteTest {
	
	int playerNo;
	@Test
	public void testColorPalette() {
		 for (int playerNo=1; playerNo<3; playerNo++) {
			 ColorPalette colorPalette = new ColorPalette(playerNo);
			 assertEquals(playerNo, colorPalette.getPlayerNo());
		 }
	}

	@Test
	public void testRegisterObserver() {
		GameSettingWindow gameSettingWindow = new GameSettingWindow();
		ColorPalette colorPalette = new ColorPalette(1);
		colorPalette.registerObserver(gameSettingWindow);
		assertEquals(gameSettingWindow, colorPalette.getaObservers().get(0));
	}

//	@Test
//	public void testStartStage() throws Exception {
//		ColorPalette colorPalette = new ColorPalette(1);
//		GameSettingWindow gameSettingWindow = new GameSettingWindow();
//		
//		colorPalette.registerObserver(gameSettingWindow);
//		Stage stage = new Stage();
//		colorPalette.start(stage);
//		
//		assertEquals(gameSettingWindow.getBtnSelectID()[0] , colorPalette.getLblSelectedColorTest());
//	}
	
	@Test
    public void testA() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
							new ColorPalette(1).start(new Stage());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // Create and
                                                        // initialize
                                                        // your app.

                    }
                });
            }
        });
        thread.start();// Initialize the thread
        Thread.sleep(10000); // Time to use the app, with out this, the thread
                                // will be killed before you can tell.
    }

}
