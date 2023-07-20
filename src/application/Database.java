package application;

import java.sql.*;

/**
 * Database class TODO: move lots of duplicated stuff here
 * 
 * @author Steve Rubin
 */
public class Database {

	private static Connection dbConnection = null;
	private static Database db = new Database();

	private Database() {
		dbConnection = databaseOpen("reflection.db");
	}

	public static Database getDatabase() {
		return db;
	}

	public static Connection getConnection() {
		return dbConnection;
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
			String sql = "CREATE TABLE IF NOT EXISTS config (key TEXT NOT NULL PRIMARY KEY, value TEXT NOT NULL);";
			stmt.executeUpdate(sql);
			sql = "INSERT OR IGNORE INTO config (key, value) VALUES ('password', 'p');";
			stmt.executeUpdate(sql);
//         sql = "INSERT OR IGNORE INTO config (key, value) VALUES ('firstlaunch', '1');";
//         stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS entries (id INTEGER NOT NULL PRIMARY KEY, title TEXT NULL, content TEXT NOT NULL, date INTEGER NOT NULL);";
			stmt.executeUpdate(sql);

			stmt.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return c;

	}
}