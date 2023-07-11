package application;

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
   //private Stage stage;
   private Scene scene;
   private Parent root;
   @FXML
   TextField passwordField;

   public void loginButtonPressed(Event e) {
      System.out.printf("Login button pressed, with Password of %s\n", passwordField.getText());
      if (passwordField.getText().equals("p")) { // Hard Coded check for now to see if the password is the default
         System.out.println("Entered the default password, switch to resetpw");

         try {
                  System.out.printf("Switching resetpw\n");;

            Parent root = FXMLLoader.load(getClass().getResource("resetpw.fxml"));
            Scene scene = new Scene(root, 640, 480);
            
         Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
         stage.setScene(scene);
        


         } catch (Exception ex) {

         }

      }
   }

   public void resetButtonPressed(Event e) {
      System.out.println("Reset button pressed");
   }
}