package ca.ualberta.cs.c301f13t13.backend;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.ChoiceTable;

public class Choice implements Serializable{
	private UUID id;
	private UUID storyId;
	private UUID currentChapter;
	private UUID nextChapter;
	private String text;
	/**
	 * Initializes a new choice object with an UUid id (needed for making a
	 * new choice after retrieving data from the database).
	 * 
	 * @param id
	 * @param storyId
	 * @param chapterIdFrom
	 * @param chapterIdTo
	 * @param text
	 */

	public Choice(UUID id, UUID storyId, UUID chapterIdFrom, UUID chapterIdTo, String text) {
		this.id = id;
		this.storyId = storyId;
		this.currentChapter = chapterIdFrom;
		this.nextChapter = chapterIdTo;
		this.text = text;	
	}
	/**
	 * Initializes a new choice object with no id.
	 * 
	 * @param id
	 * @param text
	 * @param storyid
	 */
	public Choice(UUID storyId, UUID chapterIdFrom, UUID chapterIdTo, String text) {
		this.id = UUID.randomUUID();
		this.storyId = storyId;
		this.currentChapter = chapterIdFrom;
		this.nextChapter = chapterIdTo;
		this.text = text;
	}
	// SETTERS
	/**
	 * Set the Id of the choice.
	 * @param id
	 */
	public void setId(String id) {
		 this.id =UUID.fromString(id);
	}
	/**
	 * Sets the storyId of the choice.
	 * @param storyId
	 */
	public void setStoryId(String storyId) {
		 this.storyId =UUID.fromString(storyId);
	}
	/**
	 * Sets the chapterIdFrom of the choice.
	 * @param chapterIdFrom
	 */
	public void setCurrentChapter(String chapterIdFrom) {
		this.currentChapter =UUID.fromString(chapterIdFrom);
	}
	/**
	 * Sets the chapterIdTo of the choice.
	 * @param chapterIdTo
	 */
	public void setNextChapter(String chapterIdTo) {
		this.nextChapter =UUID.fromString(chapterIdTo);
	}
	/**
	 * Sets the text of the choice.
	 * @param text
	 */
	public void setText(String text) {
		this.text=text;
	}
	
	// GETTERS
	/**
	 * Returns the Id of the choice.
	 * @return
	 */
	public UUID getId() {
		return this.id;
	}
	/**
	 * Returns the storyId of the choice.
	 * @return
	 */
	public UUID getStoryId() {
		return this.storyId;
	}
	/**
	 * Returns the chapterIdFrom of the choice.
	 * @return
	 */
	public UUID getCurrentChapter() {
		return this.currentChapter;
	}
	/**
	 * Returns the chapterIdTo of the choice.
	 * @return
	 */
	public UUID getNextChapter() {
		return this.nextChapter;
	}
	/**
	 * Returns the text of the choice.
	 * @return
	 */
	public String getText() {
		return this.text;
	}
	
	/**
	 * Returns the information of the choice (id, storyId, chapterIdFrom, etc..)
	 * in a HashMap.
	 * 
	 * @return HashMap
	 */
	public HashMap<String,String> getSearchCriteria() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		info.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, id.toString());
		info.put(ChoiceTable.COLUMN_NAME_STORY_ID, storyId.toString());
		info.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, currentChapter.toString());
		info.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, nextChapter.toString());
		info.put(ChoiceTable.COLUMN_NAME_TEXT, text);

		
		return info;
	}
	
	@Override
	public String toString() {
		return "Choice [id=" + id + ", Story=" + storyId + ", Current Chapter=" + currentChapter 
				+ ", Next Chapter=" + nextChapter + ", Text=" + text + "]";
	}
}
