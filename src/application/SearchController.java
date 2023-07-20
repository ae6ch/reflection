package application;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * javafx controller for for search.fxml
 * 
 * @author Steve Rubin
 */
public class SearchController {

	@FXML
	private DatePicker fromDate;
	@FXML
	private DatePicker toDate;
	@FXML
	private TextField textSearch;
	@FXML
	private Label errorMessage;
	@FXML
	private TableView<JournalEntry> resultsList;
	@FXML
	private TableColumn<JournalEntry, String> dateCol;
	@FXML
	private TableColumn<JournalEntry, String> titleCol;

	@FXML
	private SceneController control = new SceneController();

	private dal sqlCommand; // TODO: all the database stuff should be in the database class?

	public SearchController() {
		System.out.println("SearchController constructor called");
		sqlCommand = new dal();
	}

//	public void initialize() {
//		System.out.println("SearchController initialize called");
//	}

	/**
	 * Event handler for any buttons
	 * 
	 * @param e event
	 */
	public void buttonPressed(Event e) {
		switch (((Control) e.getSource()).getId()) {
		
		case "search": // search
			resultsList.getItems().setAll(search(fromDate.getValue(), toDate.getValue(), textSearch.getText()));
			break;
			
		case "clear": // clear search parameters
			textSearch.clear();
			fromDate.getEditor().clear();
			toDate.getEditor().clear();
			resultsList.getItems().clear();
			break;
			
		case "edit": // edit
			System.out.println("edit");

			if (resultsList.getSelectionModel().getSelectedItem() != null) {
				System.out.println("tableView selection: " + resultsList.getSelectionModel().getSelectedItem().getId());

				JournalEntryController.setEntryToEdit(resultsList.getSelectionModel().getSelectedItem());
				control.changeScene(e, "journalentry.fxml");
			}
			break;
			
		case "delete": // delete
			System.out.println("delete");

			if (resultsList.getSelectionModel().getSelectedItem() != null) {
				System.out.println("tableView selection: " + resultsList.getSelectionModel().getSelectedItem().getId());
				sqlCommand.deleteEntry(resultsList.getSelectionModel().getSelectedItem().getId());

				resultsList.getItems().remove(resultsList.getSelectionModel().getSelectedItem());
			}
			break;
			
		case "cancel": // cancel
			System.out.println("cancel");
			JournalEntryController.setEntryToEdit(null);
			control.changeScene(e, "mainmenu.fxml");
			break;
			
		default:
			System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
			break;

		}
	}

	/**
	 * Search the database for entries matching the search criteria TODO: move this
	 * to a Journal class?
	 * 
	 * @param searchFromDateTime start date/time of search
	 * @param searchToDateTime   end date/time of search
	 * @param searchText         text to search for
	 * @return ArrayList of JournalEntry objects
	 */
	private ArrayList<JournalEntry> search(LocalDate searchFromDate, LocalDate searchToDate, String searchText) {
		ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>(); // Array to hold journal entries

		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		resultsList.getItems().clear();

		if (!searchText.isEmpty()) {
			entries = sqlCommand.searchEntries(searchFromDate, searchToDate, searchText);
		}

		else {
			errorMessage.setText("");
			errorMessage.setText("Please enter a valid search parameter: must provide a substring");
		}

		return entries;
	}
}