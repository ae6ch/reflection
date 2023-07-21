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
		dal sqlCommand = new dal();
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Opened database successfully");
		sqlCommand.createTables(c);

		return c;

	}
}