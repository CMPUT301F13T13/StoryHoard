
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

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;

import ca.ualberta.cmput301f13t13.storyhoard.backend.ServerManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.SHController;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the GeneralController class in the 
 * StoryHoard application.
 * 
 * @author Stephanie Gil

 * @see SHController
 */
public class TestControllers extends
ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private SHController gc = null;
	private static ViewBrowseStories activity;

	public TestControllers() {
		super(ViewBrowseStories.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		activity = this.getActivity();
		// Clearing database
		DBHelper helper = DBHelper.getInstance(activity);
		helper.close();
		activity.deleteDatabase(DBContract.DATABASE_NAME);

		gc = SHController.getInstance(activity);				
		ServerManager sm = ServerManager.getInstance();	
		sm.setTestServer();
	}

	public void tearDown() throws Exception {
		super.tearDown();
		ServerManager sm = ServerManager.getInstance();
		sm.setRealServer();
	}



	/**
	 * Tests using the controller to publish stories and then get all published
	 * stories.
	 */
	public void testGetAllPublishedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));

		s1.publish();
		s2.publish();

		stories = gc.getAllPublishedStories();

		assertTrue(stories.size() > 1);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
		sm.remove(s2);
	}

	

	

	/**
	 * Tests using the controller to test for a variety of different stories
	 * that have been added / published.
	 */
	public void testSearchStory() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("Lily the cowy", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("Bob the hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("Bob the cowy", "me", "D: none", 
				"34532432423");
		Story s4 = new Story("sad pen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s5 = new Story("sad toe", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));

		s1.addSelf(activity);
		s2.addSelf(activity);
		s3.addSelf(activity);
		s4.publish();
		s5.publish();

		// title are null (should retrieve all local author stories)
		stories = gc.searchAuthorStories(null);
		assertEquals(stories.size(), 2);

		// title has cowy, cached stories
		stories = gc.searchCachedStories("cowy");
		assertEquals(stories.size(), 1);

		// created, title has bob and hen
		stories = gc.searchAuthorStories("Bob hen");
		assertEquals(stories.size(), 1);

		// title has toe
		stories = gc.searchPublishedStories("toe");
		assertEquals(stories.size(), 1);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s4);
		sm.remove(s5);
	}

	
	/**
	 * Tests retrieving a random story from published stories.
	 */
	public void testRandomStory() {
		Story s1 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("sad hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		
		s1.publish();
		s2.publish();
		
		Story random = gc.getRandomStory();
		assertTrue(random != null);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
		sm.remove(s2);
	}

}