package application;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * JournalEntry class
 * 
 * Contains a single journal entry (stored in the enries table)
 * 
 * @author Steve Rubin
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

	public int getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public long getTimeInSeconds() {
		return LocalDateTime.parse(date).toEpochSecond(ZoneOffset.UTC);
	}

}