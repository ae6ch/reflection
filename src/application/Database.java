package application;

import java.sql.*;

/**
 * Database class
 * 
 */
public class Database {
	public static final String DB_FILE = "reflection.db";
	public static final String DB_URL = "jdbc:sqlite:" + DB_FILE;
	public static final String DRIVER_CLASS = "org.sqlite.JDBC";
	private static Connection dbConnection = null;
	private static Database db = new Database();

	/**
	 * Constructor
	 * 
	 */
	private Database() {
		dbConnection = databaseOpen();
	}

	/**
	 * return the database object in db
	 * 
	 * @return Database
	 */
	public static Database getDatabase() {
		return db;
	}

	/**
	 * Get the database connection
	 * 
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
	private Connection databaseOpen() {
		Connection c = null;
		SqlDal sqlCommand = new SqlDal();

		try {
			Class.forName(DRIVER_CLASS);
			c = DriverManager.getConnection(DB_URL);
		} catch (SQLException sqle) {
			sqlException(sqle);
		} catch (ClassNotFoundException cnfe) { // SQL driver not found
			System.out.println(cnfe.getMessage());
		}
		sqlCommand.createTables(c);

		return c;

	}

	/**
	 * Handle SQL Exceptions in catch blocks
	 * 
	 * @param sqle SQLException
	 */

	public static void sqlException(SQLException sqle) {
		System.out.println("SQLException: " + sqle.getMessage());
		System.out.println("SQLState: " + sqle.getSQLState());
		System.out.println("VendorError: " + sqle.getErrorCode());
		System.exit(1);
	}
}