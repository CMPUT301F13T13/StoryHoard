/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

/**
 * @author sgil
 *
 */
public class Story {
	private String id;
	private String author;
	private String title;
	private String description;
	//private ArrayList<Chapter> chapters;
	
	/**
	 * Initializes a new story object
	 * 
	 * @param id
	 * @param author
	 * @param title
	 * @param description
	 */
	public Story(String id, String author, String title, String description) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.description = description;
	}
	
	/**
	 * Returns the Id of the story.
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Returns the title of the story.
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public String toString() {
		return "Story [id=" + id + ", author=" + author + ", title=" + title 
				+ ", description=" + description + "]";
	}
}
