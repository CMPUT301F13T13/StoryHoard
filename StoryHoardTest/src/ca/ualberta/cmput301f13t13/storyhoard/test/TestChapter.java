/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cmput301f13t13.storyhoard.views.test.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.views.test.File;
import ca.ualberta.cmput301f13t13.storyhoard.views.test.T;
import ca.ualberta.cmput301f13t13.storyhoard.views.test.ViewChapterActivity;

/**
 * @author Owner
 *
 */
public class TestChapter extends ActivityInstrumentationTestCase2<StoryHoardActivity>{

	public TestChapter() {
		super(StoryHoardActivity.class);
	}
	
	/**
	 * Tests displaying illustrations.
	 */
	public void testViewIllustration() {
		// Add arguments to make chapter
		Chapter chapter = new Chapter();
		File illustration = new File();
		
		// Add an illustration to the chapter
		chapter.setIllustration(illustration);
		
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.displayIllustration(Chapter chapter);		
	}
	
	/**
	 * Tests adding an illustration
	 */
	public void testAddIllustration() {
		//Create new chapter and illustration file
		Chapter chapter = new Chapter();
		File illustration = new File();
		//Add illustration to chapter
		try {
			chapter.setIllustration(illustration);
		} catch (Exception e) {
			fail("Could not add illustration: "+e);
		}
	}
	
	/**
	 * Tests edit illustration
	 */
	public void testEditIllustration() {
		//Get existing chapter from ChapterManager
		ChapterManager cm = new ChapterManager();
		int id = 123;
		Chapter chapter = cm.getChapter(id);
		
		//Create new illustration file
		File illustration = new File();
		
		//Replace existing illustration with new one
		try {
			chapter.setIllustration(illustration);
		} catch (Exception e) {
			fail("Could not add illustration "+e);
		}
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
