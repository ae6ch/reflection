package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * javafx controller for for login.fxml
 * 
 */
public class LoginController {

	private SqlDal sqlCommand;
	private static boolean firstlaunch = false; // TODO: lets get rid of this

	@FXML
	private TextField passwordField;
	@FXML
	private Label errorMessage;

	@FXML
	private SceneController control = new SceneController();

	/**
	 * Constructor
	 */
	public LoginController() {
		sqlCommand = new SqlDal();

		String question = sqlCommand.selectConfigValue(ConfigKey.SECURITYQUESTION);

		if (question == null) {
			firstlaunch = true;
		}

	}

	/**
	 * Event handler for the Login button*
	 * 
	 * @param e event
	 */

	public void loginButtonPressed(Event e) {

		String pwd = sqlCommand.selectConfigValue(ConfigKey.PASSWORD);

		if (passwordField.getText().equals(pwd)) {

			// see if first launch is set to true
			// if so changeScene to resetpw.fxml
			// else changeScene to mainmenu.fxml
			if (firstlaunch) {
				firstlaunch = false;
				control.changeScene(e, "resetpw.fxml");
			} else {

				control.changeScene(e, "mainmenu.fxml");
			}
		} else {
			errorMessage.setText("");
			errorMessage.setText("Invaild password");
		}
	}

	/**
	 * Event handler for the Login button*
	 * 
	 * @param e event
	 */
	public void resetButtonPressed(Event e) {
		if (firstlaunch) {
			errorMessage.setText("");
		} else {
			control.changeScene(e, "securityQuestionAnswer.fxml");
		}
	}
}