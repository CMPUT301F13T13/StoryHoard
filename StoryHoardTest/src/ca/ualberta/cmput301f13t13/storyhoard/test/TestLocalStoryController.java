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

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.LocalStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.HelpGuide;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

/**
 * Class for testing the functionality of LocalStoryController.java
 * 
 * @author sgil
 *
 */
public class TestLocalStoryController extends
ActivityInstrumentationTestCase2<HelpGuide> {
	private HelpGuide activity;
	private LocalStoryController lscon;
	

	public TestLocalStoryController() {
		super(HelpGuide.class);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		lscon = LocalStoryController.getInstance(activity);
	}

	/**
	 * Tests using the controller to add stories and then get all cached
	 * stories.
	 */
	public void testCacheAndGetAllCached() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", "343423");
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "45643543");

		lscon.insert(s1);
		lscon.insert(s2);
		lscon.insert(s3);
		
		stories = lscon.getAllCachedStories();

		assertEquals(stories.size(), 2);
	}
	
	/**
	 * Tests using the controller to get all stories created by the author.
	 */
	public void getAllAuthorStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "34530");
		
		lscon.insert(s1);
		lscon.insert(s2);
		lscon.insert(s3);
		
		stories = lscon.getAllAuthorStories();
		assertEquals(stories.size(), 2);
	}
	
	/**
	 * Tests inserting, retrieving, and updatig a story.
	 */
	public void testInsertLoadUpdate() {
		ArrayList<Story> stories = new ArrayList<Story>();
		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		lscon.insert(s1);
		stories = lscon.retrieve(s1);
		assertEquals(stories.size(), 1);
		
		Story s2 = stories.get(0);
		s2.setAuthor("sds");
		s2.setDescription("none");
		s2.setTitle("new");
		lscon.update(s2);
		
		stories = lscon.retrieve(s2);
		assertEquals(stories.size(), 1);
		s2 = stories.get(0);
		
		assertFalse(s1.getTitle().equals(s2.getTitle()));
		assertFalse(s1.getAuthor().equals(s2.getAuthor()));
		assertFalse(s1.getDescription().equals(s2.getDescription()));
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
		
		lscon.insert(s1);
		lscon.insert(s2);
		lscon.insert(s3);

		// title has cowy, cached stories
		stories = lscon.searchCachedStories("cowy");
		assertEquals(stories.size(), 1);

		// created, title has bob and hen
		stories = lscon.searchAuthorStories("Bob hen");
		assertEquals(stories.size(), 1);
	}
}
