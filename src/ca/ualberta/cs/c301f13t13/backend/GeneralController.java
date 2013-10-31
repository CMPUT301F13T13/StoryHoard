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

package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;

public class GeneralController {
	// MACROS
	public static final int ALL = -1;
	public static final int CACHED = 0;
	public static final int CREATED = 1;
	public static final int PUBLISHED = 2;
	
	public static final int STORY = 0;
	public static final int CHAPTER = 1;
	public static final int CHOICE = 2;
	
	// SELF
	private static GeneralController self = null;

	protected GeneralController() {	
	}
	
	/**
	 * Returns an instance of the general controller as a singleton.
	 * 
	 * @return
	 */
	public static GeneralController getInstance() {
		if (self == null) {
			self = new GeneralController();
		}
		
		return self;
	}
	
	/** 
	 * Gets all the stories that are either cached, created by the author, or published.
	 * 
	 * @param type
	 * 			Will either be CACHED (0), CREATED (1), or PUBLISHED (2). 
	 * @param context
	 * @return ArrayList<Story>
	 */
	public ArrayList<Story> getAllStories(int type, Context context){
		StoryManager sm = StoryManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Story> stories = new ArrayList<Story>();
		ArrayList<Object> objects;
		Story criteria;
		
		switch(type) {
		case CACHED:
			criteria = new Story(null, "", "", "", false);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			criteria = new Story(null, "", "", "", true);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case PUBLISHED:
			stories = sm.getPublishedStories();
			break;
		default:		
			break;
		}
		
		return stories;
	}
	
	/**
	 *  Retrieves all the chapters that are in story.
	 * 
	 * @param storyId
	 * @param context
	 * 
	 * @return ArrayList<Chapter>
	 */
	public ArrayList<Chapter> getAllChapters(UUID storyId, Context context){
		ChapterManager cm = ChapterManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		ArrayList<Object> objects;
		Chapter criteria = new Chapter(storyId, "");
		
		objects = cm.retrieve(criteria, helper);
		chapters = Utilities.objectsToChapters(objects);
		
		return chapters;
	}
	
	/** 
	 * Retrieves all the choices that are in a chapter.
	 * 
	 * @param chapterId
	 * @param context
	 * 
	 * @return ArrayList<Choice>
	 */
	public ArrayList<Choice> getAllChoices(UUID chapterId, Context context){
		ChoiceManager cm = ChoiceManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Choice> choices = new ArrayList<Choice>();
		ArrayList<Object> objects;
//		Choice criteria = new Choice(chapterId, "");
//		
//		objects = cm.retrieve(criteria, helper);
//		choices = Utilities.objectsToChoices(objects);
		return choices;
	}
	
	/**
	 * Adds either a story, chapter, or choice to the database.
	 * 
	 * @param object
	 * 			Object to be inserted (must either be a Story, Chapter, or
	 * 			Choice instance).
	 * @param type
	 * 			Will either be STORY(0), CHAPTER(1), CHOICE(2).
	 * @param context
	 */
	public void addObjectLocally(Object object, int type, Context context) {
		DBHelper helper = DBHelper.getInstance(context);
		
		switch (type) {
		case STORY:
			Story story = (Story) object;
			StoryManager sm = new StoryManager(context);
			sm.insert(story, helper);
			break;
		case CHAPTER:
			Chapter chapter = (Chapter) object;
			ChapterManager cm = new ChapterManager(context);
			cm.insert(chapter, helper);
			break;
		case CHOICE:
			Choice choice = (Choice) object;
			ChoiceManager chm = new ChoiceManager(context);
			chm.insert(choice, helper);
			break;
		}
	}
	
