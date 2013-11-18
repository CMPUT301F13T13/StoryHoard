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
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;
import android.os.StrictMode;


/**
 * 
 * Role: Uses functions from the ESClient to add, remove, update, and 
 * search for stories that have been published onto the esclient.
 * 
 * @author Stephanie Gil
 * 
 * @see ESClient
 * @see StoringManager
 */
public class ServerManager implements StoringManager {
	public static enum Task {INSERT, UPDATE, RETRIEVE, REMOVE};
	private static ESClient esclient = null;
	private static ServerManager self = null;

	protected ServerManager() {
		esclient = new ESClient();
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
		new AsyncTask<Object, Void, Void>()
		{
			@Override
			protected Void doInBackground(Object... params)
			{
				Story story = (Story) params[0];
				prepareStory(story);
				esclient.insertStory(story);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}
		}.execute(object);
	}

	private void prepareStory(Story story) {

		// get any media associated with the chapters of the story
		HashMap<UUID, Chapter> chaps = story.getChapters();

		for (UUID key : chaps.keySet()) {
			Chapter chap = chaps.get(key);
			ArrayList<Media> photos = chap.getPhotos();

			for (Media photo : photos) {
				photo.setBitmapString(photo.getBitmap());
			}
			chap.setPhotos(photos);

			ArrayList<Media> ills = chap.getIllustrations();
			for (Media ill : ills) {
				ill.setBitmapString(ill.getBitmap());
			}
			chap.setIllustrations(ills);
		}
		story.setChapters(chaps);
	}

	/**
	 * Consumes the Get operation of the service
	 */
	@Override
	public ArrayList<Object> retrieve(Object criteria) {
        /*
         * set policy to allow for internet activity to happen within the
         * android application
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
		return delegateSearch(criteria);
	}

	private ArrayList<Object> delegateSearch(Object object) {
		Story crit = (Story) object;
		ArrayList<Object> stories = new ArrayList<Object>();

		if (crit.getId() != null) { 

			// search by id
			Story story = esclient.searchById(crit.getId().toString());
			if (story != null) {
				stories.add(story);
			}
		} else {

			// search for multiple stories
			try {
				ArrayList<String> sargs = new ArrayList<String>();
				HashMap<String, String> storyData = crit.getSearchCriteria();

				// setting selection string
				for (String key: storyData.keySet()) {
					sargs.add(storyData.get(key));
				}

				String selection = setSearchCriteria(crit, sargs);	

				stories = esclient.searchStories(crit, selection);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		} 

		return stories;
	}
	
	/**
	 * update a story on the esclient
	 */
	@Override
	public void update(Object object) { 
		new AsyncTask<Object, Void, Void>()
		{
			@Override
			protected Void doInBackground(Object... params)
			{
				Story story = (Story) params[0];

				try {
					esclient.deleteStory(story);
					esclient.insertStory(story);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}
		}.execute(object);		
	}	

	/**
	 * remove a story from esclient
	 */
	//	@Override
	public void remove(Object object) { 
		new AsyncTask<Object, Void, Void>()
		{
			@Override
			protected Void doInBackground(Object... params)
			{
				Story story = (Story) params[0];
				
				try {
					esclient.deleteStory(story);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
			}
		}.execute(object);			
	}	

	@Override
	public String setSearchCriteria(Object object, ArrayList<String> args) {
		String selection = "";
		Story story = (Story) object;

		if (story.getTitle() == null) {
			return selection;
		}

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
