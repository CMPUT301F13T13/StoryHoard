/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.ChapterManager;
import ca.ualberta.cs.c301f13t13.backend.DBHelper;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.backend.StoryManager;
import ca.ualberta.cs.c301f13t13.gui.MainActivity;

import android.app.Activity;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 * 
 */
public class TestChapterManager extends ActivityInstrumentationTestCase2<MainActivity>{
	private Chapter mockChapter;
	private ArrayList<Object> mockChapters;

	
	public TestChapterManager() {
		super(MainActivity.class);
	}	
	
	/**
	 * Create a new mock chapter without choices.
	 */
	public void newMockChapter(UUID storyId, String text) {
		// chapter object
		mockChapter = new Chapter(storyId, text);
//		Choice choice = new Choice();
//		mockChapter.addChoice(choice);  // within this, choice manager add Choice should be called
	}	
		
	 /**
	 * Tests saving and loading a chapter that has no media locally.
	 */
	public void testAddLoadChapterNoMedia() {
		ChapterManager cm = new ChapterManager(this.getActivity());
		newMockChapter(UUID.randomUUID(), "bob went away");

		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(mockChapter, helper);
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
	}
	
	 /**
	 * Tests saving and loading a chapter that has media locally.
	 */
	public void testAddLoadChapterMedia() {
		// TO DO: Add media
		ChapterManager cm = new ChapterManager(this.getActivity());
		newMockChapter(UUID.randomUUID(), "it is raining");
		// Give it photos/illustrations
		
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(mockChapter, helper);
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
	}
	
	/**
	 * Tests updating a chapter's data except for media,
	 * which includes adding and loading a chapter. 
	 */
	public void testUpdateChapterNoMedia() {
		newMockChapter(UUID.randomUUID(), "hi there");
		ChapterManager cm = new ChapterManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(mockChapter, helper);
		
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
		
		Chapter newChapter = (Chapter) mockChapters.get(0);
		
		newChapter.setText("My Wizard newt");
//		newChapter.setNextChapter(UUID.randomUUID());
		cm.update(mockChapter, newChapter, helper);
		
		// make sure you can find new chapter
		mockChapters = cm.retrieve(newChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, newChapter));
		
		// make sure old version no longer exists
		mockChapters = cm.retrieve(mockChapter, helper);
		assertFalse(hasChapter(mockChapters, mockChapter));	
	}
	
	/**
	 * Tests updating a chapter's data except for media,
	 * which includes adding and loading a chapter. 
	 */
	public void testUpdateChapterMedia() {
		// ADD MEDIA
		Chapter chapter = new Chapter(UUID.randomUUID());
		ChapterManager cm = new ChapterManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		newMockChapter(UUID.randomUUID(), "hi there");
		cm.insert(mockChapter, helper);
		
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
		
		Chapter newChapter = (Chapter) mockChapters.get(0);
		
//		newChapter.setText("My Wizard newt");
//		newChapter.setNextChapter(UUID.randomUUID());
		cm.update(chapter, newChapter, helper);
		
		// make sure you can find new story
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, newChapter));
		
		// make sure old version no longer exists
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() == 0);	
	}	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

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
