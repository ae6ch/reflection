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

public class dal {
	
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
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	void storepassword(String newPasswordField, String securityQuestionField, String securityAnswerField) {
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
			System.out.println("SQL Exception: " + sqle.getMessage());
		}
	}

	String selectConfigValue(String key) {

		String value = null;
		String sqlstmt = "SELECT value FROM config WHERE key == ?";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setString(1, key);
			ResultSet rs = pstmt.executeQuery();
			value = selectConfigValueHelper(rs);

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("SQLState: " + sqle.getSQLState());
			System.out.println("VendorError: " + sqle.getErrorCode());
		}

		return value;
	}

	private String selectConfigValueHelper(ResultSet rs) throws SQLException {

		String value = null;

		if (rs.next())
			value = rs.getString("value");

		return value;
	}

	void insertEntry(String title, String body, long timeInSeconds) {

		String sqlstmt = "INSERT INTO entries (title, content, date) VALUES (?, ?, ?)";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setString(1, title);
			pstmt.setString(2, body);
			pstmt.setLong(3, timeInSeconds);
			pstmt.executeUpdate();

		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		}
	}

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
			System.out.println("SQLException: " + sqle.getMessage());
		}
	}

	void deleteEntry(int id) {

		String sqlstmt = "DELETE from entries where id = ?";

		try {
			PreparedStatement pstmt = Database.getConnection().prepareStatement(sqlstmt);
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException sqle) {
			System.err.println(sqle.getClass().getName() + ": " + sqle.getMessage());
			System.exit(0);
		}
	}

	ArrayList<JournalEntry> searchEntries(LocalDate searchFromDate, LocalDate searchToDate, String searchText) {

		ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>(); // Array to hold journal entries
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
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("SQLState: " + sqle.getSQLState());
			System.out.println("VendorError: " + sqle.getErrorCode());
		}
		return entries;
	}

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
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("SQLState: " + sqle.getSQLState());
			System.out.println("VendorError: " + sqle.getErrorCode());
		}

		return entries;
	}

}
