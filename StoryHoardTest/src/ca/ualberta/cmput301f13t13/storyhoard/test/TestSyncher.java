package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
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
		Syncher syncher = Syncher.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));

		Chapter mockChapter = new Chapter(mockStory.getId(), "chap texty");
		Choice choice = new Choice(mockChapter.getId(), UUID.randomUUID(),
				"no text");
		mockChapter.getChoices().add(choice);
		mockStory.getChapters().add(mockChapter);

		syncher.syncStoryFromServer(mockStory);

		// checking story synched properly
		ArrayList<Story> stories = storyMan.retrieve(mockStory);
		assertEquals(stories.size(), 1);
		mockStory = stories.get(0);
	}
	
	/**
	 * Tests synching chapters from the database, which is the same as retreivng
	 * and array list of all the chapters of a story, including all their choices
	 * and media.
	 */
	public void testSyncChaptersFromDb() {
		Syncher syncher = Syncher.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));

		Chapter mockChapter = new Chapter(mockStory.getId(), "chap texty");
		Choice choice = new Choice(mockChapter.getId(), UUID.randomUUID(),
				"no text");
		mockChapter.getChoices().add(choice);
		mockStory.getChapters().add(mockChapter);

		syncher.syncStoryFromServer(mockStory);
		
		// checking its chapters synched properly
		ArrayList<Chapter> chapters = mockStory.getChapters();
		assertEquals(chapters.size(), 1);
		Chapter chap = chapters.get(0);
		assertEquals(chap.getChoices().size(), 1);

		// checking its chapters synched properly
		chapters = syncher.syncChaptersFromDb(mockStory.getId());
		assertEquals(chapters.size(), 1);
	}
	
	/**
	 * Tests synching the story  from the memory into the databases.
	 */
	public void syncStoryFromMemory() {
		StoryManager storyMan = StoryManager.getInstance(getActivity());
		Syncher syncher = Syncher.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		syncher.syncStoryFromMemory(mockStory);
		
		ArrayList<Story> stories = storyMan.retrieve(mockStory);
		assertEquals(stories.size(), 1);
	}	

	/**
	 * Tests synching the different chapter parts into the Db.
	 */
	public void syncChapterParts() {
		UUID mockStoryId = UUID.randomUUID();
		ChapterManager cm = ChapterManager.getInstance(getActivity());
		Syncher syncher = Syncher.getInstance(getActivity());
		Chapter mockChapter = new Chapter(mockStoryId, "chap texty");
		Choice choice = new Choice(mockChapter.getId(), UUID.randomUUID(),
				"no text");
		mockChapter.getChoices().add(choice);
		syncher.syncChapterParts(mockChapter);
		mockChapter = cm.getById(mockChapter.getId());
		assertNotNull(mockChapter);
		assertEquals(mockChapter.getChoices().size(), 1);
	}	

}
