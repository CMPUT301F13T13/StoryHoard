package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LifecycleData {
	private boolean isEditing;
	private boolean firstStory;
	private ArrayList<Story> storyList;
	private Story story;
	private Chapter chapter;
	private ObjectType storyType;
	private Media currImage;
	private ArrayList<Media> currentImages;
	private ArrayList<Choice> currentChoices;
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

	public ArrayList<Story> getStoryList() {
		return this.storyList;
	}
	public void setStoryList(ArrayList<Story> stories) {
		storyList = stories;
	}
	public ArrayList<Media> getCurrImages() {
		if (currentImages == null) {
			return new ArrayList<Media>();
		}
		return this.currentImages;
	}
	public void setCurrImages(ArrayList<Media> medias) {
		this.currentImages = medias;
	}
	public void addToCurrImages(Media media) {
		if (currentImages == null) {
			currentImages = new ArrayList<Media>();
		}
		this.currentImages.add(media);
	}
	public ArrayList<Choice> getCurrChoices() {
		if (currentChoices == null) {
			return new ArrayList<Choice>();
		}
		return this.currentChoices;
	}
	public void setCurrChoices(ArrayList<Choice> choices) {
		this.currentChoices = choices;
	}
	public void addToCurrChoices(Choice choice) {
		if (currentChoices == null) {
			currentChoices = new ArrayList<Choice>();
		}
		this.currentChoices.add(choice);
	}	
	public Media getCurrImage() {
		return currImage;
	}
	public void setCurrImage(Media img) {
		this.currImage = img;
	}
}