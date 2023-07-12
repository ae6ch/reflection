package application;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

/**
 * javafx controller for for journalentry.fxml
 * 
 * @author Steve Rubin
 */
public class JournalEntryController {
   @FXML
   private TextField titleField;
   @FXML
   private HTMLEditor bodyField;
   @FXML
   private JFXDatePicker dateStamp;
   @FXML
   private JFXTimePicker timeStamp;

   Database db;

   public JournalEntryController() {
      System.out.println("JournalEntryController constructor called");
      db = new Database();
   }

   /**
    * Event handler for any buttons
    * 
    * @param e event
    */
   public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "save": // Save (with fall thru to cancel so it returns to mainmenu?)
            // Do the stuff here to save
            // Get the values from the fields
            String title = titleField.getText();
            String body = bodyField.getHtmlText();
            LocalDate date = dateStamp.getValue();
            LocalTime time = timeStamp.getValue();
            LocalDateTime localDateTime = LocalDateTime.of(date, time);
            System.out.println("Title: " + title);
            System.out.println("Body: " + body);
            long timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);

            System.out.println("DateTime: " + timeInSeconds);

            // Save the values to the database
            try {
               PreparedStatement pstmt = null;
               pstmt = db.getConnection()
                     .prepareStatement("INSERT INTO entries (title, content, date) VALUES (?, ?, ?)");

               pstmt.setString(1, title);
               pstmt.setString(2, body);
               pstmt.setLong(3, timeInSeconds);
               pstmt.executeUpdate();
               pstmt.close();

            } catch (SQLException sqle) {
               System.out.println("SQLException: " + sqle.getMessage());
            } finally {
            }

         case "cancel": // Change Password Button
            changeScene(e, "mainmenu.fxml");
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