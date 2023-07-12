package application;

import java.sql.*;

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
         sql = "CREATE TABLE IF NOT EXISTS entries (id INTEGER PRIMARY KEY, title TEXT, content TEXT, date INTEGER);";
         stmt.executeUpdate(sql);

         stmt.close();
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }

      return c;

   }
}