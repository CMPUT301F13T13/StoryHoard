/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * @author Owner
 *
 */
public class TestChoice extends TestCase{

	public TestChoice() {
		super();
	}

	/** 
	 * Tests creating a choice.
	 */
	public void testCreateChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
		UUID storyId= story.getId();
		Chapter chap1 = new Chapter(storyId,"test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(storyId, chap1.getId(), chap2.getId(), text);
	}
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
