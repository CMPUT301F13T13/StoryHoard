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
 *
 * Used the following code as reference:
 * URL: https://github.com/abramhindle/FillerCreepForAndroid/blob/master/src/es/softwareprocess/fillercreep/FillerCreepApplication.java
 * Date: Oct. 25, 2013
 * Author: Abram Hindle
 */

package ca.ualberta.cmput301f13t13.storyhoard.gui;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Application;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;

/**
 * The main StoryHoard Application. 
 * 
 * @author Alex Wong
 * @author Josh Tate
 * @author Kim Wu
 * @author Ashley Brown
 * @author Stephanie Gil
 *
 */
public class StoryHoardApplication extends Application {
	private static Story story;
	private static ArrayList<Story> storyList;
	private static Chapter chapter;
	private static ArrayList<Chapter> chapList;
	private static Choice choice;
	private static ArrayList<Choice> choiceList;
	private UUID storyId;
	private UUID chapterId;
	private UUID choiceId;
	
	// SETTERS
	
	public void setStory(Story story) {
		this.story = story;
	}
	
	public void setStoryList(ArrayList<Story> storyList) {
		this.storyList = storyList;
	}
	
	public void setChapter(Chapter chap) {
		this.chapter = chap;
	}
	
	public void setChapterList(ArrayList<Chapter> chapList) {
		this.chapList = chapList;
	}
	
	public void setChoice(Choice choice) {
		this.choice = choice;
	}
	
	public void setChoiceList(ArrayList<Choice> choiceList) {
		this.choiceList = choiceList;
	}
	
	// GETTERS
	
	public Story getStory() {
		return story;
	}
	
	public ArrayList<Story> getStoryList() {
		return storyList;
	}
	
	public Chapter getChapter() {
		return chapter;
	}
	
	public ArrayList<Chapter> getChapterList() {
		return chapList;
	}
	
	public Choice getChoice() {
		return choice;
	}
	
	public ArrayList<Choice> getChoiceList() {
		return choiceList;
	}
}
