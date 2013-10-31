/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.ChapterTable;

/**
 * @author Ashley Brown, Stephanie Gil
 *
 */
//TODO must deal with photos in chapter still
public class Chapter implements Serializable {

	private UUID id;
	private UUID storyId;
	private String text;
	private ArrayList<Choice> choices;
	private ArrayList<URI> illustrations;
	private ArrayList<URI> photos;
	
	/**
	 * Initializes a new chapter object with no id.
	 * 
	 * @param storyid
	 * @param text
	 */
	public Chapter(UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = UUID.randomUUID();
		choices = new ArrayList<Choice>();
		illustrations = new ArrayList<URI>();
		photos = new ArrayList<URI>();
	}
	
	
	/**
	 * Initialize a new chapter with an id. Can also be used to make
	 * a chapter from the chapter information retrieved from
	 * the database.
	 * 
	 * @param id
	 * @param storyId 
	 * @param text
	 */
	public Chapter(UUID id, UUID storyId, String text) {
		this.id = id;
		this.text = text;
		this.storyId = storyId;
		illustrations = new ArrayList<URI>();
		photos = new ArrayList<URI>();
	}
	//Getters
	
	/**
	 * Returns the Id of the chapter.
	 * @return
	 */
	public UUID getId() {
		return this.id;
	}	
	
	/**
	 * Returns the story Id of the chapter.
	 * @return
	 */
	public UUID getStoryId() {
		return this.storyId;
	}
	/**
	 * Returns the text of the chapter.
	 * @return
	 */
	public String getText() {
	
		return this.text;
	}
	/**
	 * Returns the choices of the chapter.
	 * @return
	 */
	public ArrayList<Choice> getChoices() {
		return this.choices;
	}
	
	//Setters
	
	/**
	 * Sets the Id of the chapter.
	 * @param id
	 */
	public void setId(UUID id) {
		this.id = id;
	}
	/**
	 * Sets the story Id of the chapter.
	 * @param story id
	 */
	public void setStoryId(UUID id) {
		this.storyId = id;
	}
	/**
	 * Sets the text of the chapter.
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * Sets the text of the chapter.
	 * @param text
	 */
	public void  setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}
	
	public void addChoice(Choice c) {
		choices.add(c);
	}

	@Override
	public String toString() {
		return "Chapter [id=" + id + ", storyId=" + storyId + ", text=" + text + "]";
		
	}

	public HashMap<String, String> getSearchCriteria() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		if (id != null) {
			info.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, id.toString());
		} else {
			info.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, "");
		} 
		if (storyId != null) {
			info.put(ChapterTable.COLUMN_NAME_STORY_ID, storyId.toString());
		} else {
			info.put(ChapterTable.COLUMN_NAME_STORY_ID, "");
		}
		info.put(ChapterTable.COLUMN_NAME_TEXT, text);
		
		return info;
	}	
}
