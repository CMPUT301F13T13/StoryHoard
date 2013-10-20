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
public class TestViewSegmentActivity extends
		ActivityInstrumentationTestCase2<StoryHoardActivity> {

	/**
	 * @param name
	 */
	public TestViewSegmentActivity(String name) {
		super(StoryHoardActivity.class);
	}
	
	/** 
	 * Tests taking a photo
	 */
	public void testTakePhoto() {
		ViewSegmentActivity act = new ViewSegmentActivity(this.getActivity());
		act.takePhoto();
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
