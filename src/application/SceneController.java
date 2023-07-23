package application;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
	/**
	 * Change the scene
	 * 
	 * @param e    Event where we can get the stage from
	 * @param fxml fxml file of new scene
	 */
	void changeScene(Event e, String fxml) {
		changeScene((Stage) ((Node) e.getSource()).getScene().getWindow(), fxml);
	}
	/**
	 * Change the scene
	 * 
	 * @param s    Stage to change scene of
	 * @param fxml fxml file of new scene
	 */
	void changeScene(Stage s, String fxml) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource(fxml));
			Scene scene = new Scene(root, 640, 480);

			s.setScene(scene);
		} catch (IOException ioe) { // if the fxml file isn't readable
			ioe.printStackTrace();
		}
	}

}
