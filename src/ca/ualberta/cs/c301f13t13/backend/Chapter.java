/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.ChapterTable;
import ca.ualberta.cs.c301f13t13.backend.DBContract.StoryTable;

/**
 * @author Ashley Brown, Stephanie Gil
 *
 */
public class Chapter {

	private UUID id;
	private UUID storyId;
	private String text;

	public Chapter(UUID id, UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = id;
	}
	
	public Chapter(UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = UUID.randomUUID();
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns the Id of the chapter.
	 * @return
	 */
	public UUID getId() {
		return this.id;
	}	
	
	public UUID getStoryId() {
		// TODO Auto-generated method stub
		return this.storyId;
	}	
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public void setStoryId(UUID id) {
		this.storyId = id;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return this.text;
	}



	public HashMap<String, String> getSearchCriteria() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		info.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, id.toString());
		info.put(ChapterTable.COLUMN_NAME_STORY_ID, storyId.toString());
		info.put(ChapterTable.COLUMN_NAME_TEXT, text);
		
		return info;
	}	
}
