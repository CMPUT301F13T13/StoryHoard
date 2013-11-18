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
 * @author Abram Hindle
 * @author Chenlei Zhang
 * @author Ashley Brown
 * @author Stephanie Gil
 */
public class ESClient {
	private static HttpClient httpclient = null;		// Http Connector
	private static Gson gson = null;					// JSON Utilities
	private static ESClient self = null;
	private static final String server = "http://cmput301.softwareprocess.es:8080/cmput301f13t13/stories/";
	
	protected ESClient() {
		httpclient = new DefaultHttpClient();
		gson = new Gson();
	}
	
	public static ESClient getInstance() {
		if (self == null) {
			self = new ESClient();
		}
		return self;
	}	
	
	/**
	 * delete an entry specified by the id
	 */
	public void deleteStory(Story story) throws IOException {
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
		System.err.println("Output from ESClient -> ");
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
				new InputStreamReader(response.getEntity().getContent()));
		String output;
		System.err.println("Output from ESClient -> ");
		String json = "";
		while ((output = br.readLine()) != null) {
			System.err.println(output);
			json += output;
		}
		System.err.println("JSON:"+json);
		return json;
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
			
			// We get the story from it!
			story = esResponse.getSource();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}		
		return story;
	}

	/**
	 * advanced search (logical operators)
	 */
	public ArrayList<Object> searchStories(Story criteria, String selection) 
				throws ClientProtocolException, IOException {
		ArrayList<Object> stories = new ArrayList<Object>();
		
		HttpPost searchRequest = new HttpPost(server + "_search?pretty=1");

		if (criteria.getTitle() != null) {
			// searching by keywords in title
			String query = 	"{\"query\" : {\"query_string\" : {\"default_field\"" 
					+ " : \"title\",\"query\" : \"" + selection + "\"}}}";
			StringEntity stringentity = new StringEntity(query);
			searchRequest.setEntity(stringentity);
		}
		
		searchRequest.setHeader("Accept","application/json");

		HttpResponse response = httpclient.execute(searchRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);

		Type elasticSearchSearchResponseType 
				= new TypeToken<ElasticSearchResponse<Story>>(){}.getType();
		ElasticSearchResponse<Story> esResponse = gson.fromJson(json, 
				elasticSearchSearchResponseType);
		System.err.println(esResponse);
		for (SimpleESResponse<Story> r : esResponse.getHits()) {
			Story story = r.getSource();
			stories.add(story);
		}
		
		return stories;
	}

	public void insertStory(Story story) {
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
		System.err.println("Output from ESClient -> ");
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
