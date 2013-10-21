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
public class TestStoryManager extends ActivityInstrumentationTestCase2<ViewStoryActivity>{

	public TestStoryManager() {
		super(ViewStoryActivity.class);
	}

	/**
	 * Tests saving a story.
	 */
	public void testSaveStory() {
		
	}
	
	
	/**
	 * Tests browsing cached stories
	 */
	public void testBrowseCachedStories() {
		StoryManager sm = new StoryManager(this.getActivity());
		ArrayList<Story> stories = sm.getCachedStories();
	}

	/**
	 * Tests browsing published stories
	 */	
	public void testBrowsePublishedStories() {
		StoryManager sm = new StoryManager(this.getActivity());
		ArrayList<Story> stories = sm.getPublishedStories();
	}

	/**
	 * Tests browsing author's own stories
	 */	
	public void testBrowseOwnStories() {
		StoryManager sm = new StoryManager(this.getActivity());
		ArrayList<Story> stories = sm.getOwnStories();
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
