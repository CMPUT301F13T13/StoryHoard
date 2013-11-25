package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;

import android.test.ActivityInstrumentationTestCase2;

public class TestChoiceController extends ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private ChoiceController choiceCon;
	private ArrayList<Choice> mockChoices;
	private Choice mockChoice;
	private Choice mockChoice2;
	private Choice mockChoice3;
	
	public TestChoiceController() {
		super(ViewBrowseStories.class);
		choiceCon = ChoiceController.getInstance(getActivity());
	}

	/**
	 * Tests getting all chapters from a story.
	 */
	public void testGetChoicesByStory() {
		mockChoice = new Choice(UUID.randomUUID(), "bob went away");
		choiceCon.insert(mockChoice);
		mockChoice2 = new Choice(mockChoice.getStoryId(),
				"Lily drove");
		choiceCon.insert(mockChoice2);
		mockChoice3 = new Choice(UUID.randomUUID(), "Lily drove");
		choiceCon.insert(mockChoice3);

		mockChoices = choiceCon.getChoicesByStory(mockChoice.getStoryId());
		assertEquals(mockChoices.size(), 2);		
	}
	
	/**
	 * Tests getting all created chapters.
	 */
	public void testGetAll() {
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
		mockChoice = new Choice(UUID.randomUUID(), "bob went away");
		choiceCon.insert(mockChoice);
		mockChoice2 = new Choice(mockChoice.getStoryId(),
				"Lily drove");
		choiceCon.insert(mockChoice2);
		mockChoice3 = new Choice(UUID.randomUUID(), "Lily drove");
		choiceCon.insert(mockChoice3);

		mockChoices = choiceCon.getAll();
		assertEquals(mockChoices.size(), 3);		
	}

	/**
	 * Tests getting a full chapter back from the database (including
	 * choices and media).
	 */
	public void testGetFullChoice() {
		mockChoice = new Choice(UUID.randomUUID(), "bob went away");
		Choice c1 = new Choice(mockChoice.getId(), UUID.randomUUID(), "c1");
		Media m1 = new Media(mockChoice.getId(), null, Media.ILLUSTRATION);
		choiceCon.insert(c1);
		mediaCon.insert(m1);
		choiceCon.insert(mockChoice);
		
		Choice newChoice = choiceCon.getFullChoice(mockChoice.getId());
		assertEquals(newChoice.getChoices().size(), 1);
		assertEquals(newChoice.getIllustrations().size(), 1);
		assertTrue(mockChoice.getText().equals(newChoice.getText()));
	}
	
	/**
	 * Tests inserting, retrieving, and updating a chapter.
	 */
	public void testInsertRetrieveUpdate() {
		mockChoice = new Choice(UUID.randomUUID(), "bob went away");
		choiceCon.insert(mockChoice);
		
		mockChoices = choiceCon.retrieve(mockChoice);
		assertEquals(mockChoices.size(), 1);
		
		mockChoice2 = mockChoices.get(0);
		mockChoice2.setText("hello");
		choiceCon.update(mockChoice2);
		
		mockChoices = choiceCon.retrieve(mockChoice);
		assertEquals(mockChoices.size(), 1);	
		mockChoice2 = mockChoices.get(0);
		
		assertFalse(mockChoice2.getText().equals(mockChoice.getText()));
	}	
	
}
