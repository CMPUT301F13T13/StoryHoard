package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

import android.test.ActivityInstrumentationTestCase2;

public class TestStoryController extends ActivityInstrumentationTestCase2<T> {

	public TestStoryController(String name) {
		super(name);
	}

	/**
	 * Tests getting a full chapter back from the database (including choices
	 * and media).
	 */
	public void testGetFullStoryChapters() {
		UUID storyId = UUID.randomUUID();
		mockChapter = new Chapter(storyId, "bob went away");
		Choice c1 = new Choice(mockChapter.getId(), UUID.randomUUID(), "c1");
		Media m1 = new Media(mockChapter.getId(), null, Media.ILLUSTRATION);
		choiceCon.insert(c1);
		mediaCon.insert(m1);
		chapCon.insert(mockChapter);

		ArrayList<Chapter> chaps = chapCon.getFullStoryChapters(storyId);
		mockChapter = chaps.get(0);
		assertEquals(mockChapter.getChoices().size(), 1);
		assertEquals(mockChapter.getIllustrations().size(), 1);
		assertTrue(mockChapter.getText().equals("bob went away"));
	}
	
	/**
	 * Tests converting all the bitmaps of the story's chapters' media into string format, base 64 so
	 * they can be placed into the server.
	 */
	public void testPrepareChaptersMedia() {
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		String path = BogoPicGen.createPath("img1.jpg");
		Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO);
		Chapter mockChapter = new Chapter(UUID.randomUUID(), "chap texty");
		mockChapter.addPhoto(photo);
		
		mockStory.addChapter(mockChapter);
		mockStory.prepareChaptersForServer();
		
		ArrayList<Chapter> chapters = mockStory.getChapters();
		mockChapter = chapters.get(0);
		ArrayList<Media> photos = mockChapter.getPhotos();
		photo = photos.get(0);
		
		assertFalse(photo.getBitmapString().equals(""));
	}		

}
