package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 * javafx controller for for resetpw.fxml
 * 
 * @author Steve Rubin
 */
public class ResetPasswordController {
   Database db;
   
   @FXML
   TextField currentPasswordField;
   @FXML
   TextField newPasswordField;
   @FXML
   TextField confirmPasswordField;
   @FXML
   TextField securityQuestionField;
   @FXML
   TextField securityAnswerField;
   
	@FXML
	private SceneController control = new SceneController();

   public ResetPasswordController() {
      System.out.println("ResetPasswordController constructor called");
      db = new Database();

   }

   /**
    * Event handler for any buttons
    * 
    * @param e event
    */
   public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "changePasswordButton": // Change Password Button
            if (checkPasswordRules())
               storePassword();

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
         System.out.println("Passwords do not match");
         return false;
      }
      if (securityQuestionField.getText().length() < 1) {
         System.out.println("Security question must be at least 1 character");
         return false;
      }
      if (securityAnswerField.getText().length() < 1) {
         System.out.println("Security answer must be at least 1 character");
         return false;
      }
      if (!currentPasswordField.getText().equals("p")) {
         System.out.println("Current password is incorrect");
         return false;
      }
      return true;
   }
/**
 * Store the new password in the database
 * TODO: move somewhere else?
 *
 */
   private void storePassword() {
	   db.storepassword(newPasswordField.getText(), securityAnswerField.getText(),
			   securityAnswerField.getText());

   }
}