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
import java.util.UUID;

import org.junit.Before;

import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.MainActivity;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Stephanie
 * 
 */
public class TestChapterManager extends ActivityInstrumentationTestCase2<MainActivity>{
	private ArrayList<Object> mockChapters;

	
	public TestChapterManager() {
		super(MainActivity.class);
	}	
	
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);			
	}
	
	/**
	 * Create a new mock chapter without choices.
	 */
	public Chapter newMockChapter(UUID storyId, String text) {
		// chapter object
		Chapter mockChapter = new Chapter(storyId, text);
		Choice choice = new Choice(storyId, mockChapter.getId(), 
				UUID.randomUUID(), "pick me!");
		mockChapter.addChoice(choice); 
		
		return mockChapter;
	}	
		
	/**
	 * Tests saving and loading a chapter that has no media from 
	 * the database.
	 */
	public void testAddLoadChapter() {
		ChapterManager cm = ChapterManager.getInstance(this.getActivity());
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), 
				"bob went away");

		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(mockChapter, helper);
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
	}
	
	/**
	 * Tests retrieving all the chapters of a story
	 */
	public void testGetAllChapters() {
		ChapterManager cm = ChapterManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), 
				"bob went away");
		cm.insert(mockChapter, helper);
		Chapter mockChapter2 = newMockChapter(mockChapter.getStoryId(), 
				"Lily drove");
		cm.insert(mockChapter2, helper);
		Chapter mockChapter3 = newMockChapter(UUID.randomUUID(), 
				"Lily drove");
		cm.insert(mockChapter3, helper);
		
		Chapter criteria = new Chapter(null, mockChapter.getStoryId(), "");
		
		mockChapters = cm.retrieve(criteria, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
		assertTrue(hasChapter(mockChapters, mockChapter2));
		assertFalse(hasChapter(mockChapters, mockChapter3));
	}
	
	/**
	 * Tests updating a chapter's data (except media and choices,
	 * which includes adding and loading a chapter. 
	 */
	public void testUpdateChapter() {
		ChapterManager cm = ChapterManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		Chapter mockChapter = newMockChapter(UUID.randomUUID(), "hi there");
		cm.insert(mockChapter, helper);
		
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
		
		Chapter newChapter = (Chapter) mockChapters.get(0);
		
		newChapter.setText("My Wizard newt");
		cm.update(newChapter, helper);
		
		// make sure you can find new chapter
		mockChapters = cm.retrieve(newChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, newChapter));
		
		// make sure old version no longer exists
		Chapter compChap = (Chapter) mockChapters.get(0);
		assertTrue(compChap.getText().equals(newChapter.getText()));
	}

	/**
	 * Checks whether a chapter is contained in a chapters ArrayList.
	 * 
	 * @param objs
	 * 			ArrayList of objects.
	 * @param chap
	 * 			Object for which we are testing whether or not it is 
	 * 			contained in the ArrayList.
	 * @return Boolean
	 */
	public Boolean hasChapter(ArrayList<Object> objs, Chapter chap) {
		for (Object object : objs) {
		    Chapter newChap = (Chapter) object;
		    if (newChap.getId().equals(chap.getId())) {
		    	return true;
		    }
		}		
		return false;
	}	
}
