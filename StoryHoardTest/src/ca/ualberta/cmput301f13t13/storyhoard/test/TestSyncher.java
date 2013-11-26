package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.LocalStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

public class TestSyncher extends ActivityInstrumentationTestCase2<InfoActivity> {

	public TestSyncher() {
		super(InfoActivity.class);
	}

	/**
	 * Tests synching a story that was gotten from server to the database.
	 * This will either be updating the data, inserting new data, or deleting media.
	 */
	public void testSyncStoryFromServer() {
		Syncher syncher = Syncher.getInstance(getActivity());
		LocalStoryController lsCon = LocalStoryController.getInstance(getActivity());
				
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		String path = BogoPicGen.createPath("img1.jpg");
		Media photo = new Media(mockStory.getId(), path, Media.PHOTO);
		Chapter mockChapter = new Chapter(UUID.randomUUID(), "chap texty");
		mockChapter.addPhoto(photo);
		
		mockStory.addChapter(mockChapter);
		
		syncher.syncStoryFromServer(mockStory);
		
		ArrayList<Story> stories = lsCon.retrieve(mockStory);
		assertEquals(stories.size(), 1);
		
		mockChapter = new Chapter(mockStory.getId(), "chap texty2");
		mockStory.addChapter(mockChapter);
		
		syncher.syncStoryFromServer(mockStory);
		
		stories = lsCon.retrieve(mockStory);
		assertEquals(stories.size(), 1);
		mockStory = stories.get(0);
		
		assertEquals(mockStory.getChapters().size(), 2);
		
		

		
		
		
	}
}
