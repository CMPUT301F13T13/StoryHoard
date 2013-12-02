package ca.ualberta.cmput301f13t13.storyhoard.test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

public class TestChoiceController extends ActivityInstrumentationTestCase2<InfoActivity> {

	public TestChoiceController() {
		super(InfoActivity.class);
	}
	/**
	 * Tests placing a reference to model in choice controller so
	 * it can be manipulated. Also tests that set and get retrieve the same choice data
	 * 
	 */

	public void testSetGetChoice() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		ChapterManager cm = ChapterManager.getInstance(getActivity());
		ChapterController cc = ChapterController.getInstance(getActivity());
		ChoiceController chc = ChoiceController.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		Chapter mockChapter =new Chapter(mockStory.getId(), "new");
		mockStory.getChapters().add(mockChapter);
		Chapter mockChapter2 =new Chapter(mockStory.getId(), "new2");
		Choice mockChoice =new Choice(mockChapter.getId(),mockChapter2.getId()); 
		mockChapter.getChoices().add(mockChoice);
		sm.insert(mockStory);
		cm.insert(new Chapter(mockStory.getId(), "hello"));
		
		

		chc.setCurrChoice(null);
		chc.setCurrChoice(mockChoice);
		
		assertEquals(chc.getCurrChoice(), mockChoice);
		}
	/**
	 * Tests editing a choice
	 * 
	 */
public void testEditChoice() {
		StoryManager sm = StoryManager.getInstance(getActivity());
		ChapterManager cm = ChapterManager.getInstance(getActivity());
		ChapterController cc = ChapterController.getInstance(getActivity());
		ChoiceController chc = ChoiceController.getInstance(getActivity());
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));
		Chapter mockChapter =new Chapter(mockStory.getId(), "new");
		Chapter mockChapter2 =new Chapter(mockStory.getId(), "new2");
		Choice mockChoice =new Choice(mockChapter.getId(),mockChapter2.getId()); 
		mockChapter.getChoices().add(mockChoice);
		mockStory.getChapters().add(mockChapter);
		sm.insert(mockStory);
		cm.insert(new Chapter(mockStory.getId(), "hello"));

		chc.setCurrChoice(null);
		chc.setCurrChoice(mockChoice);
		chc.editText("hi");
		assertEquals(chc.getCurrChoice().getText(), "hi");
		chc.editChapterTo(mockChapter.getId());
		assertEquals(chc.getCurrChoice().getNextChapter(), mockChapter.getId());
	}
}
