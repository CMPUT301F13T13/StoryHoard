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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

/**
 * 
 * Role: Interacts with the server by inserting, retrieving, updating, and
 * deleting story objects.
 * 
 * CODE REUSE: This code was taken directly from 
 * URL: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ESClient.java
 * Date: Nov. 4th, 2013 
 * Licensed under CC0 (available at http://creativecommons.org/choose/zero/)
 * 
 * @author Abram Hindle
 * @author Chenlei Zhang
 * @author Ashley Brown
 * @author Stephanie Gil
 */
public class ServerManager implements StoringManager{
	private static Server server = null;
	private static ServerManager self = null;
	
	protected ServerManager() {
		server = new Server();
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
		
		if (crit.getId() == null && crit.getTitle() == null) {
			
			// get all stories
		} else if (crit.getId() != null){
			
			// search by id
			stories.add(server.searchById(crit.getId().toString()));		
		} else {
			
			// search by keyword
			try {
				ArrayList<String> sargs = new ArrayList<String>();
				HashMap<String, String> storyData = crit.getSearchCriteria();
				
				// setting selection string
				for (String key: storyData.keySet()) {
					sargs.add(storyData.get(key));
				}
				
				String selection = setSearchCriteria(criteria, sargs);	
				
				stories = server.searchByKeywords(crit, selection);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			// TODO Auto-generated catch block
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
