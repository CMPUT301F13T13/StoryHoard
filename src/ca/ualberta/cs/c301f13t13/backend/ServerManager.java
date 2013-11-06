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
package ca.ualberta.cs.c301f13t13.backend;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
 * Original authors:
 * @author Abram Hindle
 * @author Chenlei Zhang
 * 
 * Modifications by:
 * @author Ashley
 * @author Stephanie
 */
public class ServerManager implements StoringManager{
	// Http Connector
	private static HttpClient httpclient = null;
	// JSON Utilities
	private static Gson gson = null;
	private static ServerManager self = null;
	private static final String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/";
	
	protected ServerManager() {
		httpclient = new DefaultHttpClient();
		gson = new Gson();
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
		Story story = (Story) object;
		HttpPost httpPost = new HttpPost(server + story.getId().toString());

		StringEntity stringentity = null;
		try {
			stringentity = new StringEntity(gson.toJson(story));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		httpPost.setHeader("Accept","application/json");

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
		System.err.println("Output from Server -> ");
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
			stories.add(searchById(crit.getId().toString()));		
		} else {
			// search by keyword
			stories = searchByKeywords(crit);
		}
		
		return stories;
	}

	/**
	 * search by story id
	 */
	public Story searchById(String id) {
		Story story = null;
		try{
			HttpGet getRequest = new HttpGet(server + id + "?pretty=1");

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			// We have to tell GSON what type we expect
			Type simpleESResponseType = 
					new TypeToken<SimpleESResponse<Story>>(){}.getType();
			// Now we expect to get a Story response
			SimpleESResponse<Story> esResponse = 
					gson.fromJson(json, simpleESResponseType);
			// We get the recipe from it!
			story = esResponse.getSource();
			System.out.println(story.toString());
			
			// NOT SURE THIS IS RIGHT OR NEEDED
			HttpEntity entity = response.getEntity();
			InputStreamReader is = new InputStreamReader(entity.getContent());
			is.close();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}		
		return story;
	}
	
	/**
	 * searches by keywords
	 */ 
	public ArrayList<Object> searchByKeywords(Story criteria) {
		ArrayList<Object> stories = new ArrayList<Object>();
		HashMap<String, String> storyData = criteria.getSearchCriteria();
		ArrayList<String> sargs = new ArrayList<String>();
		
		// setting selection string
		for (String key: storyData.keySet()) {
			sargs.add(storyData.get(key));
		}
		String selection = setSearchCriteria(criteria, sargs);
		
		HttpGet searchRequest = null;
		try {
			searchRequest = new HttpGet(server + "_search?pretty=1&q=" +
					java.net.URLEncoder.encode(selection,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		searchRequest.setHeader("Accept","application/json");
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(searchRequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = null;
		try {
			json = getEntityContent(response);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Type elasticSearchResponseType = 
				new TypeToken<ElasticSearchResponse<Story>>(){}.getType();
		ElasticSearchResponse<Story> esResponse = 
				gson.fromJson(json, elasticSearchResponseType);
		System.err.println(esResponse);
		for (SimpleESResponse<Story> r : esResponse.getHits()) {
			Story story = r.getSource();
			stories.add(story);
		}
		
		// NOT SURE THIS IS RIGHT OR NEEDED
		HttpEntity entity = response.getEntity();
		InputStreamReader is;
		try {
			is = new InputStreamReader(entity.getContent());
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return stories;
	}	

	/**
	 * advanced search (logical operators)
	 */
	public void searchsearchStories(String str) throws ClientProtocolException, IOException {
		HttpPost searchRequest = new HttpPost(server + "_search?pretty=1");
		String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"title\",\"query\" : \"" + str + "\"}}}";
		StringEntity stringentity = new StringEntity(query);

		searchRequest.setHeader("Accept","application/json");
		searchRequest.setEntity(stringentity);

		HttpResponse response = httpclient.execute(searchRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);

		Type elasticSearchSearchResponseType = new TypeToken<ElasticSearchResponse<Story>>(){}.getType();
		ElasticSearchResponse<Story> esResponse = gson.fromJson(json, elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (SimpleESResponse<Story> r : esResponse.getHits()) {
			Story recipe = r.getSource();
			System.err.println(recipe);
		}
		// NOT SURE THIS IS RIGHT OR NEEDED
		HttpEntity entity = response.getEntity();
		InputStreamReader is = new InputStreamReader(entity.getContent());
		is.close();
	}	

	/**
	 * update a field in a recipe
	 */
	@Override
	public void update(Object object) { 
		// retrieve current story with id
		// delete
		// re insert
		
//		Story story = (Story) object;
//		
//		HttpPost updateRequest = new HttpPost(server + "/1/_update");
//
//		ArrayList<Object> stories = new ArrayList<Object>();
//		HashMap<String, String> storyData = story.getSearchCriteria();
//		ArrayList<String> sargs = new ArrayList<String>();
//		
//		// setting selection string
//		for (String key: storyData.keySet()) {
//			sargs.add(storyData.get(key));
//		}
//		String selection = setSearchCriteria(story, sargs);
//		
//		String query = 	"{\"script\" : \"ctx._source." + str + "}";
//		StringEntity stringentity = new StringEntity(query);
//		
//		updateRequest.setHeader("Accept","application/json");
//		updateRequest.setEntity(stringentity);
//
//		HttpResponse response = httpclient.execute(updateRequest);
//		String status = response.getStatusLine().toString();
//		System.out.println(status);
//
//		String json = getEntityContent(response);
//		
//		// NOT SURE THIS IS RIGHT OR NEEDED
//		HttpEntity entity = response.getEntity();
//		InputStreamReader is = new InputStreamReader(entity.getContent());
//		is.close();
	}	

	/**
	 * delete an entry specified by the id
	 */
	public void deleteStory(Object object) throws IOException {
		Story story = (Story) object;
		HttpDelete httpDelete = new HttpDelete(server 
				+ story.getId().toString());
		httpDelete.addHeader("Accept","application/json");

		HttpResponse response = httpclient.execute(httpDelete);

		String status = response.getStatusLine().toString();
		System.out.println(status);

		HttpEntity entity = response.getEntity();
		InputStreamReader is = new InputStreamReader(entity.getContent());
		BufferedReader br = new BufferedReader(is);
		String output;
		System.err.println("Output from Server -> ");
		while ((output = br.readLine()) != null) {
			System.err.println(output);
		}
		
		entity.consumeContent();
		
		is.close();
	}

	/**
	 * get the http response and return json string
	 */
	String getEntityContent(HttpResponse response) throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader((response.getEntity().getContent())));
		String output;
		System.err.println("Output from Server -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
	}

	@Override
	public String setSearchCriteria(Object object, ArrayList<String> args) {
		String selection = "";
		
		// split keywords and clean them 
		
		if (args.size() > 0) {
			selection += args.get(0);
		}
		
		for (int i = 1; i < args.size(); ++i) {
			selection += " AND " + args.get(i);
		}
		return selection;
	}
}
