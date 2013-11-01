/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cs.c301f13t13.gui.ViewBrowseAuthorStories;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestUtilities extends ActivityInstrumentationTestCase2<ViewBrowseAuthorStories> {

	/**
	 * @param name
	 */
	public TestUtilities(String name) {
		super(ViewBrowseAuthorStories.class);
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
