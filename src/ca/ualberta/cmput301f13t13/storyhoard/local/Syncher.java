/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

/**
 * Role: uses all of the manager classes to "sync" stories. For example,  
 * if a user downloads a story from the server, it must be saved into the  
 * database, and all of the images within its chapters must also be saved 
 * onto the SD card. This class takes care of updating story information 
 * locally and does so by using methods from the manager classes. </br><br>
 * 
 * Design Pattern: This class is a singleton, so only one instance of it  
 * will ever exist during the application's lifecycle.
 * 
 * @author sgil
 *
 */
public class Syncher {
	private static StoryManager storyMan = null;
	private static ChapterManager chapMan = null;
	private static MediaManager mediaMan = null;
	private static ChoiceManager choiceMan = null;
	private static Syncher self = null;

	protected Syncher(Context context) {
		storyMan = StoryManager.getInstance(context);
		chapMan = ChapterManager.getInstance(context);
		mediaMan = MediaManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
	}

	/**
	 * Retrieves an instance of the Syncher class. Because it is a singleton, 
	 * the same instance will always be returned. </br></br>
	 * 
	 * Example call: </br>
	 * Syncher syncher = Syncher.getInstance(someActivity.this);
	 * 
	 * @param context
	 * 			Can be an activity or application context.
	 */
	public static Syncher getInstance(Context context) {
		if (self == null) {
			self = new Syncher(context);
		}
		return self;
	}

	/**
	 * This method is for updating the database when the user modifies a story 
	 * locally. So any changes to the story, including any changes to its 
	 * chapters or the choices and media contained in those chapters will be 
	 * updated in the database. </br></br>
	 * 
	 * Note that it doesn't always just update story information. If any  
	 * object doesn't exist in the database, it will also be added.</br></br>
	 * 
	 * Example call: </br>
	 * Syncher syncher = Syncher.getInstance(someActivity.this); </br>
	 * Story myStory = new Story("title", "author", "description", "phoneId");
	 * </br>
	 * syncher.syncStoryFromMemory(myStory);
	 * 
	 * @param story
	 */	
	public void syncStoryFromMemory(Story story) {
		storyMan.sync(story, story.getId());
		for (Chapter chap : story.getChapters()) {
			syncChapterParts(chap);
			chapMan.sync(chap, chap.getId());
		}
	}	

	/**
	 * Given a chapter object, this method takes care of synching the parts 
	 * of a chapter (media and choices). This means all its media and choices 
	 * will either be inserted into the database if they don't exist, or will 
	 * be updated if they exist. Any deletions to media will also by synched. 
	 * Note that this ONLY updates the chapter parts, not the chapter's  
	 * information itself (eg. chapter text, story id it belongs to). </br></br>
	 * 
	 * Example call: </br>
	 * Assume "chap" is a chapter object that contains choices and media. </br>
	 * Syncher syncher = Syncher.getInstance(someActivity.this); </br>
	 * syncher.syncChapterParts(chap); </br>
	 * 
	 * @param chap
	 */
	public void syncChapterParts(Chapter chap) {
		ArrayList<UUID> medias = new ArrayList<UUID>();

		for (Media photo : chap.getPhotos()) {
			medias.add(photo.getId());
			mediaMan.sync(photo, photo.getId());
		}
		for (Media ill : chap.getIllustrations()) {
			medias.add(ill.getId());
			mediaMan.sync(ill, ill.getId());
		}
		for (Choice choice : chap.getChoices()) {
			choiceMan.sync(choice, choice.getId());
		}
		mediaMan.syncDeletions(medias, chap.getId());
	}		
	
	/**
	 * This method takes care of saving a story from the server into the 
	 * database. This includes all the parts of a story (chapter, and choices 
	 * and media belonging to the chapters). As well, it saves the bitmaps 
	 * of all the images contained in its chapters to the SD card so the 
	 * user who downloaded the story can use those images as well. </br></br>
	 * 
	 * Example call: </br>
	 * Assume myStory is a story downloaded from the server (a story downloaded 
	 * from the server will always have all of its components). </br></br>
	 * 
	 * Syncher syncher = Syncher.getInstance(someActivity.this); </br>
	 * syncher.syncStoryFromServer(myStory); </br></br>
	 * 
	 * myStory has now been saved to the database, along with all chapters, 
	 * media, and choices.
	 * 
	 * @param story
	 */
	public void syncStoryFromServer(Story story) {
		ArrayList<UUID> medias = new ArrayList<UUID>();
		storyMan.sync(story, story.getId());

		for (Chapter chap : story.getChapters()) {
			for (Media photo : chap.getPhotos()) {
				medias.add(photo.getId());
				String path = Utilities.saveImageToSD(photo.getBitmapFromString());
				photo.setPath(path);
				mediaMan.sync(photo, photo.getId());
			}
			for (Media ill : chap.getIllustrations()) {
				String path = Utilities.saveImageToSD(ill.getBitmapFromString());
				ill.setPath(path);
				medias.add(ill.getId());
				mediaMan.sync(ill, ill.getId());
			}
			for (Choice choice : chap.getChoices()) {
				choiceMan.sync(choice, choice.getId());
			}
			chapMan.sync(chap, chap.getId());
			mediaMan.syncDeletions(medias, chap.getId());
		}
	}

	/**
	 * Syncs all the chapters of a story. This means retrieving every 
	 * chapter of the given story id, and getting all the choices and medias  
	 * that belong to it. This is usually when a story is about to get  
	 * published onto the server (since a story needs to have all of its  
	 * components before going on to the server). It is also used however when  
	 * reading a story; all the chapters are first retrieved, so then each one  
	 * can be displayed as the user reads on. </br></br>
	 * 
	 * Example call: </br>
	 * UUID storyId = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7"); </br>
	 * 
	 * 
	 * @param storyId
	 * 			The id of the story we want to get all the chapters from. Must 
	 * 			be a UUID.
	 */
	public ArrayList<Chapter> syncChaptersFromDb(UUID storyId) {
		ArrayList<Chapter> chaps = chapMan.getChaptersByStory(storyId);
		ArrayList<Chapter> fullChaps = new ArrayList<Chapter>();
		
		for (Chapter chap : chaps) {
			chap.setChoices(choiceMan.retrieve(new Choice(null, 
					chap.getId(), null, null)));
			chap.setIllustrations(mediaMan.retrieve(new Media(null, 
					chap.getId(), null, Media.ILLUSTRATION, "")));
			chap.setPhotos(mediaMan.retrieve(new Media(null, 
					chap.getId(), null, Media.PHOTO, "")));	
			fullChaps.add(chap);
		}
		return fullChaps;
	}	
}
