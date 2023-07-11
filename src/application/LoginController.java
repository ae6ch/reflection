package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * javafx controller for for login.fxml
 * 
 * @author Steve Rubin
 */
public class LoginController {
   @FXML
   TextField passwordField;

   public void loginButtonPressed(Event e)  {
      System.out.printf("Login button pressed, with Password of %s\n", passwordField.getText());
      if (passwordField.getText().equals("p")) { // Hard Coded check for now to see if the password is the default
         System.out.println("Entered the default password, switch to resetpw");
      /*
       * Need to switch to the resetpw.fxml file here?
       */
      }
   }

   public void resetButtonPressed(Event e) {
      System.out.println("Reset button pressed");
   }
}