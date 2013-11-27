/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package ca.ualberta.cmput301f13t13.storyhoard.serverClasses;

import org.apache.http.HttpResponse;
import java.io.IOException;
import org.apache.http.HttpEntity;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.apache.http.client.methods.HttpGet;
import java.lang.reflect.Type;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This class provides the necessary methods for retrieving responses from the
 * server. It is meant to be used only by the ServerManager class. It's methods 
 * also are able to retrieve a response by its id, or else by using a given 
 * query. </br></br>
 * 
 * Desgin Pattern: Singleton
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
 *
 */
public class ESRetrieval {
	public static HttpClient httpclient = null; // Http Connector
	public static Gson gson = null; // JSON Utilities
	private static ESRetrieval self = null;

	protected ESRetrieval() {
		httpclient = new DefaultHttpClient();
		gson = new Gson();
	}
	
	public static ESRetrieval getInstance() {
		if (self == null) {
			self = new ESRetrieval();
		}
		return self;
	}
	
	/**
	 * Retrieves the content of the given HttpResponse and returns it as
	 * a string. In this application the response content will be a story 
	 * object in JSON string format. These strings are later to be converted 
	 * to real story objects using JSON. </br></br>
	 * 
	 * This method is only meant to be used by the searchById() and retrieve()
	 * which are also defined in this class. It is a helper function for them.
	 * </br></br>
	 * 
	 * In addition to returning JSON string, this method also prints out the 
	 * JSON string to the logcat. 
	 * 
	 * @return string of the response
	 */
	private String getEntityContent(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		InputStreamReader is = new InputStreamReader(entity.getContent());
		BufferedReader br = new BufferedReader(is);
		String output;
		System.err.println("Output from ESUpdates -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:" + json);
		entity.consumeContent();
		is.close();
		br.close();
		return json;
	}

	/**
	 * Searches for a response on the server by id. If no response matching the
	 * given id is found, the value returned is null. Note also that in this
	 * application, the id's will be strings in the format of a UUID. The id's
	 * will also be corresponding to story id's, and the responses' content
	 * are story objects in JSON string format.
	 * 
	 * </br></br>
	 * An example call:
	 * </br></br>
	 * 	 * 			String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/" </br>
	 *  UUID id = 5231b533-ba17-4787-98a3-f2df37de2aD7; </br> 
	 *  Story myStory = searchById(id.toString()); </br></br> 
	 *  
	 *  myStory will contain the story that was searched for, or null if it  
	 *  didn't exist on the server.
	 *  
	 * @param server
	 * 			The location on elastic search to search for the responses.
	 * 			It expects this information as a String.</br>
	 * 			See above for an example of a valid server string.
	 * @param id 
	 * 			Will be a 128-bit UUID value that was converted to a String.
	 * 			These are unique identifiers of stories.
	 */
	protected Story searchById(String id, String server) {
		Story story = null;
		try {
			HttpGet getRequest = new HttpGet(server + id + "?pretty=1");
			getRequest.addHeader("Accept", "application/json");
			HttpResponse response;
			response = httpclient.execute(getRequest);
			String status = response.getStatusLine().toString();
			System.out.println(status);
			String json = getEntityContent(response);
			Type simpleESResponseType = new TypeToken<SimpleESResponse<Story>>() {
			}.getType();
			SimpleESResponse<Story> esResponse = gson.fromJson(json,
					simpleESResponseType);
			story = esResponse.getSource();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return story;
	}

	/**
	 * This retrieval method can retrieve multiple stories from the server.
	 * It also allows for two different types of searching. The first, 
	 * by using a provided query. The second, if no query is provided (in which
	 * case the value for the argument will be null) is searching for all 
	 * stories available on the server. </br></br>
	 * 
	 * Although the query provided could be anything,and the following is
	 * an example of a query that can be provided. This one searches for
	 * all stories on the server who have contain the specified keywords in
	 * their titles. </br></br>
	 * 
	 * {"query" : {"query_string" : {"default_field" : "title", "query" : "ugly AND red"}}}
	 * 	
	 * </br></br>
	 * Note that like in the method above, the string for the server we are
	 * searching in must also be provided. A full example call would be: </br></br>
	 * 
	 * String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/" </br>
	 * String query = "{\"query\" : {\"query_string\" : {\"default_field\""
				+ " : \"title\",\"query\" : \"dog AND cat"\"}}}";	</br>
	 * ArrayList<Story> stories = retrieve(query, server); </br></br>
	 * 
	 * stories will contain all stories that contained both the words "dog"
	 * and "cat" in their titles. </br></br>
	 * 
	 * Here is an example of what you would call to retrieve all the stories on
	 * the server.
	 * 
	 * String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/" </br>
	 * ArrayList<Story> stories = retrieve(null, server); </br></br>
	 * 
	 * 
	 * @param query 
	 * 			The query as a string. Watch out for escape characters. It can
	 * 			be null if you want to get all the stories on the server.
	 * @param server
	 * 			The location on elastic search to search for the responses.
	 * 			It expects this information as a String.</br>
	 */
	protected ArrayList<Story> retrieve(String query, String server)
			throws ClientProtocolException, IOException {
		ArrayList<Story> stories = new ArrayList<Story>();
		HttpPost searchRequest = new HttpPost(server
				+ "_search?pretty=1");
		if (query != null) {
			StringEntity stringentity = new StringEntity(query);
			searchRequest.setEntity(stringentity);
		}
		searchRequest.setHeader("Accept", "application/json");
		HttpResponse response;
		response = httpclient.execute(searchRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);
		String json = getEntityContent(response);
		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchResponse<Story>>() {
		}.getType();
		ElasticSearchResponse<Story> esResponse = gson.fromJson(json,
				elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (SimpleESResponse<Story> r : esResponse.getHits()) {
			Story story = r.getSource();
			stories.add(story);
		}
		return stories;
	}
}