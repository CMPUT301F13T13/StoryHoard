package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

public class ServerStoryController implements SHController<Story>{
	private static ServerStoryController self = null;   
	private static ServerManager serverMan;

	protected ServerStoryController(Context context) {
		serverMan = ServerManager.getInstance();
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
		return serverMan.getAll();
	}	
	
	public void publish(Story story) {
		story.prepareChaptersForServer();
		serverMan.update(story);
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
		return serverMan.searchByKeywords(title);
	}		
	
	/**
	 * Chooses a random story from within the stories that are 
	 * published. If there are no published stories available,
	 * it will return null.
	 * 
	 */
	public Story getRandomStory() {
		return serverMan.getRandom();
	}

	@Override
	public void insert(Story story) {
		serverMan.update(story);
	}

	@Override
	public void update(Story story) {
		serverMan.update(story);
	}

	@Override
	public void remove(UUID objId) {
		serverMan.remove(objId.toString());
		
	}	
}
