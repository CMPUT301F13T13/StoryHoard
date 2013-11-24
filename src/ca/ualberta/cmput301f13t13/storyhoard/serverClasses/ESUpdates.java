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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Role: Interacts with the server by inserting, retrieving, updating, and
 * deleting story objects. This is the only class that directly interacts with
 * the server. It uses the HttpClient to talk to the server, and also Google's
 * gson to move Stories to and from the server.
 * 
 * </br>
 * Design Pattern: Singleton
 * 
 * </br>
 * CODE REUSE: This code is a modified version of the code at URL:
 * https://github
 * .com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301
 * /chenlei/ESUpdates.java Date: Nov. 4th, 2013 Licensed under CC0 (available at
 * http://creativecommons.org/choose/zero/)
 * 
 * @author Abram Hindle
 * @author Chenlei Zhang
 * @author Ashley Brown
 * @author Stephanie Gil
 */
public class ESUpdates {
	public static HttpClient httpclient = null; // Http Connector
	public static Gson gson = null; // JSON Utilities
	private static ESUpdates self = null;

	/**
	 * Initializes an ESUpdates.
	 */
	protected ESUpdates() {
		httpclient = new DefaultHttpClient();
		gson = new Gson();
	}

	/**
	 * Returns an instance of itself.
	 * 
	 * @return
	 */
	public static ESUpdates getInstance() {
		if (self == null) {
			self = new ESUpdates();
		}
		return self;
	}
	
	/**
	 * Deletes an entry (in this case a story object) specified by the id from
	 * the server.
	 * 
	 * </br> Eg. Let's say the following story is on the server. 
	 * </br> Story myStory = new Story(id, "The Cow", "John Wayne", 
	 * 								   "A story about a Cow", phoneId). 
	 * </br> To delete myStory from the server, call
	 * deleteStory(myStory);
	 * 
	 * </br> The method also assumes that the story it is given does actually
	 * exist on the server. An error would occur if a non-existing story tried
	 * to be deleted form the server.
	 * 
	 * @param story
	 */
	public void deleteStory(Story story, String server) throws IOException {
		HttpDelete httpDelete = new HttpDelete(server
				+ story.getId().toString());
		httpDelete.addHeader("Accept", "application/json");

		HttpResponse response = httpclient.execute(httpDelete);

		String status = response.getStatusLine().toString();
		System.out.println(status);

		HttpEntity entity = response.getEntity();
		InputStreamReader is = new InputStreamReader(entity.getContent());
		BufferedReader br = new BufferedReader(is);
		String output;
		System.err.println("Output from ESUpdates -> ");
		while ((output = br.readLine()) != null) {
			System.err.println(output);
		}

		entity.consumeContent();
		is.close();
	}

	/**
	 * Searches for a story on the server by the story id. An exception will be
	 * thrown if no story with a matching id is found on the server. </br>
	 * 
	 * </br>
	 * </br> Example call
	 * UUID id = 5231b533-ba17-4787-98a3-f2df37de2aD7;
	 * </br>
	 *  Story myStory = searchById(id.toString());
	 *  </br> myStory is the story that was searched for, or null if it 
	 *  didn't exist.
	 * @param id
	 *            Will be a 128-bit value UUID value that was converted to a
	 *            String.
	 */

	/**
	 * Allows for two different types of searching. The first, by keywords. The
	 * second, is searching for all stories on the server.
	 * 
	 * <ol>
	 * <li>Allows searching for stories by providing keywords that are found in
	 * the title. There can be up to any number of keywords. This search will be
	 * performed if the criteria provided (a story object in itself) does not
	 * have "null" for the value of its title.
	 * 
	 * </br> Eg. Assume there is a story on the server whose title is "The bird
	 * who smelt the flower and loved the cow." To retrieve it:
	 * 
	 * </br> Story criteria = new Story(null, "bird flower cow", null, null,
	 * 									null) 
	 * </br> String selection = "bird AND flower AND cow" 
	 * </br> ArrayList<Object> stories = searchStories(criteria, selection); 
	 * </br> The ArrayList stories will contain the story titled "The bird 
	 * 		who smelt the flower and loved the cow."
	 * </li>
	 * 
	 * <li>If the title on criteria is null, then the method searches for all
	 * available stories on the server.
	 * 
	 * </br> Eg. To get all the stories currently on the server: 
	 * </br> Story criteria = new Story(null, null, null, null, null) 
	 * </br> String selection = "" 
	 * </br> ArrayList<Object> stories = searchStories(criteria, selection); 
	 * </br> The ArrayList stories will contain all available stories.
	 * </li>
	 * </ol>
	 * 
	 * @param criteria
	 *            Story object with fields matching the story we want. For
	 *            searching on the server (and not by id), we only search by
	 *            story title, so criteria will either have all null fields
	 *            (meaning user wants ALL stories) or will have keywords found
	 *            in the title as its "title" field.
	 * @param selection
	 *            The selection string. Will either be empty or a keyword query.
	 *            Eg. "bacon AND ham AND fish"
	 */

	/**
	 * Inserts a story object into the server. The story must be a complete
	 * story, in other words, have all its chapters, choices, and media. It is
	 * all stored as one story, not individual components like in the database.
	 * </br> The story is inserted using HttpPost, not HttpGet.
	 * 
	 * @param story
	 *            The complete story to post to server. It is first converted to
	 *            a Json string, and then is posted onto the server.
	 */
	public void insertStory(Story story, String server) {
		HttpPost httpPost = new HttpPost(server + story.getId().toString());

		StringEntity stringentity = null;
		try {
			stringentity = new StringEntity(gson.toJson(story));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setHeader("Accept", "application/json");

		httpPost.setEntity(stringentity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String status = response.getStatusLine().toString();
		System.out.println(status);

		HttpEntity entity = response.getEntity();
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(entity.getContent());
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(is);

		String output;
		System.err.println("Output from ESUpdates -> ");
		try {
			while ((output = br.readLine()) != null) {
				System.err.println(output);
			}
			entity.consumeContent();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
