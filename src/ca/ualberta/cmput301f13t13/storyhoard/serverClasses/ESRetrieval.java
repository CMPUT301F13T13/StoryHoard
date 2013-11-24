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

public class ESRetrieval {
	public static HttpClient httpclient = null; // Http Connector	
	public static Gson gson = null; // JSON Utilities

	/**
	 * Retrieves the http response provided and returns it as a json string.
	 * @param response
	 * @return  json string of the response
	 */
	public String getEntityContent(HttpResponse response) throws IOException {
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
	 * Searches for a story on the server by the story id. An exception will be thrown if no story with a matching id is found on the server. </br> </br> </br> Example call UUID id = 5231b533-ba17-4787-98a3-f2df37de2aD7; </br> Story myStory = searchById(id.toString()); </br> myStory is the story that was searched for, or null if it  didn't exist.
	 * @param id Will be a 128-bit value UUID value that was converted to a String.
	 */
	public Story searchById(String id, String server) {
		Story story = null;
		try {
			HttpGet getRequest = new HttpGet(server + id + "?pretty=1");
			getRequest.addHeader("Accept", "application/json");
			HttpResponse response = httpclient.execute(getRequest);
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
	 * Allows for two different types of searching. The first, by keywords. The second, is searching for all stories on the server. <ol> <li>Allows searching for stories by providing keywords that are found in the title. There can be up to any number of keywords. This search will be performed if the criteria provided (a story object in itself) does not have "null" for the value of its title. </br> Eg. Assume there is a story on the server whose title is "The bird who smelt the flower and loved the cow." To retrieve it: </br> Story criteria = new Story(null, "bird flower cow", null, null, null)  </br> String selection = "bird AND flower AND cow"  </br> ArrayList<Object> stories = searchStories(criteria, selection);  </br> The ArrayList stories will contain the story titled "The bird  who smelt the flower and loved the cow." </li> <li>If the title on criteria is null, then the method searches for all available stories on the server. </br> Eg. To get all the stories currently on the server:  </br> Story criteria = new Story(null, null, null, null, null)  </br> String selection = ""  </br> ArrayList<Object> stories = searchStories(criteria, selection);  </br> The ArrayList stories will contain all available stories. </li> </ol>
	 * @param criteria Story object with fields matching the story we want. For searching on the server (and not by id), we only search by story title, so criteria will either have all null fields (meaning user wants ALL stories) or will have keywords found in the title as its "title" field.
	 * @param selection The selection string. Will either be empty or a keyword query. Eg. "bacon AND ham AND fish"
	 */
	public ArrayList<Story> retrieve(String query, String server)
			throws ClientProtocolException, IOException {
		ArrayList<Story> stories = new ArrayList<Story>();
		HttpPost searchRequest = new HttpPost(server
				+ "_search?pretty=1");
		if (query != null) {
			StringEntity stringentity = new StringEntity(query);
			searchRequest.setEntity(stringentity);
		}
		searchRequest.setHeader("Accept", "application/json");
		HttpResponse response = httpclient.execute(searchRequest);
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