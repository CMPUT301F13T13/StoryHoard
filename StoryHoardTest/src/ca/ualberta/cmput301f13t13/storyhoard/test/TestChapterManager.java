/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Test;

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
	 * Tests saving and loading a chapter. Includes testing to get all
	 * photos and illustrations, text, and choices for viewing.
	 * 
	 * Use cases:
	 * - viewPhotos
	 * - viewIllustrations
	 * - readChapter
	 */
	public void testAddLoadChapter() {
		ChapterManager cm = new ChapterManager();
		Chapter chapter = new Chapter(); // Give it photos/illustrions
		chapter.setPhoto(photo);
		chapter.setIllustration(illustration);
		chapter.setText("I am so sleepy");
		
		cm.save(chapter);
		Chapter newChap = cm.get(chapter.getId);
		
		File photo = newChap.getPhoto();
		String text = newChap.getText();
		File illus = newChap.getIllustration();
		ArrayList<Choice> choices = newChap.getChoices();
		
	}
	
	/**
	 * Tests updating a chapters data (text, photo/illustration permissions)
	 * which includes loading a chapter. 
	 * Use cases:
	 * - allowIllustrations
	 * - editChapter
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
	
	/**
	 * Tests retrieving all chapters from a story
	 * USe cases:
	 * -browseChapters 
	 */
	public void testGetAllChapters() {
		ChapterManager cm = new ChapterManager();
		ArrayList<Chapter> chapters = cm.getAllChapters(storyId);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
