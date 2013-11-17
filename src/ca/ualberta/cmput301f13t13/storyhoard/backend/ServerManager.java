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
package ca.ualberta.cmput301f13t13.storyhoard.backend;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;


/**
 * 
 * Role: Uses functions from the ESClient to add, remove, update, and 
 * search for stories that have been published onto the server.
 * 
 * @author Stephanie Gil
 * 
 * @see ESClient
 * @see StoringManager
 */
public class ServerManager implements StoringManager{
	private static ESClient server = null;
	private static ServerManager self = null;
	
	protected ServerManager() {
		server = new ESClient();
	}
	
	public static ServerManager getInstance() {
		if (self == null) {
			self = new ServerManager();
		}
		return self;
	}	

	/**
	 * Consumes the POST/Insert operation of the service
	 */	
	@Override
	public void insert(Object object){
		server.insertStory((Story) object);
	}
	
	/**
	 * Consumes the Get operation of the service
	 */
	@Override
	public ArrayList<Object> retrieve(Object criteria) {
		Story crit = (Story) criteria;
		ArrayList<Object> stories = new ArrayList<Object>();
		
		if (crit.getId() != null) { 
			
			// search by id
			Story story = server.searchById(crit.getId().toString());
			if (story != null) {
				stories.add(story);
			}
		} else if (crit.getTitle() != null) {
			
			// search by keywords
			try {
				ArrayList<String> sargs = new ArrayList<String>();
				HashMap<String, String> storyData = crit.getSearchCriteria();
				
				// setting selection string
				for (String key: storyData.keySet()) {
					sargs.add(storyData.get(key));
				}
				
				String selection = setSearchCriteria(criteria, sargs);	
				
				stories = server.searchStories(crit, selection);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
		} else {
			// get all stories
		}
		
		return stories;
	}


	/**
	 * update a story on the server
	 */
	@Override
	public void update(Object object) { 
		Story story = (Story) object;
		
		try {
			server.deleteStory(story);
			server.insertStory(story);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	/**
	 * remove a story from server
	 */
//	@Override
	public void remove(Object object) { 
		try {
			server.deleteStory((Story) object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
		
	@Override
	public String setSearchCriteria(Object object, ArrayList<String> args) {
		String selection = "";
		Story story = (Story) object;
		
		String allWords = story.getTitle();
		
		// split keywords and clean them
		List<String> words = Arrays.asList(allWords.split("\\s+"));
		
		if (words.size() > 0) {
			selection += words.get(0);
		}
		
		for (int i = 1; i < words.size(); ++i) {
			selection += " AND " + words.get(i);
		}
		return selection;
	}
}