	/** 
	 * Used to search for stories matching the given search criteria.
	 * Users can either search by specifying the title or author of
	 * the story. All stories that match will be retrieved.
	 * 
	 * @param title
	 * 			Title of the story user is looking for.
	 * @param author
	 * 			Author of the story user is looking for.
	 * @param type
	 * 			Will either be CACHED (0), CREATED (1) , or PUBLISHED (2).
	 * @param context
	 * 
	 * @return ArrayList<Story>
	 */
	public ArrayList<Story> searchStory(String title, String author,
										int type, Context context){
		Story criteria;
		ArrayList<Object> objects;
		ArrayList<Story> stories = new ArrayList<Story>();
		StoryManager sm = StoryManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		
		switch(type) {
		case CACHED:
			criteria = new Story(null, author, title, "", false);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			criteria = new Story(null, author, title, "", true);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);			
			break;
		case PUBLISHED:
			break;
		}
		
		return stories;
	}
	
	/** 
	 * Retrieves a complete chapter (including any photos, illustrations,
	 * and choices).
	 * 
	 * @return
	 */
	public Chapter getCompleteChapter(UUID id, Context context){
		ChapterManager cm = ChapterManager.getInstance(context);
		ChoiceManager chom = ChoiceManager.getInstance(context);
		MediaManager mm = MediaManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		
		// Search criteria gets set
		Chapter criteria = new Chapter(id, null, "");
		
		// Get chapter
		ArrayList<Object> objects = cm.retrieve(criteria, helper);
		Chapter chapter = (Chapter) objects.get(0);
		
		// Get chapter choices
		Choice choiceCrit = new Choice(null, null, id);
		objects = chom.retrieve(choiceCrit, helper);
		chapter.setChoices(Utilities.objectsToChoices(objects));
		
		// Get media (photos/illustrations)
		// TODO implement this
		
		return chapter;
	}
	
	/** 
	 * Retrieves a complete story (including chapters, any photos, 
	 * illustrations, and choices).
	 * 
	 * @param id
	 * 			Story id
	 * @param context
	 * 
	 * @return Story
	 */
	public Story getCompleteStory(UUID id, Context context){
		StoryManager sm = StoryManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		
		// Search criteria gets set
		Story criteria = new Story(id, "", "", "", null);
		ArrayList<Object> objects = sm.retrieve(criteria, helper);
		Story story = (Story) objects.get(0);
		
		// Get all chapters 
		ArrayList<Chapter> chapters = getAllChapters(id, context);
		HashMap<UUID, Chapter> chaptersHash = new HashMap<UUID, Chapter>();
		
		// Get all choices
		for (Chapter chap: chapters) {
			Chapter fullChap = getCompleteChapter(chap.getId(), context);
			chaptersHash.put(chap.getId(), fullChap);
		}
		
		// add chapters to story
		story.setChapters(chaptersHash);
		
		return story;
	}
	
	/**
	 * Updates either a story, chapter, or choice object. Must specify
	 * what type of object it getting updated. Also, updates are 
	 * happening to the database of the phone, not the server.
	 * 
	 * @param object
	 * 			Object to be updated.
	 * @param type
	 * 			Will either be STORY (0), CHAPTER (1), or CHOICE (2)
	 * @param context
	 */
	public void updateObjectLocally(Object object, int type, Context context) {
		DBHelper helper = DBHelper.getInstance(context);
		
		switch(type) {
		case STORY:
			StoryManager sm = StoryManager.getInstance(context);
			sm.update(object, helper);
			break;
		case CHAPTER:
			ChapterManager cm = ChapterManager.getInstance(context);
			cm.update(object, helper);
			break;
		case CHOICE:
			ChoiceManager chom = ChoiceManager.getInstance(context);
			chom.update(object, helper);
			break;
		default:
			// raise exception
			break;
		}	
	}

	/**
	 * Saves a story onto the server.
	 */
	public void publishStory(Story story, Context context) {
		//TODO implement
	}
	
	/**
	 * Republishes a story an author has changed.
	 */
	public void updatePublished(Story story, Context context) {
		// TODO implement
	}
}
