/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.ChapterTable;

/**
 * @author Ashley Brown, Stephanie Gil
 *
 */
//TODO must deal with illustration in chapter still
public class Chapter {

	private UUID id;
	private UUID storyId;
	private String text;
	private HashMap<UUID, Choice> choice;
	/**
	 * Initializes a new chapter object with an UUid id.
	 * 
	 * @param id
	 * @param text
	 * @param storyId
	 */

	public Chapter(UUID id, UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = id;
		choice = new HashMap<UUID, Choice>();
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
		choice = new HashMap<UUID, Choice>();
	}
	
	/**
	 * For setting search criteriaNew chapter without text
	 * @param storyId
	 */
	public Chapter(UUID storyId) {
		this.text = "";
		this.storyId = storyId;
		this.id = UUID.randomUUID();
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
		
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub
		return this.storyId;
	}
	/**
	 * Returns the text of the chapter.
	 * @return
	 */
	public String getText() {
		// TODO Auto-generated method stub
		return this.text;
	}
	/**
	 * Returns the choices of the chapter.
	 * @return
	 */
	public HashMap<UUID, Choice> getChoice() {
		return this.choice;
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
		this.choice = choice;
	}
	
	public void addChoice(Choice c) {
		choice.put(c.getId(), c);
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
