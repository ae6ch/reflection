package application;

import java.sql.*;

/**
 * Database class
 * 
 */
public class Database {

	private static Connection dbConnection = null;
	private static Database db = new Database();

	/**
	 * Constructor
	 * 
	 */
	private Database() {
		dbConnection = databaseOpen("reflection.db");
	}

	/**
	 * return the database object in db
	 * @return Database
	 */
	public static Database getDatabase() {
		return db;
	}

	/**
	 * Get the database connection
	 * @return Connection
	 */

	public static Connection getConnection() {
		return dbConnection;
	}

	/**
	 * Open the database
	 * 
	 * @param dbFile database file for sqlite
	 * @return Connection
	 */
	private Connection databaseOpen(String dbFile) {
		Connection c = null;
		SqlDal sqlCommand = new SqlDal();

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
		} catch (Exception e) {
			//System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		//System.out.println("Opened database successfully");
		sqlCommand.createTables(c);

		return c;

	}
}