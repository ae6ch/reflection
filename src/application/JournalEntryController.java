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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

/**
 * javafx controller for for journalentry.fxml
 * 
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
	private Label errorMessage;

	@FXML
	private SceneController control = new SceneController();

	private SqlDal sqlCommand;

	static JournalEntry entryToEdit = null;

	/**
	 * Constructor
	 */
	public JournalEntryController() {
		sqlCommand = new SqlDal();

	}

	/**
	 * Initialize the controller
	 * 
	 */
	public void initialize() {
		if (entryToEdit != null) { // we are in edit mode
			titleField.setText(entryToEdit.getTitle());
			bodyField.setHtmlText(entryToEdit.getContent());
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
		String inputValue = null;
		
		switch (((Control) e.getSource()).getId()) {
			case "save":
				inputValue = bodyField.getHtmlText().replaceAll("\\<.*?\\>", ""); // Strips out html tags
				
				if (!inputValue.equals("")) { // Verifies that is value for journal content
					storeEntry();
					entryToEdit = null; // Clear entryToEdit
					control.changeScene(e, "mainmenu.fxml");
				}
				else {
					errorMessage.setText("");
					errorMessage.setText("Please enter journal entry content");
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

	/**
	 * Store the entry in the database
	 * TODO: Move globals to parameters
	 */
	private void storeEntry() {
		String title = titleField.getText();
		String body = bodyField.getHtmlText();
		LocalDate date = dateStamp.getValue();
		LocalTime time = timeStamp.getValue();
		LocalDateTime localDateTime = LocalDateTime.of(date, time);

		long timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);


		// Save the values to the database
		if (entryToEdit != null) // Update existing entry - Should be an upsert for this entire block
			sqlCommand.updateEntry(entryToEdit.getId(), title, body, timeInSeconds);
		else
			sqlCommand.insertEntry(title, body, timeInSeconds);
	}
}