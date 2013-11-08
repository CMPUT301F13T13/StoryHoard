package ca.ualberta.cmput301f13t13.storyhoard.test;

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

/**
 * Test case for the activity to view all stories
 * 
 * @author Alex Wong
 * 
 */
public class TestViewBrowseStories extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {

	public static final int ADAPTER_COUNT = 0;

	public TestViewBrowseStories() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Testing that the preconditions setup properly
	 */
	public void testPreConditions() {
	}
}
