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
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;

/**
 * Class meant for the testing of the ChapterManager class in the StoryHoard
 * application.
 * 
 * @author Stephanie Gil
 * 
 * @see ChapterManager
 * 
 */
public class TestChapterManager extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	private ArrayList<Chapter> mockChapters;
	private Chapter mockChapter;
	private Chapter mockChapter2;
	private Chapter mockChapter3;
	private ChapterManager cm = null;

	public TestChapterManager() {
		super(InfoActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Create a new mock chapter without choices.
	 */
	public Chapter newMockChapter(UUID storyId, String text) {
		cm = ChapterManager.getInstance(getActivity());
		
		// chapter object
		Chapter mockChapter = new Chapter(storyId, text);
		Choice choice = new Choice(storyId, mockChapter.getId(),
				UUID.randomUUID(), "pick me!");
		mockChapter.getChoices().add(choice);

		return mockChapter;
	}

	/**
	 * Tests saving and loading a chapter that has no media or choices from the
	 * database.
	 */
	public void testAddLoadChapter() {
		cm = ChapterManager.getInstance(getActivity());
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), "bob went away");

		cm.insert(mockChapter);
		mockChapters = cm.retrieve(mockChapter);
		assertEquals(mockChapters.size(), 1);
	}

	/**
	 * Tests retrieving all the chapters of a story
	 */
	public void testGetAllChapters() {
		cm = ChapterManager.getInstance(getActivity());
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), "bob went away");
		cm.insert(mockChapter);
		Chapter mockChapter2 = newMockChapter(mockChapter.getStoryId(),
				"Lily drove");
		cm.insert(mockChapter2);
		Chapter mockChapter3 = newMockChapter(UUID.randomUUID(), "Lily drove");
		cm.insert(mockChapter3);

		Chapter criteria = new Chapter(null, mockChapter.getStoryId(), null);

		mockChapters = cm.retrieve(criteria);
		assertEquals(mockChapters.size(), 2);
	}

	/**
	 * Tests updating a chapter's data, which includes adding and loading a
	 * chapter.
	 */
	public void testUpdateChapter() {
		cm = ChapterManager.getInstance(getActivity());
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), "hi there");
		cm.insert(mockChapter);

		mockChapters = cm.retrieve(mockChapter);
		assertEquals(mockChapters.size(), 1);

		Chapter newChapter = (Chapter) mockChapters.get(0);

		newChapter.setText("My Wizard newt");
		cm.update(newChapter);

		// make sure you can find new chapter
		mockChapters = cm.retrieve(newChapter);
		assertEquals(mockChapters.size(), 1);

		// make sure old version no longer exists
		Chapter compChap = (Chapter) mockChapters.get(0);
		assertTrue(compChap.getText().equals(newChapter.getText()));
	}

	/**
	 * Tests the correct determining of whether a chapter exists locally or not.
	 */
	public void testExistsLocally() {
		cm = ChapterManager.getInstance(getActivity());
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), "bob went away");
		cm.insert(mockChapter);
		Chapter mockChapter2 = newMockChapter(mockChapter.getStoryId(),
				"Lily drove");

		assertTrue(cm.existsLocally(mockChapter));
		assertFalse(cm.existsLocally(mockChapter2));
	}

	/**
	 * Tests synching a chapter, which is really already tested by inserting and
	 * updating a chapter.
	 */
	public void testSync() {
		cm = ChapterManager.getInstance(getActivity());
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), "bob went away");
		cm.syncChapter(mockChapter);
		mockChapters = cm.retrieve(mockChapter);
		assertEquals(mockChapters.size(), 1);
	}
	/**
	 * Tests getting all chapters from a story.
	 */
	public void testGetChaptersByStory() {
		cm = ChapterManager.getInstance(getActivity());
		
		mockChapter = new Chapter(UUID.randomUUID(), "bob went away");
		cm.update(mockChapter);
		mockChapter2 = new Chapter(mockChapter.getStoryId(), "Lily drove");
		cm.update(mockChapter2);
		mockChapter3 = new Chapter(UUID.randomUUID(), "Lily drove");
		cm.update(mockChapter3);

		mockChapters = cm.getChaptersByStory(mockChapter.getStoryId());
		assertEquals(mockChapters.size(), 2);
	}

	/**
	 * Tests getting all created chapters.
	 */
	public void testGetAll() {
		cm = ChapterManager.getInstance(getActivity());
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);

		mockChapter = new Chapter(UUID.randomUUID(), "bob went away");
		cm.insert(mockChapter);
		mockChapter2 = new Chapter(mockChapter.getStoryId(), "Lily drove");
		cm.insert(mockChapter2);
		mockChapter3 = new Chapter(UUID.randomUUID(), "Lily drove");
		cm.insert(mockChapter3);

		mockChapters = cm.getAll();
		assertEquals(mockChapters.size(), 3);
	}	
}
