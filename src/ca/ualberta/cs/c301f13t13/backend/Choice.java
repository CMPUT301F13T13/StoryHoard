package ca.ualberta.cs.c301f13t13.backend;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.ChoiceTable;

public class Choice implements Serializable{
	private UUID id;
	private UUID storyId;
	private UUID chapterIdFrom;
	private UUID chapterIdTo;
	private String text;
	private int choiceNum;
	/**
	 * Initializes a new choice object with an UUid id.
	 * 
	 * @param id
	 * @param storyId
	 * @param chapterIdFrom
	 * @param chapterIdTo
	 * @param text
	 * @param choiceNum
	 */

	public Choice(UUID id, UUID storyId, UUID chapterIdFrom, UUID chapterIdTo, String text, int choiceNum) {
		this.id = id;
		this.storyId = storyId;
		this.chapterIdFrom = chapterIdFrom;
		this.chapterIdTo = chapterIdTo;
		this.text = text;
		this.choiceNum= choiceNum;		
	}
	/**
	 * Initializes a new choice object with no id.
	 * 
	 * @param id
	 * @param text
	 * @param storyid
	 */
	public Choice(UUID storyId, UUID chapterIdFrom, UUID chapterIdTo, String text, int choiceNum) {
		this.id = UUID.randomUUID();
		this.storyId = storyId;
		this.chapterIdFrom = chapterIdFrom;
		this.chapterIdTo = chapterIdTo;
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
	public void setChapterIdFrom(String chapterIdFrom) {
		this.chapterIdFrom =UUID.fromString(chapterIdFrom);
	}
	/**
	 * Sets the chapterIdTo of the choice.
	 * @param chapterIdTo
	 */
	public void setChapterIdTo(String chapterIdTo) {
		this.chapterIdTo =UUID.fromString(chapterIdTo);
	}
	/**
	 * Sets the text of the choice.
	 * @param text
	 */
	public void setText(String text) {
		this.text=text;
	}
	/**
	 * Sets the choiceNum of the choice.
	 * @param ChoiceNum
	 */
	public void setChoiceNum(int choiceNum) {
		this.choiceNum=choiceNum;
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
	public UUID getChapterIdFrom() {
		return this.chapterIdFrom;
	}
	/**
	 * Returns the chapterIdTo of the choice.
	 * @return
	 */
	public UUID getChapterIdTo() {
		return this.chapterIdTo;
	}
	/**
	 * Returns the Id of the choice.
	 * @return
	 */
	public String getText() {
		return this.text;
	}
	/**
	 * Returns the Id of the choice.
	 * @return
	 */
	public int getChoiceNum() {
		return this.choiceNum;
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
		info.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, chapterIdFrom.toString());
		info.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, chapterIdTo.toString());
		info.put(ChoiceTable.COLUMN_NAME_TEXT, text);
		info.put(ChoiceTable.COLUMN_NAME_CHOICE_NUMBER, Integer.toString(choiceNum));
		
		return info;
	}
	
	@Override
	public String toString() {
		return "Choice [id=" + id + ", Story=" + storyId + ", Current Chapter=" + chapterIdFrom 
				+ ", Next Chapter=" + chapterIdTo + ", Text=" + text + ", Choice Number=" + choiceNum + "]";
	}
}
