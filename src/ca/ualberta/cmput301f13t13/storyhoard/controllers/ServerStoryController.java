package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.ServerManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;

public class ServerStoryController implements SHController<Story>{
	private static ServerStoryController self = null;   
	private static ServerManager serverMan;
	private static ChapterController chapCon = null;

	protected ServerStoryController(Context context) {
		serverMan = ServerManager.getInstance();
		chapCon = ChapterController.getInstance(context);
	}
	
	public static ServerStoryController getInstance(Context context) {
		if (self == null) {
			self = new ServerStoryController(context);
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
	@Override
	public ArrayList<Story> getAll() {
		Story criteria = new Story(null, null, null, null, null);
		return serverMan.retrieve(criteria);
	}		
	
	public void publish(Story story) {
		story.setChapters(chapCon.getFullStoryChapters(story.getId()));
		serverMan.update(story);
	}
	
	public void unpublish(Story story) {
		serverMan.remove(story);
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
	public ArrayList<Story> searchByTitle(String title) {
		Story criteria = new Story(null, title, null, null, null);
		return serverMan.retrieve(criteria);
	}		
	
	/**
	 * Chooses a random story from within the stories that are 
	 * published. If there are no published stories available,
	 * it will return null.
	 * 
	 */
	public Story getRandomStory() {
		Story story = null;
		ArrayList<Story> stories = getAll();
		
		if (stories.size() < 1) {
			return null;
		}
		
		Random rand = new Random(); 
		int index = rand.nextInt(stories.size());
		
		story = stories.get(index);
		 
		return story;
	}

	@Override
	public ArrayList<Story> retrieve(Story story) {
		return serverMan.retrieve(story);
	}

	@Override
	public void insert(Story story) {
		serverMan.update(story);
	}

	@Override
	public void update(Story story) {
		serverMan.update(story);
	}	
}
