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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

import com.google.gson.Gson;

/**
 * This class provides the necessary methods for any actual modifications
 * to stories on the server. This includes deletion and insertion. It is 
 * meant to be used only by the ServerManager class. </br></br>
 * 
 * Desgin Pattern: Singleton </br></br>
 * 
 * CODE REUSE: </br>
 * This code is a modified version of the code at: </br>
 * URL: https://github.com/rayzhangcl/ESDemo/blob/master/ESDemo/src/ca/ualberta/cs/CMPUT301/chenlei/ESUpdates.java 
 * </br>Date: Nov. 4th, 2013 </br>
 * Licensed under CC0 (available at http://creativecommons.org/choose/zero/)
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
	 */
	public static ESUpdates getInstance() {
		if (self == null) {
			self = new ESUpdates();
		}
		return self;
	}
	
	/**
	 * Deletes an entry (in this case a story object) specified by the id from
	 * the server. You must specify the id as a string (but in the format of
	 * a UUID), and the server as a string as well. In addition, output from
	 * the server (the response's content) will also be printed out to
	 * System.err. </br></br>
	 * 
	 * Example call: </br>
	 * Let's say the following story is on the server. </br>
	 * </br> String id = f1bda3a9-4560-4530-befc-2d58db9419b7; 
	 * 		Story myStory = new Story(id, "The Cow", "John Wayne", 
	 * 								   "A story about a Cow", phoneId). 
	 * </br> To delete myStory from the server, call </br>
	 * String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/" </br>
	 * String id = f1bda3a9-4560-4530-befc-2d58db9419b7; </br>
	 * deleteStory(id, server); </br></br>
	 * 
	 * @param id
	 * 			Must be a String in the valid format of a UUID. See example
	 * 			above for the formatting of a UUID.
	 * @param server
	 * 			The location on elastic search to search for the responses.
	 * 			It expects this information as a String.</br>
	 * 			See the above example for an example of a valid server string
	 * 			format.
	 */
	protected void deleteStory(String id, String server) throws IOException {
		HttpDelete httpDelete = new HttpDelete(server + id);
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
	 * Inserts a story object into the server. The story must be a complete
	 * story, in other words, have all its chapters, choices, and media. It is
	 * all stored as one story, not individual components like in the database.
	 * </br> The story is inserted using HttpPost, not HttpGet.
	 * 
	 * @param story
	 *            The complete story to post to server. It is first converted to
	 *            a Json string, and then is posted onto the server.
	 */
	protected void insertStory(Story story, String server) {
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
