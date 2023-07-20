 package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SecurityQuestionAnswerController {
	
	private dal sqlCommand;
	
	@FXML
	private Label securityQuestionField;
	@FXML
	private TextField securityAnswerField;
	@FXML
	private Label errorMessage;

	@FXML
	private SceneController control = new SceneController();
	
	public SecurityQuestionAnswerController() {
		System.out.println("SecurityQuestionAnswerController constructor called");
		sqlCommand = new dal();
	}
	
	public void initialize() {
		System.out.println("SecurityQuestionAnswerController initialize called");
		securityQuestionField.setText(sqlCommand.selectConfigValue("securityquestion"));
	}
	
	/**
	 * Event handler for any buttons
	 * 
	 * @param e event
	 */
	public void buttonPressed(Event e) {
		switch (((Control) e.getSource()).getId()) {
		
		case "nextButton": // go to resetpw scene
			if (checkAnswer()) {
				control.changeScene(e, "resetpw.fxml");
			} else {
				errorMessage.setText("");
				errorMessage.setText("Invaild Answer");
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
	
	public boolean checkAnswer() {
		
		String Answer = sqlCommand.selectConfigValue("securityanswer");
		
		if(securityAnswerField.getText().equals(Answer))
			return true;
		
		return false;
	}

}
