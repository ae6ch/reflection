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

	private SqlDal sqlCommand;

	/**
	 * 
	 */
	public SearchController() {
		sqlCommand = new SqlDal();
	}

	public void initialize() {
		fromDate.setValue(LocalDate.parse("2023-01-01"));
		toDate.setValue(LocalDate.now());
	}

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
				fromDate.setValue(null);
				toDate.getEditor().clear();
				toDate.setValue(null);
				resultsList.getItems().clear();
				break;

			case "edit": // edit

				if (resultsList.getSelectionModel().getSelectedItem() != null) {

					JournalEntryController.setEntryToEdit(resultsList.getSelectionModel().getSelectedItem());
					control.changeScene(e, "journalentry.fxml");
				} else if (resultsList.getItems().isEmpty()) {
					errorMessage.setText("");
					errorMessage.setText("Please search for vaild entries prior to view/edit");
				} else {
					errorMessage.setText("");
					errorMessage.setText("Please select an entry to view/edit");
				}
				break;

			case "delete": // delete

				if (resultsList.getSelectionModel().getSelectedItem() != null) {

					sqlCommand.deleteEntry(resultsList.getSelectionModel().getSelectedItem().getId());

					resultsList.getItems().remove(resultsList.getSelectionModel().getSelectedItem());
				} else if (resultsList.getItems().isEmpty()) {
					errorMessage.setText("");
					errorMessage.setText("Please search for vaild entries prior to delete");
				} else {
					errorMessage.setText("");
					errorMessage.setText("Please select an entry to delete");
				}

				break;

			case "cancel": // cancel
				JournalEntryController.setEntryToEdit(null);
				control.changeScene(e, "mainmenu.fxml");
				break;

			default:
				break;

		}
	}

	/**
	 * Search the database for entries matching the search criteria
	 * 
	 * 
	 * @param searchFromDateTime start date/time of search
	 * @param searchToDateTime   end date/time of search
	 * @param searchText         text to search for
	 * @return ArrayList of JournalEntry objects
	 */
	private ArrayList<JournalEntry> search(LocalDate searchFromDate, LocalDate searchToDate, String searchText) {
		ArrayList<JournalEntry> entries = new ArrayList<>(); // Array to hold journal entries

		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		resultsList.getItems().clear();

		if (!searchText.isEmpty() || (searchFromDate != null && searchToDate != null)) {
			entries = sqlCommand.searchEntries(searchFromDate, searchToDate, searchText);
		}

		else {
			errorMessage.setText("");
			errorMessage.setText("Please enter a valid search criteria");
		}

		return entries;
	}
}