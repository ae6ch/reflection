package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class
 */
public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
			Parent loginFxml = FXMLLoader.load(getClass().getResource("login.fxml"));

			Scene loginScene = new Scene(loginFxml, 650, 480);

			stage.setTitle("Reflection");
			stage.setScene(loginScene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
