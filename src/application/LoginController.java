package application;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * javafx controller for for login.fxml
 * 
 * @author Steve Rubin
 */
public class LoginController {

   @FXML
   TextField passwordField;

   /**
    * Event handler for the Login button*
    * 
    * @param e event
    */

   public void loginButtonPressed(Event e) {
      System.out.printf("Login button pressed, with Password of %s\n", passwordField.getText());
      if (passwordField.getText().equals("p")) { // Hard Coded check for now to see if the password is the default
         System.out.println("Entered the default password, switch to resetpw");
         changeScene(e, "resetpw.fxml");
      } else { // This is what would happen if they entered the right password. Lets assume if
               // they dont enter "p" (default) its the right password.
         changeScene(e, "mainmenu.fxml");
      }
   }

   /**
    * Change the scene
    * @param e Event where we can get the stage from
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
   
   /**
    * Event handler for the Login button*
    * 
    * @param e event
    */
   public void resetButtonPressed(Event e) {
      System.out.println("Reset button pressed");
   }
}