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
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.local.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

/**
 * Class meant for the testing of the OwnStoryManager class in the StoryHoard
 * application.
 * 
 * @author Stephanie Gil
 * 
 */
public class TestStoryManager extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	private ArrayList<Story> mockStories;
	private StoryManager sm = null;

	public TestStoryManager() {
		super(InfoActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
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
		mockStory.getChapters().add(mockChapter);
		mockStory.setFirstChapterId(mockChapter.getId());

		return mockStory;
	}

	/**
	 * Tests adding and loading a story from the local storage
	 */
	public void testAddLoadStory() {
		sm = StoryManager.getInstance(getActivity());
		
		Story mockStory = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));

		sm.insert(mockStory);

		// retrieving story in db that matches mockStory
		mockStories = sm.retrieve(mockStory);
		assertEquals(mockStories.size(), 1);
	}

	/**
	 * Tests loading all created stories, and makes sure the results don't
	 * include any stories not created by author.
	 */
	public void testGetAllAuthorStories() {
		sm = StoryManager.getInstance(getActivity());
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);

		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory1);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory2);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer",
				"my chubby hen", "blaksjdks");
		sm.insert(mockStory3);

		// setting search criteria
		Story mockCriteria = new Story(null, null, null, null,
				Utilities.getPhoneId(getActivity()));
		mockStories = sm.retrieve(mockCriteria);
		assertTrue(hasStory(mockStories, mockStory1));
		assertTrue(hasStory(mockStories, mockStory2));

	}

	/**
	 * Tests loading all cached stories.
	 */
	public void testGetAllCachedStories() {
		sm = StoryManager.getInstance(getActivity());

		Story mockStory1 = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory1);
		Story mockStory2 = newMockStory("My Frog", "Dr. Phil",
				"my chubby frog", "43545454353");
		sm.insert(mockStory2);
		Story mockStory3 = newMockStory("My Hen", "Dr. Farmer",
				"my chubby hen", "3432423432");
		sm.insert(mockStory3);

		// setting search criteria
		Story mockCriteria = new Story(null, null, null, null,
				Story.NOT_AUTHORS);
		mockStories = sm.retrieve(mockCriteria);
		
		assertTrue(hasStory(mockStories, mockStory2));
		assertTrue(hasStory(mockStories, mockStory3));
	}

	/**
	 * Tests editing story
	 */
	public void testEditStory() {
		sm = StoryManager.getInstance(getActivity());
		
		Story mockStory = newMockStory("My Wizard Mouse", "JK ROlling",
				"before the edit...", Utilities.getPhoneId(this.getActivity()));
		sm.insert(mockStory);

		mockStories = sm.retrieve(mockStory);
		assertEquals(mockStories.size(), 1);

		Story newStory = (Story) mockStories.get(0);

		newStory.setTitle("My Wizard newt");
		newStory.setAuthor("not jk rolling");
		newStory.setDescription("after the edit...");
		sm.update(newStory);

		// make sure you can find new story
		mockStories = sm.retrieve(newStory);
		assertEquals(mockStories.size(), 1);

		// make sure old version no longer exists
		mockStories = sm.retrieve(mockStory);
		assertEquals(mockStories.size(), 0);
	}

	/**
	 * Tests the correct determining of whether a story exists locally or not.
	 */
	public void testExistsLocally() {
		sm = StoryManager.getInstance(getActivity());
		
		Story mockStory = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));
		
		assertFalse(sm.existsLocally(mockStory.getId()));
		sm.insert(mockStory);
		assertTrue(sm.existsLocally(mockStory.getId()));
	}

	/**
	 * Tests synching a story, which is really already tested by inserting and
	 * updating a story.
	 */
	public void testSync() {
		sm = StoryManager.getInstance(getActivity());
		Story mockStory = newMockStory("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(this.getActivity()));
		sm.sync(mockStory, mockStory.getId());
		ArrayList<Story> mockStorys = sm.retrieve(mockStory);
		assertEquals(mockStorys.size(), 1);
	}
	
	/**
	 * Tests using the controller to add stories and then get all cached
	 * stories.
	 */
	public void testCacheAndGetAllCached() {
		sm = StoryManager.getInstance(getActivity());
		ArrayList<Story> stories = new ArrayList<Story>();
		Syncher syncher = Syncher.getInstance(getActivity());
		
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", "343423");
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "45643543");

		syncher.syncStoryFromServer(s2);
		syncher.syncStoryFromServer(s3);

		stories = sm.getAllCachedStories();

		assertTrue(hasStory(stories, s2));
		assertTrue(hasStory(stories, s3));
	}
	
	/**
	 * Tests using the controller to test for a variety of different stories
	 * that have been added / published.
	 */
	public void testSearchStory() {
		sm = StoryManager.getInstance(getActivity());
		
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("Lily the cowy", "me", "D: none",
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("please work", "me", "D: none",
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("please work", "me", "D: none", "34532432423");

		sm.insert(s1);
		sm.insert(s2);
		sm.insert(s3);

		// title has cowy, cached stories
		stories = sm.searchCachedStories("work");
		assertEquals(stories.size(), 1);

		// created, title has bob and hen
		stories = sm.searchAuthorStories("please");
		assertEquals(stories.size(), 1);
	}
	

    /**
     * Checks whether a story is contained in a stories ArrayList.
     */
    public Boolean hasStory(ArrayList<Story> stories, Story aStory) {
            for (Story story : stories) {
                    if (story.getId().equals(aStory.getId())) {
                            return true;
                    }
            }
            return false;
    }	
}
