package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.UUID;

public class LifecycleData {
	private boolean isEditing;
	private boolean firstStory;
	private Story story;
	private Chapter chapter;
	private ObjectType storyType;
	private static LifecycleData self = null;
	
	protected LifecycleData() {
	}
	
	public static LifecycleData getInstance() {
		if (self == null) {
			self = new LifecycleData();
		}
		return self;
	}

	
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

	public ObjectType getStoryType() {
		return storyType;
	}

	public void setStoryType(ObjectType storyType) {
		this.storyType = storyType;
	}
	
	public UUID getChapterID() {
		return this.chapter.getId();
	}
	
	public UUID getStoryID() {
		return this.story.getId();
	}
}