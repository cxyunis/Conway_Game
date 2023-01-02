import static org.junit.Assert.*;

import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class GUIGameBoardTest {

	@Test
	public void testStartStage() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				new JFXPanel(); // Initializes the JavaFx Platform
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						new GUIGameBoard().start(new Stage()); // Create and
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
