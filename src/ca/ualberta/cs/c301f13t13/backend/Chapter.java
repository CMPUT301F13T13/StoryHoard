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
	
	Chapter() {
		id = null;
	}
	
	Chapter(UUID storyId) {
		this.storyId = storyId;
		this.id = UUID.randomUUID();
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
}
