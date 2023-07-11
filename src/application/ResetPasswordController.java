package application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * javafx controller for for resetpw.fxml
 * 
 * @author Steve Rubin
 */
public class ResetPasswordController {

   public void changePasswordButtonPressed(Event e) {
      System.out.println("Change Password button pressed");

 try {
                  System.out.printf("Switching resetpw\n");;

            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Scene scene = new Scene(root, 640, 480);
            
         Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
         stage.setScene(scene);
        


         } catch (Exception ex) {

         }
      

   }
}