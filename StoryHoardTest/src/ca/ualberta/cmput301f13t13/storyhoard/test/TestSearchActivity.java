package ca.ualberta.cmput301f13t13.storyhoard.test;

import org.junit.Before;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.gui.SearchActivity;

/**
 * Test case for the searching stories activity
 * 
 * @author Kim Wu
 * 
 */

public class TestSearchActivity extends ActivityInstrumentationTestCase2<T> {

	public TestSearchActivit() {
		super(SearchActivity.class);
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
