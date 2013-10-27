/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import org.junit.Test;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.DBHelper;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.backend.StoryManager;
import ca.ualberta.cs.c301f13t13.gui.MainActivity;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestStoryManager extends ActivityInstrumentationTestCase2<MainActivity>{
	private Story mockStory;
	private Chapter mockChapter;
	
	public TestStoryManager() {
		super(MainActivity.class);	
	}

	/**
	 * Create a new story.
	 */
	public void newMockStory(String title, String author, String description, 
			Boolean authorsOwn) {
		// story object
		mockStory = new Story(title, author, description, authorsOwn);
		
		// first chapter of story
		mockChapter = new Chapter(mockStory.getId());
		mockStory.addChapter(mockChapter);	
		mockStory.setFirstChapterId(mockChapter.getId());
	}
	
	/**
	 * Tests caching a story locally on the device, and then 
	 * loading cached stories.
	 */
	public void testCacheLoadStory() {
		newMockStory("My Frog", "blueberry", "my cute frog", false);
		StoryManager sm = new StoryManager(this.getActivity());
		sm.cacheStory(mockStory);
		ArrayList<Story> cachedStories = sm.getCachedStories();
		assertNotSame(cachedStories.size(), 0);
		assertTrue(cachedStories.contains(mockStory));
	}
	
	/**
	 * Tests adding and loading a story from the local storage
	 */
	public void testAddLoadStory() {
		newMockStory("My Cow", "Dr. Poe", "my chubby cow", true);
		StoryManager sm = new StoryManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		sm.insert(mockStory, helper);
		try {
			// retrieving story in db that matches mockStory
			ArrayList<Object> loadedStories = sm.retrieve(mockStory, helper);
			for (Object obj: loadedStories) {
				System.out.println((Story) obj);
			}
			assertNotSame(loadedStories.size(), 0);
			assertTrue(loadedStories.contains(mockStory));
		} catch(Exception e) {
			fail("Could not read Story: " + e.getStackTrace());
		}
	}

	/**
	 * Tests editing story
	 */
	public void testEditStory() {
		newMockStory("My Wizard Mouse", "JK ROlling", "before the edit...",
				    true);
		StoryManager sm = new StoryManager(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		ArrayList<Object> stories = sm.retrieve(mockStory, helper);
		// assert retrieval worked
		assertEquals(stories.size(), 1);
		
		Story newStory = (Story) stories.get(0);
		
		newStory.setTitle("My Wizard newt");
		newStory.setAuthor("not jk rolling");
		newStory.setDescription("after the edit...");
		sm.update(mockStory, newStory, helper);
		
		// make sure you can find new story
		stories = sm.retrieve(newStory, helper);
		assertNotSame(stories.size(), 0);
		assertTrue(stories.contains(newStory));
		
		// make sure old version no longer exists
		stories = sm.retrieve(mockStory, helper);
		assertFalse(stories.contains(mockStory));
	}
	
	/**
	 * Tests publishing story, then loading it from server.
	 */
	public void testPublishLoadStory() {
		newMockStory("My Monkey", "TS ELLIOT", 
					"monkey is in the server", true);
		StoryManager sm = new StoryManager(this.getActivity());
		sm.publish(mockStory);
		
		ArrayList<Story> pubStories = sm.getPublishedStories();
		assertNotSame(pubStories.size(), 0);
		assertTrue(pubStories.contains(mockStory));
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
