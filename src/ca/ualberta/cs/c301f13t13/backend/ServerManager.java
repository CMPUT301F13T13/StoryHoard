/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

import android.os.storage.StorageManager;

/**
 * @author sgil
 *
 */
public interface ServerManager {


	/**
	 * Saves a story to the server for other users to see.
	 * @param story
	 */
	public void publish(Story story);	
	
	/**
	 * Retrieves all stories from the server, i.e. the published stories.
	 * 
	 * @return ArrayList
	 */
	public ArrayList<Story> getPublishedStories();	
	
	/**
	 * Updates a published story, i.e. republishes a story after
	 * changes have been made to it.
	 * @param story
	 */
	public void updatePublished(Story story);	
	
	/**
	 * Searches for a story on the server.
	 */
	public void searchPublished(Story story);

}
