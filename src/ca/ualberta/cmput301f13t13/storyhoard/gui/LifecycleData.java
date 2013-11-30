/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.ualberta.cmput301f13t13.storyhoard.gui;

import java.util.ArrayList;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

/**
 * This is a singleton class for the gui, designed to hold
 * state values between activities. This is  an alternative
 * to passing objects along with intents.
 * 
 * This was a refactoring change from passing objects with intents.
 * 
 * @author alexanderwong
 *
 */
public class LifecycleData {
	private boolean isEditing;
	private boolean isEditingChoice;
	private boolean firstStory;
	private ArrayList<Story> searchResults;
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

	public ArrayList<Story> getSearchResults() {
		return this.searchResults;
	}
	
	public void setSearchResults(ArrayList<Story> stories) {
		searchResults = stories;
	}
}