package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;

/**
 * javafx controller for for mainmenu.fxml
 * 
 */
public class MenuController {

	@FXML
	private SceneController control = new SceneController();

	/**
	 * Event handler for any buttons
	 * 
	 * @param e event
	 */
	public void buttonPressed(Event e) {
		switch (((Control) e.getSource()).getId()) {

			case "quit": // Quit Button
				System.exit(1);
				break; // not reached, but linter complains

			case "logout": // logout Button
				control.changeScene(e, "login.fxml");
				break;

			case "search": // Search Button
				control.changeScene(e, "search.fxml");
				break;

			case "newJournalEntry": // New Journal Entry Button
				control.changeScene(e, "journalentry.fxml");
				break;

			case "changePassword": // Change Password Button
				control.changeScene(e, "resetpw.fxml");
				break;

			default:
				System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
				break;

		}
	}
}