/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestChoice extends ActivityInstrumentationTestCase2<StoryHoardActivity>{

	public TestChoice() {
		super(StoryHoardActivity.class);
	}

	/** 
	 * Tests creating a choice.
	 */
	public void testCreateChoice() {
		Chapter chap1 = new Chapter();
		Chapter chap2 = new Chapter();
		String text = "pick me";
		Choice c = new Choice(chap1, chap2, text);
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
