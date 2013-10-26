/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.UUID;

/**
 * @author Ashley Brown, Stephanie Gil
 *
 */
public class Chapter {

	private UUID id;
	private UUID storyId;
	
	protected Chapter() {
		id = null;
	}
	
	public Chapter(UUID storyId) {
		this.storyId = storyId;
		this.id = UUID.randomUUID();
	}
	
	/**
	 * Initialize a new chapter from databse info.
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public Chapter(String string, String string2, String string3) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Returns the Id of the chapter.
	 * @return
	 */
	public UUID getId() {
		return this.id;
	}	
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public void setStoryId(UUID id) {
		this.storyId = id;
	}

	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStoryId() {
		// TODO Auto-generated method stub
		return null;
	}	
}
