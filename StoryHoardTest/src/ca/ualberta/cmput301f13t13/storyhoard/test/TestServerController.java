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

import android.os.StrictMode;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ServerStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Tests the functionality of the server story controller.
 * 
 * @author sgil
 *
 */
public class TestServerController extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	private ServerStoryController serverCon;
	
	public TestServerController() {
		super(InfoActivity.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		ServerManager.getInstance().setTestServer();
		serverCon = ServerStoryController.getInstance(getActivity());
	}

	/**
	 * Tests getting all published stories.
	 */
	public void testPublishGetAll() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));

		serverCon.insert(story);
		ArrayList<Story> stories = serverCon.getAll();
		
		// delete
		serverCon.remove(story.getId());
		assertEquals(stories.size(), 1);
	}	
	
	/**
	 * Tests searching for a published story using keywords
	 * found in its title.
	 */
	public void testSearchByTitle(String title) {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));

		serverCon.insert(story);
		ArrayList<Story> stories = serverCon.searchByTitle("Harry");
		
		// delete
		serverCon.remove(story.getId());
		
		assertEquals(stories.size(), 1);
	}		
	
	
	/**
	 * Tests getting a random story.
	 */
	public void testGetRandomStory() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		Story story2 = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		
		serverCon.insert(story);
		serverCon.insert(story2);
		story = serverCon.getRandomStory();
		
		// delete
		serverCon.remove(story.getId());
		// delete
		serverCon.remove(story2.getId());
		
		assertNotNull(story);
	}

	/**
	 * Tests updating a published story.
	 */
	public void testUpdate() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));

		serverCon.insert(story);
		ArrayList<Story> stories = serverCon.searchByTitle("Harry");
		assertEquals(stories.size(), 1);
		Story newStory = stories.get(0);
		newStory.setTitle("new title");
		serverCon.update(newStory);
		
		stories = serverCon.searchByTitle("new title");
		
		// delete
		serverCon.remove(story.getId());
		
		assertTrue(stories.size() > 1);
	}

	/**
	 * Tests removing story from server.
	 */
	public void testRemove() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));

		serverCon.update(story);
		ArrayList<Story> stories = serverCon.searchByTitle("Harry Potter");
		assertTrue(stories.size() > 1);

		// delete
		serverCon.remove(story.getId());
		stories = serverCon.searchByTitle("Harry Potter");
		assertEquals(stories.size(), 0);
		
	}		
}
