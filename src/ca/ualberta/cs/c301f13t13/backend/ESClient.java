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
 * Original authors:
 * @author Abram Hindle
 * @author Chenlei Zhang
 * 
 * Modifications by:
 * @author Ashley
 * @author Stephanie
 */
public class ESClient {
	// Http Connector
	private HttpClient httpclient = new DefaultHttpClient();

	// JSON Utilities
	private Gson gson = new Gson();

	/**
	 * create a simple recipe
	 * @return
	 */
	private Story initializeStory() {
		return null;

	}

	/**
	 * Consumes the POST/Insert operation of the service
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public void insertStory(Story story) throws IllegalStateException, IOException{
		HttpPost httpPost = new HttpPost("http://cmput301.softwareprocess.es:8080/cmput301f13t13/"+story.getId().toString());
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
		InputStreamReader is = new InputStreamReader(entity.getContent());
		BufferedReader br = new BufferedReader(is);
		
		String output;
		System.err.println("Output from Server -> ");
		while ((output = br.readLine()) != null) {
			System.err.println(output);
		}

		try {
			entity.consumeContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// NOT SURE THIS IS RIGHT
		is.close();
	}

	/**
	 * Consumes the Get operation of the service
	 */
	public void getStory(){
		try{
			HttpGet getRequest = new HttpGet("http://cmput301.softwareprocess.es:8080/testing/lab02/999?pretty=1");//S4bRPFsuSwKUDSJImbCE2g?pretty=1

			getRequest.addHeader("Accept","application/json");

			HttpResponse response = httpclient.execute(getRequest);

			String status = response.getStatusLine().toString();
			System.out.println(status);

			String json = getEntityContent(response);

			// We have to tell GSON what type we expect
			Type elasticSearchResponseType = new TypeToken<SimpleESResponse<Story>>(){}.getType();
			// Now we expect to get a Story response
			SimpleESResponse<Story> esResponse = gson.fromJson(json, elasticSearchResponseType);
			// We get the recipe from it!
			Story recipe = esResponse.getSource();
			System.out.println(recipe.toString());
			
			// NOT SURE THIS IS RIGHT OR NEEDED
			HttpEntity entity = response.getEntity();
			InputStreamReader is = new InputStreamReader(entity.getContent());
			is.close();

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * search by keywords
	 */
	public void searchStories(String str) throws ClientProtocolException, IOException {
		HttpGet searchRequest = new HttpGet("http://cmput301.softwareprocess.es:8080/testing/lab02/_search?pretty=1&q=" +
				java.net.URLEncoder.encode(str,"UTF-8"));
		searchRequest.setHeader("Accept","application/json");
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
	 * advanced search (logical operators)
	 */
	public void searchsearchStories(String str) throws ClientProtocolException, IOException {
		HttpPost searchRequest = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/lab02/_search?pretty=1");
		String query = 	"{\"query\" : {\"query_string\" : {\"default_field\" : \"ingredients\",\"query\" : \"" + str + "\"}}}";
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
	public void updateStories(String str) throws ClientProtocolException, IOException {
		HttpPost updateRequest = new HttpPost("http://cmput301.softwareprocess.es:8080/testing/lab02/1/_update");
		String query = 	"{\"script\" : \"ctx._source." + str + "}";
		StringEntity stringentity = new StringEntity(query);

		updateRequest.setHeader("Accept","application/json");
		updateRequest.setEntity(stringentity);

		HttpResponse response = httpclient.execute(updateRequest);
		String status = response.getStatusLine().toString();
		System.out.println(status);

		String json = getEntityContent(response);
		
		// NOT SURE THIS IS RIGHT OR NEEDED
		HttpEntity entity = response.getEntity();
		InputStreamReader is = new InputStreamReader(entity.getContent());
		is.close();
	}	

	/**
	 * delete an entry specified by the id
	 */
	public void deleteStory() throws IOException {
		HttpDelete httpDelete = new HttpDelete("http://cmput301.softwareprocess.es:8080/testing/lab02/1");
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
}
