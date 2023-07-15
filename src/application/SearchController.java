package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
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
   private TableView<JournalEntry> resultsList;
   @FXML
   TableColumn<JournalEntry, String> dateCol;
   @FXML
   TableColumn<JournalEntry, String> titleCol;
   
	@FXML
	private SceneController control = new SceneController();

   Database db; // TODO: all the database stuff should be in the database class?

   public SearchController() {
      System.out.println("SearchController constructor called");
      db = new Database();
   }

   public void initialize() {
      System.out.println("SearchController initialize called");
//      fromDate.setValue(LocalDate.parse("2023-01-01"));
//      toDate.setValue(LocalDate.now());
//      resultsList.getItems().setAll(search(fromDate.getValue().atStartOfDay(),
//            toDate.getValue().atTime(23, 59, 59), textSearch.getText()));
//
//      resultsList.getItems()
//            .setAll(search(fromDate.getValue().atStartOfDay(), toDate.getValue().atTime(23, 59, 59), ""));
   }

   /**
    * Event handler for any buttons
    * 
    * @param e event
    */
   public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "search": // search
  
            resultsList.getItems().setAll(search(fromDate.getValue(),
                  toDate.getValue(), textSearch.getText()));
            break;
         case "edit": // edit
            System.out.println("edit");
            
            if(resultsList.getSelectionModel().getSelectedItem() != null) {
            	System.out.println("tableView selection: " + resultsList.getSelectionModel().getSelectedItem().getId());

            	JournalEntryController.setEntryToEdit(resultsList.getSelectionModel().getSelectedItem());
            	control.changeScene(e, "journalentry.fxml");
            }

            break;
         case "delete": // delete
            System.out.println("delete");
            
            if(resultsList.getSelectionModel().getSelectedItem() != null) {
            	System.out.println("tableView selection: " + resultsList.getSelectionModel().getSelectedItem().getId());
            	db.deleteEntry(resultsList.getSelectionModel().getSelectedItem().getId());
  
            	resultsList.getItems().remove(resultsList.getSelectionModel().getSelectedItem());
            }

            // delete here
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
    * Search the database for entries matching the search criteria
    * TODO: move this to a Journal class?
    * 
    * @param searchFromDateTime start date/time of search
    * @param searchToDateTime   end date/time of search
    * @param searchText         text to search for
    * @return ArrayList of JournalEntry objects
    */
   private ArrayList<JournalEntry> search(LocalDate searchFromDate, LocalDate searchToDate,
         String searchText) {
      ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>(); // Array to hold journal entries
      ResultSet rs = null;

      dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
      titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
      resultsList.getItems().clear();
      
      
    	  if(!searchText.isEmpty()) {
    		  rs = db.searchEntries(searchFromDate, searchToDate, searchText);
    		  entries = parseResultSet(rs);
    		  System.out.println("search res");
    	  }

    	  else {
    		  System.out.println("Please enter a valid search parameter: must provide a substring");
    	  }

      return entries;
   }
   
   private ArrayList<JournalEntry> parseResultSet(ResultSet rs){
	   
	   ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>(); // Array to hold journal entries
	   
	   try {
		   while (rs != null && rs.next()) {
			   int id = rs.getInt("id");
			   // LocalDateTime date = LocalDateTime.ofEpochSecond(rs.getLong("date"), 0,
			   // ZoneOffset.UTC);
			   LocalDateTime date = LocalDateTime.ofEpochSecond(rs.getLong("date"), 0, ZoneOffset.UTC);
			   String title = rs.getString("title");
			   String content = rs.getString("content");
			   entries.add(new JournalEntry(id, date.toString(), title, content)); // Fill the array we will use for the                                                                      // table
		   }
	   }catch (SQLException sqle) {
	        System.out.println("SQLException: " + sqle.getMessage());
	        System.out.println("SQLState: " + sqle.getSQLState());
	        System.out.println("VendorError: " + sqle.getErrorCode());
	     }
       
       return entries; 
   }
}