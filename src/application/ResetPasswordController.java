package application;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 * javafx controller for for resetpw.fxml
 * 
 * @author Steve Rubin
 */
public class ResetPasswordController {

   /** 
    * Event handler for any buttons
    * 
    * @param e event
    */
 public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "changePasswordButton": // Change Password Button
            System.out.println("changepw");
            changeScene(e, "login.fxml");
            break;
         default:
            System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
            break;

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
}