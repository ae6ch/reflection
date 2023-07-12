package application;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            System.out.println("changepw");
            System.out.println("currentPasswordField = " + currentPasswordField.getText());
            System.out.println("newPasswordField = " + newPasswordField.getText());
            System.out.println("confirmPasswordField = " + confirmPasswordField.getText());
            System.out.println("securityQuestionField = " + securityQuestionField.getText());
            System.out.println("securityAnswerField = " + securityAnswerField.getText());

            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
               System.out.println("Passwords do not match");
               return;
            }
            if (newPasswordField.getText().length() < 8) {
               System.out.println("Password must be at least 8 characters");
               return;
            }
            if (securityQuestionField.getText().length() < 1) {
               System.out.println("Security question must be at least 1 character");
               return;
            }
            if (securityAnswerField.getText().length() < 1) {
               System.out.println("Security answer must be at least 1 character");
               return;
            }
            if (!currentPasswordField.getText().equals("p")) {
               System.out.println("Current password is incorrect");
               return;
            }

            try {
               String sql = "INSERT INTO config (key,value) VALUES (?,?) on CONFLICT(key) DO UPDATE SET value=excluded.value";
               PreparedStatement pstmt = db.getConnection().prepareStatement(sql);
               pstmt.setString(1, "password");
               pstmt.setString(2, newPasswordField.getText());
               pstmt.executeUpdate();

               pstmt.setString(1, "securityquestion");
               pstmt.setString(2, securityQuestionField.getText());
               pstmt.executeUpdate();

               pstmt.setString(1, "securityanswer");
               pstmt.setString(2, securityAnswerField.getText());
               pstmt.executeUpdate();

               pstmt.setString(1, "firstlaunch");
               pstmt.setString(2, "0");
               pstmt.executeUpdate();

               pstmt.close();
            } catch (SQLException sqle) {
               System.out.println("SQL Exception: " + sqle.getMessage());
            }

            changeScene(e, "login.fxml");
            break;
         default:
            System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
            break;

      }
   }

   /**
    * Change the scene
    * 
    * @param e    Event where we can get the stage from
    * @param fxml fxml file of new scene
    */
   private void changeScene(Event e, String fxml) {
      try {
         Parent root = FXMLLoader.load(getClass().getResource(fxml));
         Scene scene = new Scene(root, 640, 480);

         Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
         stage.setScene(scene);
      } catch (IOException ioe) {

      }
   }
}