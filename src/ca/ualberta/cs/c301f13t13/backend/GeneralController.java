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

package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.HashMap;
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
 * @author Stephanie
 * @author Ashley
 * 
 */
public class GeneralController {
	// CONSTANTS
	public static final int ALL = -1;
	public static final int CACHED = 0;
	public static final int CREATED = 1;
	public static final int PUBLISHED = 2;

	public static final int STORY = 0;
	public static final int CHAPTER = 1;
	public static final int CHOICE = 2;
	public static final int MEDIA = 3;

	// SELF
	private static GeneralController self = null;
	
	// Models
	StoryManager sm = null;
	ChapterManager chapm = null;
	ChoiceManager chom = null;
	MediaManager mm = null;
	DBHelper helper = null;

	protected GeneralController(Context context) {
		sm = StoryManager.getInstance(context);
		chapm = ChapterManager.getInstance(context);
		chom = ChoiceManager.getInstance(context);
		mm = MediaManager.getInstance(context);
		helper = DBHelper.getInstance(context);
	}

	/**
	 * Returns an instance of the general controller as a singleton.
	 * 
	 * @return
	 */
	public static GeneralController getInstance(Context context) {
		if (self == null) {
			self = new GeneralController(context);
		}
		return self;
	}

	/**
	 * Gets all the stories that are either cached, created by the author, or
	 * published.
	 * 
	 * @param type
	 *            Will either be CACHED (0), CREATED (1), or PUBLISHED (2).
	 * @return Array list of all the stories the application asked for.
	 */
	public ArrayList<Story> getAllStories(int type) {
		ArrayList<Story> stories = new ArrayList<Story>();
		ArrayList<Object> objects;
		Story criteria;

		switch (type) {
		case CACHED:
			criteria = new Story(null, null, null, null, false);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			criteria = new Story(null, null, null, null, true);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case PUBLISHED:
			criteria = new Story(null, null, null, null, null);
			stories = sm.searchPublished(criteria);
			break;
		default:
			break;
		}

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
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		ArrayList<Object> objects;
		Chapter criteria = new Chapter(null, storyId, null);

		objects = chapm.retrieve(criteria, helper);
		chapters = Utilities.objectsToChapters(objects);

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
		ArrayList<Choice> choices = new ArrayList<Choice>();
		ArrayList<Object> objects;
		Choice criteria = new Choice(null, chapterId);

		objects = chom.retrieve(criteria, helper);
		choices = Utilities.objectsToChoices(objects);
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
		ArrayList<Media> illustrations = new ArrayList<Media>();
		ArrayList<Object> objects;
		Media criteria = new Media(null, chapterId, null, Media.ILLUSTRATION);

		objects = mm.retrieve(criteria, helper);
		illustrations = Utilities.objectsToMedia(objects);
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
		ArrayList<Media> photos = new ArrayList<Media>();
		ArrayList<Object> objects;
		Media criteria = new Media(null, chapterId, null, Media.PHOTO);

		objects = mm.retrieve(criteria, helper);
		photos = Utilities.objectsToMedia(objects);
		return photos;
	}

	/**
	 * Adds either a story, chapter, or choice to locally (to the database).
	 * 
	 * @param object
	 *            Object to be inserted (must either be a Story, Chapter,
	 *            Choice, or Media object).
	 * @param type
	 *            Will either be STORY(0), CHAPTER(1), CHOICE(2), MEDIA (3)
	 */
	public void addObjectLocally(Object object, int type) {

		switch (type) {
		case STORY:
			sm.insert(object, helper);
			break;
		case CHAPTER:
			chapm.insert(object, helper);
			break;
		case CHOICE:
			chom.insert(object, helper);
			break;
		case MEDIA:
			mm.insert(object, helper);
			break;
		}
	}

	/**
	 * Used to search for stories matching the given search criteria. Users can
	 * either search by specifying the title or author of the story. All stories
	 * that match will be retrieved.
	 * 
	 * @param title
	 *            Title of the story user is looking for.
	 * @param author
	 *            Author of the story user is looking for.
	 * @param type
	 *            Will either be CACHED (0), CREATED (1) , or PUBLISHED (2).
	 *            
	 * @return ArrayList of stories that matched the search criteria.
	 */
	public ArrayList<Story> searchStory(String title, String author, int type) {
		Story criteria;
		ArrayList<Object> objects;
		ArrayList<Story> stories = new ArrayList<Story>();

		switch (type) {
		case CACHED:
			criteria = new Story(null, title, author, null, false);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			criteria = new Story(null, title, author, null, true);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case PUBLISHED:
			break;
		default:
			// raise exception
			break;
		}

		return stories;
	}

	/**
	 * Retrieves a complete chapter (including any photos, illustrations, and
	 * choices).
	 * 
	 * @param id
	 *            Id of the chapter wanted.
	 * 
	 * @return The complete chapter.
	 */
	public Chapter getCompleteChapter(UUID id) {
		// Search criteria gets set
		Chapter criteria = new Chapter(id, null, null);

		// Get chapter
		ArrayList<Object> objects = chapm.retrieve(criteria, helper);
		Chapter chapter = (Chapter) objects.get(0);

		// Get chapter choices
		chapter.setChoices(getAllChoices(id));

		// Get media (photos/illustrations)
		ArrayList<Media> photos = getAllPhotos(id);
		chapter.setPhotos(photos);

		ArrayList<Media> ills = getAllIllustrations(id);
		chapter.setIllustrations(ills);

		return chapter;
	}

	/**
	 * Retrieves a complete story (including chapters, and any photos,
	 * illustrations, and choices belonging to the chapters).
	 * 
	 * @param id
	 *            Story id of the story wanted.
	 * 
	 * @return The complete story.
	 */
	public Story getCompleteStory(UUID id) {
		// Search criteria gets set
		Story criteria = new Story(id, null, null, null, null);
		ArrayList<Object> objects = sm.retrieve(criteria, helper);
		Story story = (Story) objects.get(0);

		// Get all chapters
		ArrayList<Chapter> chapters = getAllChapters(id);
		HashMap<UUID, Chapter> chaptersHash = new HashMap<UUID, Chapter>();

		// Get all choices
		for (Chapter chap : chapters) {
			Chapter fullChap = getCompleteChapter(chap.getId());
			chaptersHash.put(chap.getId(), fullChap);
		}

		// add chapters to story
		story.setChapters(chaptersHash);

		return story;
	}

	/**
	 * Updates either a story, chapter, or choice object. Must specify what type
	 * of object it getting updated. Also, updates are happening to the database
	 * of the phone, not the server.
	 * 
	 * @param object
	 *            Object to be updated.
	 * @param type
	 *            Will either be STORY (0), CHAPTER (1), CHOICE (2), or MEDIA(3)
	 */
	public void updateObjectLocally(Object object, int type) {

		switch (type) {
		case STORY:
			sm.update(object, helper);
			break;
		case CHAPTER:
			chapm.update(object, helper);
			break;
		case CHOICE:
			chom.update(object, helper);
			break;
		case MEDIA:
			mm.update(object, helper);
			break;
		default:
			// raise exception
			break;
		}
	}

	/**
	 * Saves a story onto the server.
	 */
	public void publishStory(Story story) {
		// TODO implement
	}

	/**
	 * Updates a story that is on the server.
	 */
	public void updatePublished(Story story) {
		// TODO implement
	}
}
