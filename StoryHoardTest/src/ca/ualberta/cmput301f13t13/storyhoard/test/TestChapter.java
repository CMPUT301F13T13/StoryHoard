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
	 * Tests viewing / reading a chapter.
	 */
	public void testReadChapter() {
		// Add arguments to make chapter
		Chapter chapter = new Chapter();
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.readChapter(chapter);
	}
	
	/**
	 * Tests displaying illustrations.
	 */
	public void testDisplayIllustration() {
		// Add arguments to make chapter
		Chapter chapter = new Chapter();
		File illustration = new File();
		
		// Add an illustration to the chapter
		chapter.setIllustration(illustration);
		
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.displayIllustration(Chapter chapter);		
	}	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
