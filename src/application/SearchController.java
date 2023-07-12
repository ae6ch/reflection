package application;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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

   Database db; // TODO: all the database stuff should be in the database class?

   public SearchController() {
      System.out.println("SearchController constructor called");
      db = new Database();
   }

   /**
    * Event handler for any buttons
    * 
    * @param e event
    */
   public void buttonPressed(Event e) {
      switch (((Control) e.getSource()).getId()) {
         case "search": // search
            resultsList.getItems().setAll(search(fromDate.getValue().atStartOfDay(),
                  toDate.getValue().atTime(23, 59, 59), textSearch.getText()));
            break;
         case "cancel": // cancel
            changeScene(e, "mainmenu.fxml");
            break;

         default:
            System.out.printf("unknown event: %s\n", ((Control) e.getSource()).getId());
            break;

      }
   }

   /**
    * Search the database for entries matching the search criteria
    * TODO: move this to a Journal class?

    * @param searchFromDateTime start date/time of search
    * @param searchToDateTime   end date/time of search
    * @param searchText         text to search for
    * @return ArrayList of JournalEntry objects
    */
   private ArrayList<JournalEntry> search(LocalDateTime searchFromDateTime, LocalDateTime searchToDateTime,
         String searchText) {
      ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>(); // Array to hold journal entries

      dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
      titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
      resultsList.getItems().clear();
      try {
         PreparedStatement pstmt = db.getConnection()
               .prepareStatement(
                     "SELECT id,title,content,date FROM entries WHERE date BETWEEN ? AND ? AND content LIKE ?");
         pstmt.setLong(1, searchFromDateTime.toEpochSecond(ZoneOffset.UTC));
         pstmt.setLong(2, searchToDateTime.toEpochSecond(ZoneOffset.UTC));
         pstmt.setString(3, "%" + searchText + "%");
         ResultSet rs = pstmt.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            LocalDateTime date = LocalDateTime.ofEpochSecond(rs.getLong("date"), 0, ZoneOffset.UTC);
            String title = rs.getString("title");
            String content = rs.getString("content");
            entries.add(new JournalEntry(id, date.toString(), title, content)); // Fill the array we will use for the
                                                                                // table
         }
         pstmt.close();

      } catch (SQLException sqle) {
         System.out.println("SQLException: " + sqle.getMessage());
         System.out.println("SQLState: " + sqle.getSQLState());
         System.out.println("VendorError: " + sqle.getErrorCode());
      }
      return entries;
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