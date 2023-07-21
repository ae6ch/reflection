package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;

/**
 * javafx controller for for mainmenu.fxml
 * 
 * @author Steve Rubin
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
			System.out.println("quit!");
			System.exit(1);
			
		case "logout": // logout Button
			System.out.println("quit!");
			control.changeScene(e, "login.fxml");
			break;
			
		case "search": // Search Button
			System.out.println("search");
			control.changeScene(e, "search.fxml");
			break;
			
		case "newJournalEntry": // New Journal Entry Button
			System.out.println("newjournalentry");
			control.changeScene(e, "journalentry.fxml");
			break;
			
		case "changePassword": // Change Password Button
			System.out.println("changepw");
			control.changeScene(e, "resetpw.fxml");
			break;
			
		default:
			System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
			break;

		}
	}
}