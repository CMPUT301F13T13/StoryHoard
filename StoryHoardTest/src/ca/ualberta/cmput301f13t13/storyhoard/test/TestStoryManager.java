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

import org.junit.Before;

import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.*;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Class meant for the testing of the StoryManager class in the StoryHoard
 * application.
 * 
 * @author Stephanie
 * 
 */
public class TestStoryManager extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private ArrayList<Object> mockStories;
	private StoryManager sm = null;

	public TestStoryManager() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();

		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
		sm = StoryManager.getInstance(getActivity());
	}

	/**
	 * Create a new story.
	 */
	public Story newMockStory(String title, String author, String description,
			String phoneId) {
		// story object
		Story mockStory = new Story(title, author, description, phoneId);

		// first chapter of story
		Chapter mockChapter = new Chapter(mockStory.getId(), "my first chapter");
		mockStory.addChapter(mockChapter);
		mockStory.setFirstChapterId(mockChapter.getId());

		return mockStory;
	}

	/**
	 * Tests caching a story locally on the device database, and then loading it
	 * again to make sure it was properly saved.
	 */
	public void testCacheLoadStory() {
		Story mockStory = newMockStory("My Frog", "blueberry", "my cute frog",
				Utilities.getPhoneId(this.getActivity()));

		sm.insert(mockStory);

		mockStories = sm.retrieve(mockStory);
		assertTrue(mockStories.size() != 0);
		assertTrue(hasStory(mockStories, mockStory));
	}

	/**
	 * Tests adding and loading a story from the local storage
	 */
	public void testAddLoadStory() {
		Story mockStory = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));

		sm.insert(mockStory);

		try {
			// retrieving story in db that matches mockStory
			mockStories = sm.retrieve(mockStory);
			assertTrue(mockStories.size() != 0);
			assertTrue(hasStory(mockStories, mockStory));
		} catch (Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}

	/**
	 * Tests loading all created stories, and makes sure the results don't
	 * include any stories not created by author.
	 */
	public void testGetAllAuthorStories() {
		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory1);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory2);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer",
				"my chubby hen", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory3);

		// setting search criteria
		Story mockCriteria = new Story(null, null, null, null, null);
		mockStories = sm.retrieve(mockCriteria);
		assertFalse(hasStory(mockStories, mockStory3));
		assertEquals(mockStories.size(), 2);

	}

	/**
	 * Tests loading all cached stories.
	 */
	public void testGetAllCachedStories() {
		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory1);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory2);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer",
				"my chubby hen", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory3);

		// setting search criteria
		Story mockCriteria = new Story(null, null, null, null, null);
		mockStories = sm.retrieve(mockCriteria);
		assertFalse(hasStory(mockStories, mockStory1));
		assertEquals(mockStories.size(), 2);

	}

	/**
	 * Tests editing story
	 */
	public void testEditStory() {
		Story mockStory = newMockStory("My Wizard Mouse", "JK ROlling",
				"before the edit...", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory);

		mockStories = sm.retrieve(mockStory);
		assertTrue(mockStories.size() != 0);
		assertTrue(hasStory(mockStories, mockStory));

		Story newStory = (Story) mockStories.get(0);

		newStory.setTitle("My Wizard newt");
		newStory.setAuthor("not jk rolling");
		newStory.setDescription("after the edit...");
		sm.update(newStory);

		// make sure you can find new story
		mockStories = sm.retrieve(newStory);
		assertTrue(mockStories.size() == 1);
		assertTrue(hasStory(mockStories, newStory));

		// make sure old version no longer exists
		mockStories = sm.retrieve(mockStory);
		assertTrue(mockStories.size() == 0);
	}

	/**
	 * Checks whether a story is contained in a story ArrayList.
	 * 
	 * @param objs
	 *            ArrayList of objects.
	 * @param story
	 *            Object for which we are testing whether or not it is contained
	 *            in the ArrayList.
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
