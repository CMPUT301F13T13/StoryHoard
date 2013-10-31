/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import org.junit.Test;
import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.*;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestStoryManager extends ActivityInstrumentationTestCase2<MainActivity>{
	private ArrayList<Object> mockStories;
	
	public TestStoryManager() {
		super(MainActivity.class);	
	}

	public void setup() {
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);		
	}
	
	/**
	 * Create a new story.
	 */
	public Story newMockStory(String title, String author, String description, 
			Boolean authorsOwn) {
		// story object
		Story mockStory = new Story(title, author, description, authorsOwn);
		
		// first chapter of story
		Chapter mockChapter = new Chapter(mockStory.getId(), 
				"my first chapter");
		mockStory.addChapter(mockChapter);	
		mockStory.setFirstChapterId(mockChapter.getId());
		
		return mockStory;
	}
	
	/**
	 * Tests caching a story locally on the device, and then 
	 * loading cached mockStories.
	 */
	public void testCacheLoadStory() {
		Story mockStory = newMockStory("My Frog", "blueberry", "my cute frog", false);
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		sm.cacheStory(mockStory);
		
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		mockStories = sm.retrieve(mockStory, helper);
		assertTrue(mockStories.size() != 0);
		assertTrue(hasStory(mockStories, mockStory));
	}
	
	/**
	 * Tests adding and loading a story from the local storage
	 */
	public void testAddLoadStory() {
		Story mockStory = newMockStory("My Cow", "Dr. Poe", "my chubby cow", 
				true);
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		sm.insert(mockStory, helper);
		
		try {
			// retrieving story in db that matches mockStory
			mockStories = sm.retrieve(mockStory, helper);
			assertTrue(mockStories.size() != 0);
			assertTrue(hasStory(mockStories, mockStory));
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}

	/**
	 * Tests loading all created stories, and makes sure the results
	 * don't include any stories not created by author.
	 */
	public void testGetAllAuthorStories() {
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", 
				"my chubby cow", true);	
		sm.insert(mockStory1, helper);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil", 
				"my chubby frog", true);	
		sm.insert(mockStory2, helper);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer", 
				"my chubby hen", false);	
		sm.insert(mockStory3, helper);
		
		try {
			// setting search criteria
			Story mockCriteria = new Story(null, "", "", "", true);	
			mockStories = sm.retrieve(mockCriteria, helper);
			assertFalse(hasStory(mockStories, mockStory3));
			assertEquals(mockStories.size(), 2);
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}	
	
	/**
	 * Tests loading all cached stories.
	 */
	public void testGetAllCachedStories() {
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", 
				"my chubby cow", true);	
		sm.insert(mockStory1, helper);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil", 
				"my chubby frog", false);	
		sm.insert(mockStory2, helper);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer", 
				"my chubby hen", false);	
		sm.insert(mockStory3, helper);
		
		try {
			// setting search criteria
			Story mockCriteria = new Story(null, "", "", "", false);	
			mockStories = sm.retrieve(mockCriteria, helper);
			assertFalse(hasStory(mockStories, mockStory1));
			assertEquals(mockStories.size(), 2);
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}	
	
	/**
	 * Tests loading all created stories, and makes sure the results
	 * don't include any stories not created by author.
	 */
	public void testGetAllPublishedStories() {
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", 
				"my chubby cow", true);	
		sm.publish(mockStory1);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil", 
				"my chubby frog", false);	
		sm.publish(mockStory2);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer", 
				"my chubby hen", false);	
		sm.publish(mockStory3);
		
		try {
			// setting search criteria
			Story mockCriteria = new Story(null, "", "", "", false);	
			ArrayList<Story> mockStories = sm.getPublishedStories();
			assertEquals(mockStories.size(), 3);
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}	
	
	/**
	 * Tests editing story
	 */
	public void testEditStory() {
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		Story mockStory = newMockStory("My Wizard Mouse", "JK ROlling", 
				"before the edit...", true);
		sm.insert(mockStory, helper);
		
		mockStories = sm.retrieve(mockStory, helper);
		assertTrue(mockStories.size() != 0);
		assertTrue(hasStory(mockStories, mockStory));
		
		Story newStory = (Story) mockStories.get(0);
		
		newStory.setTitle("My Wizard newt");
		newStory.setAuthor("not jk rolling");
		newStory.setDescription("after the edit...");
		sm.update(newStory, helper);
		
		// make sure you can find new story
		mockStories = sm.retrieve(newStory, helper);
		assertTrue(mockStories.size() != 0);
		assertTrue(hasStory(mockStories, newStory));
		
		// make sure old version no longer exists
		mockStories = sm.retrieve(mockStory, helper);
		assertTrue(mockStories.size() == 0);
	}
	
	/**
	 * Tests publishing story, then loading it from server.
	 */
	public void testPublishLoadStory() {
		Story mockStory = newMockStory("My Monkey", "TS ELLIOT", 
					"monkey is in the server", false);
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		sm.publish(mockStory);
		
		ArrayList<Story> pubStories = sm.getPublishedStories();
		assertTrue(pubStories.contains(mockStory));
	}
	
	/**
	 * Tests publishing story, caching it, then loading it from server.
	 */
	public void testPublishCacheLoadStory() {
		Story mockStory = newMockStory("My Monkey", "TS ELLIOT", 
					"monkey is in the server", false);
		StoryManager sm = StoryManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		sm.publish(mockStory);
		sm.cacheStory(mockStory);
		
		ArrayList<Object> pubStories = sm.retrieve(mockStory, helper);
		assertTrue(hasStory(pubStories, mockStory));
	}	
	
	/**
	 * Checks whether a story is contained in a story ArrayList.
	 * 
	 * @param objs
	 * 			ArrayList of objects.
	 * @param story
	 * 			Object for which we are testing whether or not it is 
	 * 			contained in the ArrayList.
	 * @return Boolean
	 */
	public Boolean hasStory(ArrayList<Object> objs, Story story) {
		for (Object object : objs) {
		    Story newStory = (Story) object;
		    if (newStory.getId().equals(story.getId())) {
		    	return true;
		    }
		}		
		return false;
	}
}
