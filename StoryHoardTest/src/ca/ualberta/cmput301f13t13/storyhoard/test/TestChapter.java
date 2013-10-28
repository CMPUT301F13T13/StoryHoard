/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.*;
import static org.junit.Assert.*;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.gui.MainActivity;

/**
 * @author Owner
 *
 */
public class TestChapter extends ActivityInstrumentationTestCase2<MainActivity>{

	public TestChapter() {
		super(MainActivity.class);
	}
	
	/**
	 * Tests displaying illustrations.
	 */
	public void testViewIllustration() {
		// Add arguments to make chapter
		Chapter chapter = new Chapter(UUID.randomUUID(), "bobby went home");
		File illustration = new File(null);
		
		// Add an illustration to the chapter
		chapter.setIllustration(illustration);
		
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.displayIllustration(chapter);		
	}
	
	/**
	 * Tests adding an illustration
	 */
	public void testAddIllustration() {
		//Create new chapter and illustration file
		Chapter chapter = new Chapter(UUID.randomUUID(), "the cow mooed");
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
		Chapter chapter = new Chapter(UUID.randomUUID(), "the cow mooed");
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		ArrayList<Object> chapters = cm.retrieve(chapter, helper);
		
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
