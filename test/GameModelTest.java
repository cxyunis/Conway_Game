import static org.junit.Assert.*;

import org.junit.Test;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

public class GameModelTest {


	@Test
	public void testGameModel() {
		GameSetting.instance().setPlayerName(1, "GameModel");
		GameSetting.instance().setPlayerName(2, "Test");
		GameModel game = new GameModel();
	}
	
	@Test
    public void testStartGame() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        new GameModel().startGame(); // Create and
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
