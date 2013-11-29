package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.LocalStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.local.MediaManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

public class TestSyncher extends ActivityInstrumentationTestCase2<InfoActivity> {

	public TestSyncher() {
		super(InfoActivity.class);
	}

	/**
	 * Tests synching a story that was gotten from server to the database. This
	 * will either be updating the data, inserting new data, or deleting media.
	 */
	public void testSyncStoryFromServer() {
		StoryManager storyMan = StoryManager.getInstance(getActivity());
		ChapterManager chapMan = ChapterManager.getInstance(getActivity());
		MediaManager mediaMan = MediaManager.getInstance(getActivity());
		ChoiceManager choiceMan = ChoiceManager.getInstance(getActivity());
		
		Syncher syncher = Syncher.getInstance(getActivity());
		LocalStoryController lsCon = LocalStoryController
				.getInstance(getActivity());
		ChapterController chapCon = ChapterController
				.getInstance(getActivity());

		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));

		Chapter mockChapter = new Chapter(mockStory.getId(), "chap texty");
		Choice choice = new Choice(mockChapter.getId(), UUID.randomUUID(),
				"no text");
		mockChapter.addChoice(choice);
		mockStory.addChapter(mockChapter);

		syncher.syncStoryFromServer(mockStory);

		// checking story synched properly
		ArrayList<Story> stories = lsCon.retrieve(mockStory);
		mockStory = stories.get(0);
		assertEquals(stories.size(), 1);

		// checking its chapters synched properly
		ArrayList<Chapter> chapters = chapCon.getFullStoryChapters(mockStory
				.getId());
		assertEquals(chapters.size(), 1);
		Chapter chap = chapters.get(0);
		assertEquals(chap.getChoices().size(), 1);

		mockChapter = new Chapter(mockStory.getId(), "chap texty2");
		mockStory.addChapter(mockChapter);

		// synching, this time should udpate
		syncher.syncStoryFromServer(mockStory);
		stories = lsCon.retrieve(mockStory);
		assertEquals(stories.size(), 1);
		mockStory = stories.get(0);

		// checking its chapters synched properly
		chapters = chapCon.getFullStoryChapters(mockStory.getId());
		assertEquals(chapters.size(), 2);

		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
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
}
