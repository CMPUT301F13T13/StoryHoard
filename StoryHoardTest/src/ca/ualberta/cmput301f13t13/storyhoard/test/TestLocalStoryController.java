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
 */

package ca.ualberta.cmput301f13t13.storyhoard.test;


import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Class for testing the functionality of LocalStoryController.java
 * 
 * @author sgil
 *
 */
public class TestLocalStoryController extends
ActivityInstrumentationTestCase2<ViewBrowseStories> {

	public TestLocalStoryController() {
		super(ViewBrowseStories.class);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Gets all the stories that are either cached, created by the author, or
	 * published.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY, or
	 *            CREATED_STORY.
	 * @return Array list of all the stories the application asked for.
	 */
	public ArrayList<Story> getAllCachedStories() {
		Story criteria = new Story(null, null, null, null, Story.NOT_AUTHORS);
		return retrieve(criteria);
	}
	
	/**
	 * Gets all the stories that are either cached, created by the author, or
	 * published.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY, or
	 *            CREATED_STORY.
	 * @return Array list of all the stories the application asked for.
	 */
	public ArrayList<Story> getAllAuthorStories() {
		Story criteria = new Story(null, null, null, null, phoneId);
		return retrieve(criteria);
	}
	
	public void cache(Story story) {
		syncher.syncStoryFromServer(story);
	}
	
	@Override
	public void update(Story story) {
		storyMan.update(story);
	}
	
	@Override
	public void insert(Story story) {
		storyMan.insert(story);
	}
	
	private ArrayList<Story> retrieve(Story story) {
		return storyMan.retrieve(story);
	}
	
	/**
	 * Used to search for stories matching the given search criteria. Users can
	 * either search by specifying the title or author of the story. All stories
	 * that match will be retrieved.
	 * 
	 * @param title
	 *            Title of the story user is looking for.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY
	 * 
	 * @return ArrayList of stories that matched the search criteria.
	 */
	public ArrayList<Story> searchCachedStories(String title) {
		Story criteria = new Story(null, title, null, null, Story.NOT_AUTHORS);
		return storyMan.retrieve(criteria);
	}	
	
	/**
	 * Used to search for stories matching the given search criteria. Users can
	 * either search by specifying the title or author of the story. All stories
	 * that match will be retrieved.
	 * 
	 * @param title
	 *            Title of the story user is looking for.
	 * 
	 * @param type
	 *            Will either be PUBLISHED_STORY, CACHED_STORY
	 * 
	 * @return ArrayList of stories that matched the search criteria.
	 */
	public ArrayList<Story> searchAuthorStories(String title) {
		Story criteria = new Story(null, title, null, null, phoneId);
		return storyMan.retrieve(criteria);
	}

	@Override
	public ArrayList<Story> getAll() {
		Story criteria = new Story(null, null, null, null, null);
		return retrieve(criteria);
	}
	
	@Override
	public void remove(UUID id) {
		storyMan.remove(id);
	}	
	
}
