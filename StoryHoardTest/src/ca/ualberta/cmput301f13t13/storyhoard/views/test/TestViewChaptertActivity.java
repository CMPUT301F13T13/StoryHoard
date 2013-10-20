/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.views.test;

import static org.junit.Assert.*;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestViewChapterActivity extends
		ActivityInstrumentationTestCase2<StoryHoardActivity> {

	/**
	 * Test creating a storyhoard activity.
	 */
	public TestViewChapterActivity() {
		super(StoryHoardActivity.class);
	}
	
	/** 
	 * Tests taking a photo
	 */
	public void testTakePhoto() {
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.takePhoto();
	}
	
	/**
	 * Tests viewing / reading a chapter.
	 */
	public void testReadChapter() {
		Chapter chapter = new Chapter();
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.readChapter(chapter);
	}
	
	/**
	 * Tests displaying illustrations.
	 */
	public void testDisplayIllustration() {
		Chapter chapter = new Chapter();
		ViewChapterActivity act = new ViewChapterActivity(this.getActivity());
		act.displayIllustration(Chapter chapter);		
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
