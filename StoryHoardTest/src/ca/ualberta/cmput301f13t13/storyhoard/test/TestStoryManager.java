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
	 * Tests caching a story locally on the device
	 */
	public void testCacheStory() {
		Story astory = new Story();
		Storymanager sm = new StoryManager(this.getActivity());
		sm.cacheStory(astory);
		ArrayList<Story> cachedStories = sm.getCachedStories();
		assertTrue(cachedStories.contains(astory) );
	}
	
	/**
	 * Tests reading a story from the local storage
	 */
	public void testReadStory() {
		Story astory = new Story();
		Storymanager sm = new StoryManager(this.getActivity());
		sm.cacheStory(astory);
		try {
			sm.readStory(astory.getID());
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
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
