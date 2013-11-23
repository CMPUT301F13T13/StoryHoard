package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;

public class LocalStoryController implements SHController<Story> {
	private static StoryManager storyMan = null;
	private static LocalStoryController self = null;
	private static String phoneId = null;

	protected LocalStoryController(Context context) {
		storyMan = StoryManager.getInstance(context);
		phoneId = Utilities.getPhoneId(context);
	}
	
	public static LocalStoryController getInstance(Context context) {
		if (self == null) {
			self = new LocalStoryController(context);
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
	public ArrayList<Story> getAllCachedStories() {
		Story criteria = new Story(null, null, null, null, Story.NOT_AUTHORS);
		return retrieve(criteria);
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
		Story criteria = new Story(null, null, null, null, phoneId);
		return retrieve(criteria);
	}
	
	public void cache(Story story) {
		if (existsLocally(story)) {
			update(story);
		} else {
			insert(story);
		}	
	}
	
	@Override
	public void update(Story story) {
		storyMan.update(story);
	}
	
	@Override
	public void insert(Story story) {
		storyMan.insert(story);
	}
	
	@Override
	public ArrayList<Story> retrieve(Story story) {
		return storyMan.retrieve(story);
	}
	
	public Boolean existsLocally(Story story) {
		Story crit = new Story(story.getId(), null, null, null, null);
		ArrayList<Story> stories = storyMan.retrieve(crit);
		if (stories.size() != 1) {
			return false;
		}
		return true;		
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
	public ArrayList<Story> searchCachedStories(String title) {
		Story criteria = new Story(null, title, null, null, Story.NOT_AUTHORS);
		return storyMan.retrieve(criteria);
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
	public ArrayList<Story> searchAuthorStories(String title) {
		Story criteria = new Story(null, title, null, null, phoneId);
		return storyMan.retrieve(criteria);
	}

	@Override
	public ArrayList<Story> getAll() {
		Story criteria = new Story(null, null, null, null, null);
		return retrieve(criteria);
	}		
}
