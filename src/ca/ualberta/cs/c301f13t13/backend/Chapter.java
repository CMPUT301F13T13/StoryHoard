/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.io.Serializable;
import java.net.URI;
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
	private HashMap<UUID, Choice> choices;
	private HashMap<UUID, URI> illustrations;
	private HashMap<UUID, URI> photos;
	/**
	 * Initializes a new chapter object with a UUID id.
	 * 
	 * @param id
	 * @param text
	 * @param storyId
	 */

	public Chapter(UUID id, UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = id;
		choices = new HashMap<UUID, Choice>();
		illustrations = new HashMap<UUID, URI>();
		photos = new HashMap<UUID, URI>();
	}
	
	/**
	 * Initializes a new chapter object with no id.
	 * 
	 * @param id
	 * @param text
	 * @param storyid
	 */
	public Chapter(UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = UUID.randomUUID();
		choices = new HashMap<UUID, Choice>();
		illustrations = new HashMap<UUID, URI>();
		photos = new HashMap<UUID, URI>();
	}
	
	
	/**
	 * Initialize a new chapter from database info.
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public Chapter(String id, String text, String storyId) {
		this.id=  UUID.fromString(id);
		this.text= text;
		this.storyId=UUID.fromString(storyId);
		illustrations = new HashMap<UUID, URI>();
		photos = new HashMap<UUID, URI>();
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
	public HashMap<UUID, Choice> getChoice() {
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
	public void  setChoice(HashMap<UUID, Choice> choice) {
		this.choices = choice;
	}
	
	public void addChoice(Choice c) {
		choices.put(c.getId(), c);
	}

	@Override
	public String toString() {
		return "Chapter [id=" + id + ", storyId=" + storyId + ", text=" + text + "]";
		
	}

	public HashMap<String, String> getSearchCriteria() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		info.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, id.toString());
		info.put(ChapterTable.COLUMN_NAME_STORY_ID, storyId.toString());
		info.put(ChapterTable.COLUMN_NAME_TEXT, text);
		
		return info;
	}	
}
