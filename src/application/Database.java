package application;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * Database class
 * TODO: move lots of duplicated stuff here
 * 
 * @author Steve Rubin
 */
public class Database {
   Connection db = null;

   public Database() {
      db = databaseOpen("reflection.db");
   }

   public Connection getConnection() {
      return db;
   }

   private Connection databaseOpen(String dbFile) {
      Connection c = null;
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
      System.out.println("Opened database successfully");

      // config table is a key value store. It is used to store our settings (user
      // password, etc). After creating the config table, add a entry called password
      // with the value of "p", and another entry called "firstlaunch" with a value of
      // 1.
      // This will be used to check if the user has launched the program before.
      try {
         Statement stmt = c.createStatement();
         String sql = "CREATE TABLE IF NOT EXISTS config (key TEXT PRIMARY KEY, value TEXT);";
         stmt.executeUpdate(sql);
         sql = "INSERT OR IGNORE INTO config (key, value) VALUES ('password', 'p');";
         stmt.executeUpdate(sql);
         sql = "INSERT OR IGNORE INTO config (key, value) VALUES ('firstlaunch', '1');";
         stmt.executeUpdate(sql);
         sql = "CREATE TABLE IF NOT EXISTS entries (id INTEGER PRIMARY KEY, title TEXT NULL, content TEXT NOT NULL, date INTEGER NOT NULL);";
         stmt.executeUpdate(sql);

         stmt.close();
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }

      return c;

   }
   void insertEntry(String title, String body, long timeInSeconds) {
	   try {
		   PreparedStatement pstmt = 
				   db.prepareStatement("INSERT INTO entries (title, content, date) VALUES (?, ?, ?)");
		   pstmt.setString(1, title);
		   pstmt.setString(2, body);
		   pstmt.setLong(3, timeInSeconds);
		   pstmt.executeUpdate();
		   
	   }catch (SQLException sqle) {
		   System.out.println("SQLException: " + sqle.getMessage());
		   }   
   }
   void updateEntry(int id, String title, String body, long timeInSeconds) {
	   try {
		   PreparedStatement pstmt = 
				   db.prepareStatement("UPDATE entries SET title = ?, content = ?, date = ? WHERE id = ?");
		   pstmt.setString(1, title);
		   pstmt.setString(2, body);
		   pstmt.setLong(3, timeInSeconds);
		   pstmt.setInt(4, id);
		   pstmt.executeUpdate();
		   
	   }catch (SQLException sqle) {
		   System.out.println("SQLException: " + sqle.getMessage());
		   }   
   }
   void deleteEntry(int id) {
	   try {
		   PreparedStatement pstmt = 
    			db.prepareStatement("DELETE from entries where id = ?");
		   pstmt.setInt(1, id);
		   pstmt.executeUpdate();
	   } catch (SQLException sqle) {
		   System.err.println(sqle.getClass().getName() + ": " + sqle.getMessage());
		   System.exit(0);
	   }
   }
   
   ResultSet searchEntries(LocalDate searchFromDate, LocalDate searchToDate,
		   String searchText) {
   
   ResultSet rs = null;
   
   	try {
   		PreparedStatement pstmt = 
   				db.prepareStatement(
   						"SELECT id,title,content,date FROM entries WHERE date BETWEEN ? AND ? OR (content LIKE ? OR title LIKE ?) ");
   		
   		//check whether there is selected date range for the search
   		if (searchFromDate != null && searchToDate != null) {
   			pstmt.setLong(1, searchFromDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC));
   			pstmt.setLong(2, searchToDate.atTime(23, 59, 59).toEpochSecond(ZoneOffset.UTC));
   		}
   		//searches title and content for substring
   		pstmt.setString(3, "%" + searchText + "%");
   		pstmt.setString(4, "%" + searchText + "%");
   		rs = pstmt.executeQuery();
   		
   	}catch (SQLException sqle) {
        System.out.println("SQLException: " + sqle.getMessage());
        System.out.println("SQLState: " + sqle.getSQLState());
        System.out.println("VendorError: " + sqle.getErrorCode());
     }
   	
   	System.out.println("im here");
   	return rs;
  }
}