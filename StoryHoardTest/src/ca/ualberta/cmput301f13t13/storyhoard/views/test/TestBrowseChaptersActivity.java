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
public class TestBrowseChaptersActivity extends ActivityInstrumentationTestCase2<T> {

	/**
	 * 
	 */
	public TestBrowseChaptersActivity() {
		super(StoryHoardActivity.class);
	}
	
	/**
	 * Tests retrieving and displaying chapters from a stroy.
	 */
	public void TestDisplayChapters() {
		TestBrowseChaptersActivity act = new TestBrowseChaptersActivity(this.getActivity());
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		act.displayChapters(chapters);
	}

	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
