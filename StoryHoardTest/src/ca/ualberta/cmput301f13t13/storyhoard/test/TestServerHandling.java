/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Media;
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
		Media m = new Media(chap.getId(), Uri.parse("http://hi"), Media.PHOTO);
		
		// generate bitmap 
		
		chap.addPhoto(m);
		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.publish(story);
		ArrayList<Story> stories = sm.searchPublished(story);
		assertEquals(stories.size(), 1);
		
		Story newStory = stories.get(0);
		
		HashMap<UUID, Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 1);
		Chapter nChap = chaps.get(newStory.getFirstChapterId());
		ArrayList<Choice> choices = nChap.getChoices();
		assertEquals(choices.size(), 1);
		ArrayList<Media> photos = nChap.getPhotos();
		assertEquals(photos.size(), 1);
	}
	
	/**
	 * Tests updating a story on the server.
	 */
	public void testUpdateStory() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		Story story = new Story("Harry Potter", "oprah", "the emo boy", true);
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		Media m = new Media(chap.getId(), Uri.parse("http://hi"), Media.PHOTO);
		
		// generate bitmap 
		
		chap.addPhoto(m);
		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.publish(story);
		ArrayList<Story> stories = sm.searchPublished(story);
		assertEquals(stories.size(), 1);
		
		Story newStory = stories.get(0);
		newStory.setTitle("new title");
		newStory.setAuthor("new author");
		newStory.addChapter(new Chapter(newStory.getId(), "my text"));
		
		sm.updatePublished(newStory);
		stories = sm.searchPublished(story);
		assertEquals(stories.size(), 1);
		
		HashMap<UUID, Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 2);
		assertFalse(newStory.getAuthor().equals(story.getAuthor()));
		assertFalse(newStory.getTitle().equals(story.getTitle()));
	}
	
	/**
	 * Tests deleting a story from the server.
	 */
	public void testDeleteStory() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		Story story = new Story("Harry Potter", "oprah", "the emo boy", true);
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		Media m = new Media(chap.getId(), Uri.parse("http://hi"), Media.PHOTO);
		
		// generate bitmap 
		
		chap.addPhoto(m);
		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.publish(story);
		ArrayList<Story> stories = sm.searchPublished(story);
		assertEquals(stories.size(), 1);
		
		sm.deletePublished(story);
		stories = sm.searchPublished(story);
		assertEquals(stories.size(), 0);
	}
}
