package application;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * JournalEntry class
 * 
 * Contains a single journal entry (stored in the enries table)
 * 
 */
public class JournalEntry {
	private int id;
	private String date;
	private String title;
	private String content;

	/**
	 * Constructor for JournalEntry
	 *
	 * @param id      id of the entry in database
	 * @param date    date of the entry
	 * @param title   title of the entry
	 * @param content content of the entry
	 */
	public JournalEntry(int id, String date, String title, String content) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.content = content;
	}

	/**
	 * Get the id of the entry
	 * 
	 * @return int id of entry
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the date of the entry
	 * 
	 * @return String date of entry
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Set the date of the entry
	 * 
	 * @param date String date of entry
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Get the title of the entry
	 * 
	 * @return String title of entry
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the title of the entry
	 * 
	 * @param title String title of entry
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set the content of the entry
	 * 
	 * @param content String content of entry
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get the content of the entry
	 * 
	 * @return String content of entry
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Get the time in seconds of the entry
	 * 
	 * @return long time in seconds of entry (epoch time)
	 */
	public long getTimeInSeconds() {
		return LocalDateTime.parse(date).toEpochSecond(ZoneOffset.UTC);
	}

}