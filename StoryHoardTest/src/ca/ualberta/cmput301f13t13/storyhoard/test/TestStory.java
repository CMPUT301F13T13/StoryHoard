/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cs.c301f13t13.backend.Story;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestStory extends TestCase{
	
	public TestStory() {
		super();
	}
	
	/**
	 * Tests creating a story without chapters.
	 */
	public void testCreateStory() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
