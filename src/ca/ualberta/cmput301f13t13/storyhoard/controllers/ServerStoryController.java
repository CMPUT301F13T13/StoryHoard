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

package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * This class implements the SHController interface to be able to insert, update,
 * retrieve, and delete stories from the server. It defines the generic type to
 * be the class Story. Instead of directly interacting with the server, it 
 * wraps methods from the server manager class, which itself uses methods from
 * ESRetrieval and ESUpdates classes to do the actual interacting with the
 * server. </br></br>
 * 
 * This class works as an adapter because it uses the methods available in 
 * ServerManager and makes them fit in the methods required my this method
 * for the activity classes to use with ease. </br></br>
 * 
 * Note that it specifies different methods for different types of retrieval. 
 * Specifically, getAll(), getRandom(), and searchByTitle().  </br></br>
 * 
 * Design Patterns: Singleton, Adaptor
 * 
 * @author Stephanie Gil
 * 
 * @see SHController
 * @see ServerManager
 */
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
	 * Gets all the stories that are on the server. The stories are returned in
	 * an array list. To actually </br></br>
	 * 
	 * Example call: </br>
	 * ArrayList<Story> stories = getAll(); </br></br>
	 * 
	 * The array list stories will contain all the stories that were found on 
	 * the server.
	 */
	@Override
	public ArrayList<Story> getAll() {
		return serverMan.getAll();
	}	

	/**
	 * This method published a story object onto the server. Note that it calls 
	 * the update() method in ServerManager rather than the insert() method.
	 * This is done because this method is also called whenever the "publish"
	 * button in EditStoryChapter is called. The functionality desired for this
	 * is insertion of the story onto the server if it does not exist, or the
	 * update of the story if it does exist, which is exactly what the update()
	 * method in ServerManager does. </br></br>
	 * 
	 * This method also prepares the story before putting onto the server. 
	 * Preparing the story involves retrieving all of its chapters, all
	 * the choices and media of those chapters, and for all the media, 
	 * converting the bitmaps to Strings and storing them in the Media's
	 * BitMapString field. They are converted by encoding them into base 64
	 * strings so that the Media objects are able to be converted into JSON
	 * strings later on and put onto the server. </br></br>
	 * 
	 * Example call: </br>
	 * Story myStory = new Story("the title", "the author", "description", 
	 * 			"123"); </br>
	 * insert(story); </br></br>
	 * 
	 * myStory is now on the server.
	 * 
	 * @param story
	 * 			Story to be inserted into the server.
	 */

	/**
	 * Uses the update method in ServerManager to insert a story onto the 
	 * server. </br></br>
	 * 
	 * Note that the update method in ServerManager checks whether or not 
	 * a story exists on the server and either inserts it or updates it
	 * accordingly. Therefore, this method could technically also be used for 
	 * inserting stories. </br></br>
	 * 
	 * Example call: </br>
	 * First, a story is needed on the server to update. </br>
	 * Story myStory = new Story("the title", "the author", "description", 
	 * 			"123"); </br>
	 * insert(story); </br></br>
	 * 	 
	 * Now, to update the story. </br>
	 * myStory.setTitle("new title"); </br>
	 * update(story); </br></br>
	 * 
	 * myStory on the server now has the title "new title". </br>
	 * You can change any part of the story an update it, including its,
	 * any objects and fields belonging to the chapter, and so on. </br></br>
	 * 
	 * The only attribute that is not meant to be updatable is the story id.
	 * Changing it would just insert a whole different copy of the story onto
	 * the server, leaving the original untouched.
	 * 
	 * @param story
	 * 			Story object containing the changes in data to be made on the
	 * 			version of it stored in the server.
	 */
	@Override
	public void update(Story story) {
		serverMan.update(story);
	}

	/**
	 * Uses the remove method in ServerManager to insert a story onto the 
	 * server. </br></br>
	 * 
	 * Note that unlike the other methods, the remove method takes a UUID, 
	 * not a Story object. </br></br>
	 * 
	 * Example call: </br>
	 * First, a story is needed on the server to remove. </br>
	 * Story myStory = new Story("the title", "the author", "description", 
	 * 			"123"); </br>
	 * insert(story); </br></br>
	 * 	 
	 * Now, to remove the story. </br>
	 * remove(story.getId()); </br></br>
	 * 
	 * The story has now been removed from the server.
	 * 
	 * @param storyId
	 * 			Must be a UUID. The following is an example of a UUID format: 
	 * 			f1bda3a9-4560-4530-befc-2d58db9419b7
	 */
	@Override
	public void remove(UUID storyId) {
		serverMan.remove(storyId.toString());
		
	}	
	
	/**
	 * Calls the searchByKeywords method defined in ServerManager to retrieve
	 * all stories that were on the server whose titles contained all of the
	 * keywords given.
	 * </br>
	 * It is also assumed that the string of keywords passed in contains
	 * the keywords separated by whitespace. </br></br>
	 * 
	 * Example Call: </br>
	 * String keywords = "dog cat blue sky";
	 * ArrayList<Story> stories = searchByKeywords(keywords); </br></br>
	 * 
	 * stories will contain any story that contained "dog", "cat", "blue",
	 * and "sky" in its title. Therefore, a story with a title "The cat
	 * and blue dog in the sky" would be contained in stories, but a story
	 * with the title "blue cat and dog" would not since it did not contain
	 * "sky" in the title.
	 * 
	 * @param keywords
	 * 			The String of keywords that you want appearing in the title of 
	 * 			the stories you are searching. Each keyword must be separated 
	 * 			by whitespace.
	 */ 
	public ArrayList<Story> searchByKeywords(String keywords) {
		return serverMan.searchByKeywords(keywords);
	}		
	
	/**
	 * Calls the getRandom() method defined in ServerManager to retrieve a 
	 * random story from the server. If no story exists on the server, then
	 * null is returned. </br></br>
	 * 
	 * Example call:</br>
	 * Story randomStory = getRandomStory(); </br></br>
	 * 
	 * randomStory now either contains a random story on the server, or null
	 * if no stories were found.
	 * 
	 */
	public Story getRandomStory() {
		return serverMan.getRandom();
	}
}
