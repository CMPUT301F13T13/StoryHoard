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

import android.app.Activity;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 * 
 */
public class TestChapterManager extends ActivityInstrumentationTestCase2<Activity>{
	
	public TestChapterManager() {
		super(Activity.class);
	}	
		
	 /**
	 * Tests saving and loading a chapter that has no media locally.
	 */
	public void testAddLoadChapterNoMedia() {
		ChapterManager cm = new ChapterManager(this.getActivity());
		Choice choice = new Choice();
		cm.addChoice(chapter, choice);
		Chapter chapter = new Chapter(UUID.randomUUID()); // Give it photos/illustrations
		
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(chapter, helper);
		ArrayList<Object> chapters = cm.retrieve(chapter, helper);
		
		assertEquals(chapters.size(), 1);
		
	}
	 /**
	 * Tests saving and loading a chapter that has media locally.
	 */
	public void testAddLoadChapterMedia() {
		// TO DO: Add media
		ChapterManager cm = new ChapterManager(this.getActivity());
		Choice choice = new Choice();
		cm.addChoice(chapter, choice);
		Chapter chapter = new Chapter(UUID.randomUUID()); // Give it photos/illustrations
		
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(chapter, helper);
		ArrayList<Object> chapters = cm.retrieve(chapter, helper);
		
		assertEquals(chapters.size(), 1);
		
	}
	
	/**
	 * Tests updating a chapter's data except for media,
	 * which includes adding and loading a chapter. 
	 */
	public void testUpdateChapterNoMedia() {
		Chapter chapter = new Chapter(UUID.randomUUID());
		ChapterManager cm = new ChapterManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		ArrayList<Object> chapters = cm.retrieve(chapter, helper);
		// assert retrieval worked
		assertEquals(chapters.size(), 1);
		
		Chapter newChapter = (Chapter) chapters.get(0);
		
		newChapter.setText("My Wizard newt");
		newChapter.setNextChapter(UUID.randomUUID());
		cm.update(chapter, newChapter, helper);
		
		// make sure you can find new story
		chapters = cm.retrieve(newChapter, helper);
		assertEquals(chapters.size(), 1);
		
		// make sure old version no longer exists
		chapters = cm.retrieve(chapter, helper);
		assertEquals(chapters.size(), 0);		
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
		
		ArrayList<Object> chapters = cm.retrieve(chapter, helper);
		// assert retrieval worked
		assertEquals(chapters.size(), 1);
		
		Chapter newChapter = (Chapter) chapters.get(0);
		
		newChapter.setText("My Wizard newt");
		newChapter.setNextChapter(UUID.randomUUID());
		cm.update(chapter, newChapter, helper);
		
		// make sure you can find new story
		chapters = cm.retrieve(newChapter, helper);
		assertEquals(chapters.size(), 1);
		
		// make sure old version no longer exists
		chapters = cm.retrieve(chapter, helper);
		assertEquals(chapters.size(), 0);		
	}	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
