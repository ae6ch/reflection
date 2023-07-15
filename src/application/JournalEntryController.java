package application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

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
   
	@FXML
	private SceneController control = new SceneController();

   Database db;

   static JournalEntry entryToEdit = null;

   public JournalEntryController() {
      System.out.println("JournalEntryController constructor called");
      db = new Database();

   }

   public void initialize() {
      System.out.println("JournalEntryController initialize called");
      if (entryToEdit != null) { // we are in edit mode
         System.out.println("entryToEdit is not null is " + entryToEdit.getId());
         titleField.setText(entryToEdit.getTitle());
         bodyField.setHtmlText(entryToEdit.getContent());
         System.out.printf(entryToEdit.getDate());
         try {
            dateStamp.setValue(LocalDate.parse(entryToEdit.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            timeStamp.setValue(LocalTime.parse(entryToEdit.getDate(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
         } catch (DateTimeParseException dtpe) {
            System.out.println("DateTimeParseException caught");
            System.out.println(dtpe.getMessage());
         }

      } else { // new entry
         dateStamp.setValue(LocalDate.now());
         timeStamp.setValue(LocalTime.now());
      }

   }

   /**
    * Set the entry to edit next time this controller is called
    * 
    * @param entry entry to edit
    */
   public static void setEntryToEdit(JournalEntry entry) {
      entryToEdit = entry;
   }

   /**
    * Event handler for any buttons
    * 
    * @param e event
    */
   public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "save":
        	 if (bodyField.getHtmlText().length() > 72) {
        		 storeEntry();
        		 entryToEdit = null; // Clear entryToEdit
        		 control.changeScene(e, "mainmenu.fxml");
        	 }
        	 else {
        		 System.out.println("Please enter journal content");
        	 }
            break;

         case "cancel": // Change Password Button
            entryToEdit = null; // Clear entryToEdit
            control.changeScene(e, "mainmenu.fxml");
            break;

         default:
            System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
            break;

      }
   }

   private void storeEntry() {
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
      if (entryToEdit != null) // Update existing entry - Should be an upsert for this entire block
    	  db.updateEntry(entryToEdit.getId(), title, body, timeInSeconds);
      else
    	  db.insertEntry(title, body, timeInSeconds);
   }
}