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
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.serverClasses;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.StoringManager;

import android.os.StrictMode;

/**
 * Role: Uses functions from the ESUpdates to add, remove, update, and 
 * search for stories that have been published onto the server. All 
 * operations except for retrieving stories are performed on a different
 * thread than the main UI thread.
 * 
 * </br> 
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * 
 * @see ESUpdates
 * @see StoringManager
 */
public class ServerManager {
	private static ESRetrieval esRetrieval = null;
	private static ESUpdates esUpdates = null;
	private static ServerManager self = null;
	public static String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/";
	

	/**
	 * Initializes a new ServerManager.
	 */
	protected ServerManager() {
		esUpdates = new ESUpdates();
		esRetrieval = new ESRetrieval();
	}

	/**
	 * Returns an instance of a ServerManager (singleton).
	 * @return
	 */
	public static ServerManager getInstance() {
		if (self == null) {
			self = new ServerManager();
		}
		return self;
	}
	
	public void setTestServer() {
		server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/tests/";
	}
	
	public void setRealServer() {
		server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/";
	}	

	/**
	 * Inserts a story story onto the server.The insertStory from the ESUpdates 
	 * class, here the story is prepared (all its images are first turned into 
	 * strings to be stored), and this method also sets up a new thread for the
	 * task.
	 * 
	 * </br>
	 * Eg. Story myStory = new Story(UUID, "My Cow", "John Blaine", "a little
	 * 								 cow", phoneId).
	 * </br>
	 * ServerManager sm = ServerManager.getInstance(context);
	 * </br>
	 * sm.insert(myStory);
	 * 
	 * @param story
	 * 			Story story to be inserted.
	 */	
	public void insert(Story story){
		esUpdates.insertStory(story, server);
	}


	public Story getById(UUID id) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		// search by id
		return esRetrieval.searchById(id.toString(), server);	
	}


	public ArrayList<Story> getAll() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		ArrayList<Story> stories = new ArrayList<Story>();
		try {
			stories = esRetrieval.retrieve(null, server);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		

		return stories;
	}

	/**
	 * Returns null if no stories were found.
	 * @return
	 */
	public Story getRandom() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
		.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		String query = "{\"query\": {\"custom_score\" : {\"script\" : "
				+ "\"random()\", \"query\" : {\"match_all\" : {}}}}, " +
				"\"sort\" : {\"_score\" : {\"order\" :\"desc\"}}, \"size\" :" 
				+ " 1 }";
		ArrayList<Story> stories = new ArrayList<Story>();	
		try {
			stories = esRetrieval.retrieve(query, server);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		if (stories.size() != 1) {
			return null;
		} else {
			return stories.get(0);
		}		
	}

	public ArrayList<Story> searchByKeywords(String keywords) {
		String selection = prepareKeywords(keywords);
		String query = "{\"query\" : {\"query_string\" : {\"default_field\""
				+ " : \"title\",\"query\" : \"" + selection + "\"}}}";	

		ArrayList<Story> stories = new ArrayList<Story>();
		try {
			stories =  esRetrieval.retrieve(query, server);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stories;
	}

	/**
	 * Updates a story on the server. This is done by first deleting the
	 * story matching the story to be updated's id, and then re-inserting
	 * it. This method sets up a new thread to do the updating on, and uses
	 * ESUpdates methods to remove and insert the story.
	 * 
	 * </br> Example call.
	 * </br> Story newStory = new Story(exisitingUUID, "new title", 
	 * 									"new author", null, null);
	 * </br> ServerManager sm = ServerManager.getInstance(context);
	 * </br> sm.update(newStory);
	 * 
	 * @param story
	 * 			Story with updates/new data that you want to publish.
	 */
	public void update(Story story) { 
		String id = story.getId().toString();

		// story already on server
		if (esRetrieval.searchById(id, server) != null) {
			try {
				esUpdates.deleteStory(id, server);
				esUpdates.insertStory(story, server);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			insert(story);
		}
	}	

	/**
	 * Removes a story from the server. It deletes the story with
	 * the matching id of the story passed to it.
	 * 
	 * </br> Example call.
	 * </br> Story criteria = (UUID, null, null, null, null);
	 * </br> ServerManager sm = ServerManager.getInstance(context);
	 * </br> sm.remove(criteria);
	 * 
	 * @param story
	 * 			Story with id of the story you want to delete from server. 
	 */
	public void remove(String id) { 
		try {
			esUpdates.deleteStory(id, server);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}	

	/**
	 * Sets the selection string for doing a keyword search of story title.
	 * The argument args is not needed when dealing with the server, it is
	 * used with the classes that implement StoringManager and interact with
	 * the database.
	 * 
	 * </br> Example. Story crit = new Story(null, "ham butter eggs", null,
	 * 										null, null);
	 * </br> String selection = setSearchCriteria(crit, null);
	 * 
	 * </br> selection holds "ham AND butter AND eggs".
	 * 
	 * </br> If the story's title is null, then an empty selection string
	 * will be returned.
	 * 
	 * @param story
	 */
	private String prepareKeywords(String keywords) {
		String selection = "";

		if (keywords == null) {
			return selection;
		}

		// split keywords and clean them
		List<String> words = Arrays.asList(keywords.split("\\s+"));

		if (words.size() > 0) {
			selection += words.get(0);
		}

		for (int i = 1; i < words.size(); ++i) {
			selection += " AND " + words.get(i);
		}
		return selection;
	}
}
