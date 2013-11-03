/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.backend.StoryManager;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

/**
 * @author Owner
 *
 */
public class TestServerHandling extends ActivityInstrumentationTestCase2<ViewBrowseStories>{

	public TestServerHandling() {
		super(ViewBrowseStories.class);
	}

	@Before
	public void setUp() throws Exception {
		// clean up server
	}

	/**
	 * Tests uploading and retrieving a story from the server.
	 */
	public void testUploadRetrieveStory() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		Story story = new Story("Harry Potter", "oprah", "the emo boy", true);
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		
		// generate bitmap
		
		
	}
	
	/**
	 * Tests retrieving all stories from server.
	 */
	public void testGetAllStories() {
		
	}
	
	/**
	 * Tests updating a story on the server.
	 */
	public void testUpdateStory() {
		
	}
	
	/**
	 * Tests deleting a story from the server.
	 */
	public void testDeleteStory() {
		
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
