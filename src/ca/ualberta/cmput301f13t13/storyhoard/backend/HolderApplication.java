package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.UUID;

import android.app.Application;

/**
 * This is a subclass of the application, which allows passing back
 * and forth objects, relying on the fact that Application is persistent
 * throughout the application's life cycle
 * 
 * @author alexanderwong
 *
 */
public class HolderApplication extends Application {
	private boolean isEditing;
	private boolean firstStory;
	private Story story;
	private Chapter chapter;
	
	
	public boolean isEditing() {
		return isEditing;
	}
	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
	public boolean isFirstStory() {
		return firstStory;
	}
	public void setFirstStory(boolean firstStory) {
		this.firstStory = firstStory;
	}
	public Story getStory() {
		return story;
	}
	public void setStory(Story story) {
		this.story = story;
	}
	public Chapter getChapter() {
		return chapter;
	}
	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	/**
	 * Returns the UUID of the chapter
	 * @return UUID
	 */
	public UUID chapterID() {
		UUID chapterID = null;
		if (this.chapter!=null) {
			chapterID = this.chapter.getId();
		}
		return chapterID;
	}
	/**
	 * Returns the UUID of the story.
	 * @return UUID
	 */
	public UUID storyID() {
		UUID storyID = null;
		if (this.story!=null) {
			storyID = this.story.getId();
		}
		return storyID;
	}
}
