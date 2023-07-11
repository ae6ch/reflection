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
 * javafx controller for for mainmenu.fxml
 * 
 * @author Steve Rubin
 */
public class MenuController {
   /** 
    * Event handler for any buttons
    * 
    * @param e event
    */
   public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "quit":  // Quit Button
            System.out.println("quit!");       
            System.exit(1);
         case "search": // Search Button
            System.out.println("search");
            changeScene(e, "search.fxml");
            break;
         case "newJournalEntry": // New Journal Entry Button
            System.out.println("newjournalentry");
            changeScene(e, "journalentry.fxml");
            break;
         case "changePassword": // Change Password Button
            System.out.println("changepw");
            changeScene(e, "resetpw.fxml");
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