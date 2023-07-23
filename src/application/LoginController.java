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
	private static boolean firstlaunch = false;

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
		// System.out.println("LoginController constructor called");
		sqlCommand = new SqlDal();

		String question = sqlCommand.selectConfigValue("securityquestion");

		if (question == null) {
			// System.out.println("value = " + question);
			firstlaunch = true;
		}

		if (firstlaunch) {
			// System.out.println("First launch detected, please change your password");
		} else {
			// System.out.println("Not our first time launching, please enter your
			// password");
		}

	}

	/**
	 * Event handler for the Login button*
	 * 
	 * @param e event
	 */

	public void loginButtonPressed(Event e) {
		// System.out.printf("Login button pressed, with Password of %s\n",
		// passwordField.getText());

		String pwd = sqlCommand.selectConfigValue("password");

		if (passwordField.getText().equals(pwd)) {
			// System.out.println("Password is correct");

			// see if first launch is set to true
			// if so changeScene to resetpw.fxml
			// else changeScene to mainmenu.fxml
			if (firstlaunch) {
				// System.out.println("First launch detected, please change your password");
				firstlaunch = false;
				control.changeScene(e, "resetpw.fxml");
			} else {
				// System.out.println("Not our first time launching, please enter your
				// password");
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
		// System.out.println("Reset button pressed");

		if (firstlaunch) {
			errorMessage.setText("");
			// errorMessage.setText("First time launch, unable to Reset Password");
		} else {
			control.changeScene(e, "securityQuestionAnswer.fxml");
		}
	}
}