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

package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.ArrayList;

import java.util.Random;
import java.util.UUID;

import android.content.Context;

/**
 * Role: Is called by the views of the application to then interact with the
 * manager classes to handle all types of data (story, chapter, media, choice).
 * This class does not directly interact with the database or server, only the
 * managers.
 * 
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 */
public class SHController {

	private static SHController self = null;   
	private static ServerManager serverMan;
	private static StoryManager storyMan;
	private static ChapterManager chapterMan;
	private static ChoiceManager choiceMan;
	private static MediaManager mediaMan;
	
	private Context context = null;

	protected SHController(Context context) {
		serverMan = ServerManager.getInstance();
		storyMan = StoryManager.getInstance(context);
		chapterMan = ChapterManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
		mediaMan = MediaManager.getInstance(context);
		
		this.context = context;
	}

	/**
	 * Returns an instance of the general controller as a singleton.
	 * 
	 * @return SHController
	 */
	public static SHController getInstance(Context context) {
		if (self == null) {
			self = new SHController(context);
		}
		return self;
	}

	/**
	 * Gets all the stories that are either cached, created by the author, or
	 * published.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY, or
	 *            CREATED_STORY.
	 * @return Array list of all the stories the application asked for.
	 */
	public ArrayList<Story> getAllAuthorStories() {
		Story criteria = new Story(null, null, null, null, 
				Utilities.getPhoneId(context));
		ArrayList<Object> objects = storyMan.retrieve(criteria);
		ArrayList<Story> stories = Utilities.objectsToStories(objects);
		return stories;
	}

	/**
	 * Gets all the stories that are either cached, created by the author, or
	 * published.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY, or
	 *            CREATED_STORY.
	 * @return Array list of all the stories the application asked for.
	 */
	public ArrayList<Story> getAllCachedStories() {
		Story criteria = new Story(null, null, null, null, null);
		ArrayList<Object> objects = storyMan.retrieve(criteria);
		ArrayList<Story> stories = Utilities.objectsToStories(objects);
		return stories;
	}
	
	/**
	 * Gets all the stories that are either cached, created by the author, or
	 * published.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY, or
	 *            CREATED_STORY.
	 * @return Array list of all the stories the application asked for.
	 */
	public ArrayList<Story> getAllPublishedStories() {
		Story criteria = new Story(null, null, null, null, null);
		ArrayList<Object> objects = serverMan.retrieve(criteria);
		ArrayList<Story> stories = Utilities.objectsToStories(objects);
		return stories;
	}	
	
	/**
	 * Retrieves all the chapters that are in a given story.
	 * 
	 * @param storyId
	 *            Id of the story the chapters are wanted from.
	 * 
	 * @return ArrayList of the chapters.
	 */
	public ArrayList<Chapter> getAllChapters(UUID storyId) {
		Chapter criteria = new Chapter(null, storyId, null);	
		ArrayList<Object> objects = chapterMan.retrieve(criteria);
		ArrayList<Chapter> chapters = Utilities.objectsToChapters(objects);
		return chapters;
	}

	/**
	 * Retrieves all the choices that are in a chapter.
	 * 
	 * @param chapterId
	 *            Id of the chapter the choices are wanted from.
	 * 
	 * @return ArrayList of the chapter's choices.
	 */
	public ArrayList<Choice> getAllChoices(UUID chapterId) {
		Choice criteria = new Choice(null, chapterId);
		ArrayList<Object> objects = choiceMan.retrieve(criteria);
		ArrayList<Choice> choices = Utilities.objectsToChoices(objects);		
		return choices;
	}

	/**
	 * Retrieves all the illustrations that are in a chapter.
	 * 
	 * @param chapterId
	 *            Id of the chapter the illustrations are wanted from.
	 * 
	 * @return ArrayList of the illustrations.
	 */
	public ArrayList<Media> getAllIllustrations(UUID chapterId) {
		Media criteria = new Media(null, chapterId, null, Media.ILLUSTRATION);
		ArrayList<Object> objects = mediaMan.retrieve(criteria);
		ArrayList<Media> illustrations = Utilities.objectsToMedia(objects);
		return illustrations;
	}

	/**
	 * Retrieves all the photos that are in a chapter.
	 * 
	 * @param chapterId
	 *            Id of the chapter the photos are wanted from.
	 * 
	 * @return ArrayList of the photos.
	 */
	public ArrayList<Media> getAllPhotos(UUID chapterId) {
		Media criteria = new Media(null, chapterId, null, Media.PHOTO);
		ArrayList<Object> objects = mediaMan.retrieve(criteria);
		ArrayList<Media> photos = Utilities.objectsToMedia(objects);
		return photos;
	}

	/**
	 * Used to search for stories matching the given search criteria. Users can
	 * either search by specifying the title or author of the story. All stories
	 * that match will be retrieved.
	 * 
	 * @param title
	 *            Title of the story user is looking for.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY
	 * 
	 * @return ArrayList of stories that matched the search criteria.
	 */
	public ArrayList<Story> searchStory(String title) {
		Story criteria = new Story(null, title, null, null, null);
		ArrayList<Object> local = storyMan.retrieve(criteria);
		ArrayList<Object> server = serverMan.retrieve(criteria);
		local.addAll(server);
		ArrayList<Story> stories = Utilities.objectsToStories(local);
		return stories;
	}
	
	/**
	 * Used to search for stories matching the given search criteria. Users can
	 * either search by specifying the title or author of the story. All stories
	 * that match will be retrieved.
	 * 
	 * @param title
	 *            Title of the story user is looking for.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY
	 * 
	 * @return ArrayList of stories that matched the search criteria.
	 */

	
	/**
	 * Chooses a random story from within the stories that are 
	 * published. If there are no published stories available,
	 * it will return null.
	 * 
	 */
	public Story getRandomStory() {
		Story story = null;
		ArrayList<Story> stories = getAllPublishedStories();
		Random rand = new Random(); 
		int index = rand.nextInt(stories.size());
		
		if (stories.size() < 1) {
			return null;
		}
		
		story = stories.get(index);
		 
		return story;
	}
	
	/**
	 * Retrieves a random choice from the chapter.
	 * 
	 * @param chapterID
	 *            Id of  the chapters that the choice is for.
	 * 
	 * @return a choice
	 */
	public Choice getRandomChoice(UUID chapterId) {
		ArrayList<Choice> choices = getAllChoices(chapterId);
		int max = 0;
		max = choices.size();
		Random rand = new Random(); 
		int num;
		num = rand.nextInt(max);
		Choice choice=choices.get(num);

		return choice;
	}	
	
	/**
	 * Updates either a story, chapter, or choice object. Must specify what type
	 * of object it getting updated. Also, updates are happening to the database
	 * of the phone, not the server.
	 * 
	 * @param object
	 *            Object to be updated.
	 * @param type 
	 *            Will either be CHAPTER, CHOICE, MEDIA, PUBLISHED_STORY,
	 *            CACHED_STORY, CREATED_STORY
	 */

	
	/**
	 * Adds either a story, chapter, or choice.
	 * 
	 * @param object
	 *            Object to be inserted (must either be a Story, Chapter,
	 *            Choice, or Media object).
	 * @param type
	 *            Will either be CHAPTER, CHOICE, MEDIA, PUBLISHED_STORY,
	 *            CACHED_STORY, CREATED_STORY
	 */
	
}
