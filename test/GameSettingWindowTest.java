import static org.junit.Assert.*;

import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameSettingWindowTest {

	@Test
    public void testStartStage() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        new GameSettingWindow().start(new Stage()); // Create and
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

//	@Test
//	public void testColorChange() {
//		CellColor c = CellColor.GREEN;
//		Color pColor = Color.GREEN;
//		new GameSettingWindow().colorChange(1, pColor);
//		
//		assertEquals(GameSetting.instance().getPlayerCellColor(), c);
//	}

}
