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
public class TestStory extends ActivityInstrumentationTestCase2<StoryHoardActivity>{

	public TestStory() {
		super(StoryHoardActivity.class);
	}
	
	/**
	 * Tests creating a story.
	 */
	public void testCreateStory() {
		//With author, title, and description
		String title = "moo cow";
		String author = "sheperd";
		String description = "the cow's life";
		Story story = new Story(title, author, description);
		
		//Without author, title, or description
		Story story = new Story("","","");
	}

	/**
	 * Tests retrieving all chapters from a story
	 */
	public void testGetAllChapters() {
		String title = "moo cow";
		String author = "sheperd";
		String description = "the cow's life";
		Story story = new Story(title, author, description);
		ArrayList<Chapter> chapters = story.getAllChapters();
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
