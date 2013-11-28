package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

public class LifecycleData {
	private boolean isEditing;
	private boolean isEditingChoice;
	private boolean firstStory;
	private ArrayList<Story> storyList;
	private static LifecycleData self = null;
	
	protected LifecycleData() {
	}
	
	public static LifecycleData getInstance() {
		if (self == null) {
			self = new LifecycleData();
		}
		return self;
	}
	
	
	public boolean isEditingChoice() {
		return isEditingChoice;
	}
	
	public void setEditingChoice(boolean editingChoice) {
		this.isEditingChoice = editingChoice;
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

	public ArrayList<Story> getStoryList() {
		return this.storyList;
	}
	public void setSearchResults(ArrayList<Story> stories) {
		storyList = stories;
	}
}