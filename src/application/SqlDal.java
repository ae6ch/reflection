package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

/**
 * SQL Data Access Layer (DAL) for the journal application. This class handles
 * all
 * database interactions.
 */
public class SqlDal {
	/**
	 * This method creates the tables in the database. It is called when the program
	 * is run for the first time (or if the database is deleted).
	 * 
	 * @param dbConnection The connection to the database.
	 */
	void createTables(Connection dbConnection) {
		// config table is a key value store. It is used to store our settings (user
		// password, etc). After creating the config table, add a entry called password
		// with the value of "p", and another entry called "firstlaunch" with a value of
		// 1.
		// This will be used to check if the user has launched the program before.
		try {

			Statement stmt = dbConnection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS config (key TEXT NOT NULL PRIMARY KEY, value TEXT NOT NULL);";
			stmt.executeUpdate(sql);
			sql = "INSERT OR IGNORE INTO config (key, value) VALUES ('password', 'p');";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE IF NOT EXISTS entries (id INTEGER NOT NULL PRIMARY KEY, title TEXT NULL, content TEXT NOT NULL, date INTEGER NOT NULL);";
			stmt.executeUpdate(sql);

			stmt.close();
		} catch (SQLException sqle) {
			sqlException(sqle);

		}
	}

	/**
	 * UPSERT the password and security question/answer into the config table.
	 * 
	 * @param newPasswordField      new password
	 * @param securityQuestionField Secuturity question
	 * @param securityAnswerField   Security answer
	 */
	void storePassword(String newPasswordField, String securityQuestionField, String securityAnswerField) {
		try {
			String sqlstmt = "INSERT INTO config (key,value) VALUES (?,?) on CONFLICT(key) DO UPDATE SET value=excluded.value";
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setString(1, "password");
			pstmt.setString(2, newPasswordField);
			pstmt.executeUpdate();

			if (!securityQuestionField.isEmpty() && !securityAnswerField.isEmpty()) {
				pstmt.setString(1, "securityquestion");
				pstmt.setString(2, securityQuestionField);
				pstmt.executeUpdate();

				pstmt.setString(1, "securityanswer");
				pstmt.setString(2, securityAnswerField);
				pstmt.executeUpdate();
			}

			pstmt.close();
		} catch (SQLException sqle) {
			sqlException(sqle);
		}
	}

	/**
	 * Get parameters from the key/value store
	 * 
	 * @param key key to search for
	 * @return value from the key/value store
	 */
	String selectConfigValue(String key) {

		String value = null;
		String sqlstmt = "SELECT value FROM config WHERE key == ?";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setString(1, key);
			ResultSet rs = pstmt.executeQuery();
			value = selectConfigValueHelper(rs);

		} catch (SQLException sqle) {
			sqlException(sqle);
		}

		return value;
	}

	/**
	 * Handle SQL Exceptions in catch blocks
	 * 
	 * @param sqle
	 */
	private void sqlException(SQLException sqle) {
		System.out.println("SQLException: " + sqle.getMessage());
		System.out.println("SQLState: " + sqle.getSQLState());
		System.out.println("VendorError: " + sqle.getErrorCode());
		System.exit(1);
	}

	private String selectConfigValueHelper(ResultSet rs) throws SQLException {

		String value = null;

		if (rs.next())
			value = rs.getString("value");

		return value;
	}

	/**
	 * insert a new entry into the journal entry table
	 * 
	 * @param title         Title of the journal entry
	 * @param body          body of the journal entry
	 * @param timeInSeconds time in seconds since epoch
	 */
	void insertEntry(String title, String body, long timeInSeconds) {

		String sqlstmt = "INSERT INTO entries (title, content, date) VALUES (?, ?, ?)";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setString(1, title);
			pstmt.setString(2, body);
			pstmt.setLong(3, timeInSeconds);
			pstmt.executeUpdate();

		} catch (SQLException sqle) {
			sqlException(sqle);
		}
	}

	/**
	 * Update a existing entry in the journal entry table
	 * 
	 * (TODO: convert to UPSERT and merge with insertEntry())
	 * 
	 * @param id            pkey of the entry
	 * @param title         title of the entry
	 * @param body          body of the entry
	 * @param timeInSeconds time in seconds since epoch
	 */
	void updateEntry(int id, String title, String body, long timeInSeconds) {

		String sqlstmt = "UPDATE entries SET title = ?, content = ?, date = ? WHERE id = ?";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setString(1, title);
			pstmt.setString(2, body);
			pstmt.setLong(3, timeInSeconds);
			pstmt.setInt(4, id);
			pstmt.executeUpdate();

		} catch (SQLException sqle) {
			sqlException(sqle);
		}
	}

	/**
	 * Delete journal entry from the journal entry table
	 * 
	 * @param id pkey of the entry
	 */
	void deleteEntry(int id) {

		String sqlstmt = "DELETE from entries where id = ?";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			sqlException(sqle);

		}
	}

	/**
	 * Get all journal entries from the journal entry table using the search
	 * criteria
	 * 
	 * @param searchFromDate start date of the search
	 * @param searchToDate   end date of the search
	 * @param searchText     text to search for
	 * @return ArrayList of journal entries in JournalEntry format
	 */
	ArrayList<JournalEntry> searchEntries(LocalDate searchFromDate, LocalDate searchToDate, String searchText) {

		ArrayList<JournalEntry> entries = new ArrayList<>(); // Array to hold journal entries
		String sqlstmt = "SELECT id,title,content,date FROM entries WHERE date BETWEEN ? AND ? AND (content LIKE ? OR title LIKE ?)";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);

			// check whether there is selected date range for the search
			if (searchFromDate != null && searchToDate != null) {
				pstmt.setLong(1, searchFromDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC));
				pstmt.setLong(2, searchToDate.atTime(23, 59, 59).toEpochSecond(ZoneOffset.UTC));
			} else {
				pstmt.setLong(1, LocalDateTime.MIN.toEpochSecond(ZoneOffset.UTC));
				pstmt.setLong(2, LocalDateTime.MAX.toEpochSecond(ZoneOffset.UTC));
			}
			// searches title and content for substring
			pstmt.setString(3, "%" + searchText + "%");
			pstmt.setString(4, "%" + searchText + "%");
			ResultSet rs = pstmt.executeQuery();
			parseResultSet(rs, entries);

		} catch (SQLException sqle) {
			sqlException(sqle);
		}
		return entries;
	}

	/**
	 * Convert JDBC ResultSet to ArrayList of JournalEntry
	 * 
	 * @param rs      JDBC ResultSet
	 * @param entries ArrayList of JournalEntry
	 * @return ArrayList of JournalEntry
	 */
	private ArrayList<JournalEntry> parseResultSet(ResultSet rs, ArrayList<JournalEntry> entries) {

		try {
			while (rs.next()) {
				int id = rs.getInt("id");
				// LocalDateTime date = LocalDateTime.ofEpochSecond(rs.getLong("date"), 0,
				// ZoneOffset.UTC);
				LocalDateTime date = LocalDateTime.ofEpochSecond(rs.getLong("date"), 0, ZoneOffset.UTC);
				String title = rs.getString("title");
				String content = rs.getString("content");
				entries.add(new JournalEntry(id, date.toString(), title, content)); // Fill the array we will use for
				// the // table
			}
		} catch (SQLException sqle) {
			sqlException(sqle);
		}

		return entries;
	}

}
