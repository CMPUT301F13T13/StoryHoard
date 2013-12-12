package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

public class TestChapterController extends ActivityInstrumentationTestCase2<InfoActivity> {

	public TestChapterController() {
		super(InfoActivity.class);
	}

	/**
	 * Tests placing a reference to model in chapter controller so
	 * it can be manipulated. Also tests that set and get retrieve the same chapter data
	 * 
	 */
	public void testSetCurrChapterCompleteAndIncomplete() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		ChapterManager cm = ChapterManager.getInstance(getActivity());
		ChapterController cc = ChapterController.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		Chapter mockChapter =new Chapter(mockStory.getId(), "new");
		mockStory.getChapters().add(mockChapter);
		
		sm.insert(mockStory);
		cm.insert(new Chapter(mockStory.getId(), "hello"));
		cc.setCurrChapterIncomplete(mockChapter.getId());
		

		cc.setCurrChapterComplete(null);
		cc.setCurrChapterComplete(mockChapter);
		
		assertEquals(cc.getCurrChapter(), mockChapter);
	}
	/**
	 * Tests editing a chapter.
	 */
	public void testEditChapter() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		ChapterManager cm = ChapterManager.getInstance(getActivity());
		ChapterController cc = ChapterController.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		Chapter mockChapter =new Chapter(mockStory.getId(), "new");
		Chapter mockChapter2 =new Chapter(mockStory.getId(), "new2");
		Choice mockChoice =new Choice(mockChapter.getId(),mockChapter2.getId()); 
		Choice mockChoice2 =new Choice(mockChoice.getId(),mockChapter.getId(),mockChapter2.getId(),"it worked"); 
		mockChapter.getChoices().add(mockChoice);
		mockStory.getChapters().add(mockChapter);
		
		sm.insert(mockStory);
		cm.insert(new Chapter(mockStory.getId(), "hello"));
		cc.setCurrChapterIncomplete(mockChapter.getId());
		

		cc.setCurrChapterComplete(null);
		cc.setCurrChapterComplete(mockChapter);
		cc.editText("hi");
		assertEquals(cc.getCurrChapter().getText(), "hi");
		cc.editRandomChoice(true);
		assertTrue(cc.getCurrChapter().hasRandomChoice());
		cc.editRandomChoice(false);
		assertFalse(cc.getCurrChapter().hasRandomChoice());
		cc.addChoice(mockChoice);
		assertEquals(cc.getCurrChapter().getChoices().size(),2);
		cc.addRandomChoice();
		assertEquals(cc.getCurrChapter().getChoices().size(),3);
		cc.updateChoice(mockChoice2);
		
		
		
	}
	/**
	 * Tests placing a chapter in the database
	 * 
	 */
	public void testPushChangesToDb() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		ChapterManager cm = ChapterManager.getInstance(getActivity());
		ChapterController cc = ChapterController.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		Chapter mockChapter =new Chapter(mockStory.getId(), "new");
		mockStory.getChapters().add(mockChapter);
		sm.insert(mockStory);
		cm.insert(new Chapter(mockStory.getId(), "hello"));
		cc.setCurrChapterIncomplete(mockChapter.getId());
		
		cc.setCurrChapterComplete(null);
		cc.setCurrChapterComplete(mockChapter);
		cc.pushChangesToDb();
		Chapter chap = cm.getById(mockChapter.getId());
		assertNotNull(chap);
	}
}
