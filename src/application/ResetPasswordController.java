package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * javafx controller for for resetpw.fxml
 * 
 * @author Steve Rubin
 */
public class ResetPasswordController {

	private dal sqlCommand;

	@FXML
	private TextField currentPasswordField;
	@FXML
	private TextField newPasswordField;
	@FXML
	private TextField confirmPasswordField;
	@FXML
	private TextField securityQuestionField;
	@FXML
	private TextField securityAnswerField;
	@FXML
	private Label errorMessage;

	@FXML
	private SceneController control = new SceneController();

	public ResetPasswordController() {
		System.out.println("ResetPasswordController constructor called");
		sqlCommand = new dal();

	}

	/**
	 * Event handler for any buttons
	 * 
	 * @param e event
	 */
	public void buttonPressed(Event e) {
		switch (((Control) e.getSource()).getId()) {
		
		case "changePasswordButton": // Change Password Button
			if (checkPasswordRules()) {
				storePassword();
				control.changeScene(e, "login.fxml");
			} else {
				currentPasswordField.clear();
				newPasswordField.clear();
				confirmPasswordField.clear();
				securityQuestionField.clear();
				securityAnswerField.clear();
			}
			break;
			
		case "cancelButton": // Change Password Button
			control.changeScene(e, "login.fxml");
			break;
			
		default:
			System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
			break;

		}
	}

	/**
	 * Verify the entered passwords mets the password rules
	 * 
	 * @return true if the password meets the rules, false otherwise
	 */
	private boolean checkPasswordRules() {
		if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
			errorMessage.setText("");
			errorMessage.setText("Passwords do not match");
			return false;
		}
		if (securityQuestionField.getText().length() < 1) {
			errorMessage.setText("");
			errorMessage.setText("Security question must be at least 1 character");
			return false;
		}
		if (securityAnswerField.getText().length() < 1) {
			errorMessage.setText("");
			errorMessage.setText("Security answer must be at least 1 character");
			return false;
		}
		if (!currentPasswordField.getText().equals(sqlCommand.selectConfigValue("password"))) {
			errorMessage.setText("");
			errorMessage.setText("Invaild Password");
			return false;
		}
		return true;
	}

	/**
	 * Store the new password in the database TODO: move somewhere else?
	 *
	 */
	private void storePassword() {
		sqlCommand.storepassword(newPasswordField.getText(), securityAnswerField.getText(),
				securityAnswerField.getText());

	}
}