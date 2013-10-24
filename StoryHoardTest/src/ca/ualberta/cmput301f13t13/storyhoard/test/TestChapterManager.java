/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 * 
 */
public class TestChapterManager extends ActivityInstrumentationTestCase2<DisplayChapterActivity>{
	
	public TestChapterManager() {
		super(DisplayChapterActivity.class);
	}	
	
	/**
	 * Test wrapper method for adding a choice to a chapter.
	 */
	public void testAddChoice() {
		ChapterManager cm = new ChapterManager();
		Choice choice = new Choice();
		Chapter chap = new Chapter();
		cm.addChoice(chap, choice); // calls addChoice from chocieManager
		assertEquals(choice.getChoices().size(), 1);
	}
		
	 /**
	 * Tests saving and loading a chapter. Includes testing to get all
	 * photos and illustrations, text, and choices for viewing.
	 * 
	 */
	public void testAddLoadChapter() {
		ChapterManager cm = new ChapterManager();
		Uri photoUri = "blahblah.jpg";
		Uri illusUri = "nomnom.jpg";
		Choice choice = new Choice();
		Chapter chapter = new Chapter(); // Give it photos/illustrations
		chapter.setPhoto(photoUri);
		chapter.setIllustration(illusUri);
		chapter.setText("I am so sleepy");
		cm.addChoice(chapter, choice);
		
		cm.save(chapter);
		Chapter newChap = cm.get(chapter.getId);
		
		ArrayList<Uri> photoUris = newChap.getPhotos();
		String text = newChap.getText();
		ArrayList<Uri> illusUris = newChap.getIllustrations();
		ArrayList<Choice> choices = newChap.getChoices();

		assert(photoUri.equals(photoUris.get(0)));
		assert(illusUri.equals(illusUris.get(0)));
		assertEquals(choices.size(),2);
		
	}
	
	/**
	 * Tests updating a chapters data (text, photo/illustration permissions)
	 * which includes loading a chapter. 
	 */
	public void testUpdateChapter() {
		ChapterManager cm = new ChapterManager();
		int mockId = 0;
		Chapter chapter = new Chapter(mockId);
		cm.save(chapter);
		Chapter newChap = cm.load(mockId);
		chapter.allowIllustrations(true);
		chapter.setText("cows taste gooood");
		cm.updateItem(chapter);
		
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
