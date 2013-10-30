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
	private Story mockStory;
	private Chapter mockChapter;
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
	public void newMockStory(String title, String author, String description, 
			Boolean authorsOwn) {
		// story object
		mockStory = new Story(title, author, description, authorsOwn);
		
		// first chapter of story
		mockChapter = new Chapter(mockStory.getId(), "my first chapter");
		mockStory.addChapter(mockChapter);	
		mockStory.setFirstChapterId(mockChapter.getId());
	}
	
	/**
	 * Tests caching a story locally on the device, and then 
	 * loading cached mockStories.
	 */
	public void testCacheLoadStory() {
		newMockStory("My Frog", "blueberry", "my cute frog", false);
		StoryManager sm = new StoryManager(this.getActivity());
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
		newMockStory("My Cow", "Dr. Poe", "my chubby cow", true);
		StoryManager sm = new StoryManager(this.getActivity());
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
	public void testGetAuthorStories() {
		StoryManager sm = new StoryManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		newMockStory("My Cow", "Dr. Poe", "my chubby cow", true);	
		sm.insert(mockStory, helper);
		newMockStory("My Frog", "Dr. Phil", "my chubby frog", true);	
		sm.insert(mockStory, helper);
		newMockStory("My Hen", "Dr. Farmer", "my chubby hen", false);	
		sm.insert(mockStory, helper);
		Story oldStory = mockStory;
		
		try {
			// setting search criteria
			newMockStory("", "", "", false);	
			mockStories = sm.retrieve(mockStory, helper);
			assertFalse(hasStory(mockStories, oldStory));
			assertEquals(mockStories.size(), 2);
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}	
	
	/**
	 * Tests editing story
	 */
	public void testEditStory() {
		newMockStory("My Wizard Mouse", "JK ROlling", "before the edit...",
				    true);
		StoryManager sm = new StoryManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		sm.insert(mockStory, helper);
		
		mockStories = sm.retrieve(mockStory, helper);
		assertTrue(mockStories.size() != 0);
		assertTrue(hasStory(mockStories, mockStory));
		
		Story newStory = (Story) mockStories.get(0);
		
		newStory.setTitle("My Wizard newt");
		newStory.setAuthor("not jk rolling");
		newStory.setDescription("after the edit...");
		sm.update(mockStory, newStory, helper);
		
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
		newMockStory("My Monkey", "TS ELLIOT", 
					"monkey is in the server", true);
		StoryManager sm = new StoryManager(this.getActivity());
		sm.publish(mockStory);
		
		ArrayList<Story> pubStories = sm.getPublishedStories();
		assertTrue(pubStories.contains(mockStory));
	}
	
	/**
	 * Tests publishing story, caching it, then loading it from server.
	 */
	public void testPublishCacheLoadStory() {
		newMockStory("My Monkey", "TS ELLIOT", 
					"monkey is in the server", true);
		StoryManager sm = new StoryManager(this.getActivity());
		sm.publish(mockStory);
		sm.cacheStory(mockStory);
		
//		ArrayList<Story> pubStories = sm.retrieve();
//		assertTrue(pubStories.contains(mockStory));
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
