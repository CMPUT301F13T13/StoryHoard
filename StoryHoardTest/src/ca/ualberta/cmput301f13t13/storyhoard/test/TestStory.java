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
		// Empty story
		Story s = new Story();
		
		// With arguments
		String title = "moo cow";
		String author = "sheperd";
		String description = "the cow's life";
		Story story = new Story(title, author, description);
	}
	
	/**
	 * Tests editing story
	 */
	public void testEditStory() {
		StoryManager sm = new StoryManager();
		int id = 123;
		Story story = sm.getStory(id);
		String description = "new description";
		story.editStory(description);
		story.save();
	}
	
	/**
	 * Tests publishing story
	 */
	public void testPublishStory() {
		StoryManager sm = new StoryManager();
		ArrayList<Story> pubStories = sm.getPublishedStories();
		Story story = new Story();
		pubStories.add(story);
	}
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
