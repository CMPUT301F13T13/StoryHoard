/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

/**
 * Class meant for the testing of the ChoiceManager class in the StoryHoard
 * application.
 * 
 * @author Ashley Brown
 * 
 * @see ChoiceManager
 */
public class TestChoiceManager extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	ChoiceManager cm = null;
	private ArrayList<Choice> mockChoices;
	private Choice mockChoice;
	private Choice mockChoice2;
	private Choice mockChoice3;

	public TestChoiceManager() {
		super(InfoActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests adding a choice (saving locally to database)
	 */
	public void testSaveLoadChoice() {
		cm = ChoiceManager.getInstance(getActivity());
		Story story = new Story("7 bugs", "Shamalan", "scary story",
				Utilities.getPhoneId(this.getActivity()));
		UUID storyId = story.getId();
		Chapter chap1 = new Chapter(storyId, "test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(chap1.getId(), chap2.getId(), text);

		cm.insert(c);

		// retrieving story in db that matches mockStory
		ArrayList<Choice> choice = cm.retrieve(c);
		assertEquals(choice.size(), 1);
	}

	/**
	 * Tests saving, loading and editing a choice.
	 */
	public void testEditChoice() {
		cm = ChoiceManager.getInstance(getActivity());
		Story story = new Story("7 bugs", "Shamalan", "scary story",
				Utilities.getPhoneId(this.getActivity()));
		UUID storyId = story.getId();
		Chapter chap1 = new Chapter(storyId, "test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(chap1.getId(), chap2.getId(), text);

		cm.insert(c);

		ArrayList<Choice> mockChoices = cm.retrieve(c);
		assertEquals(mockChoices.size(), 1);

		Choice newChoice = (Choice) mockChoices.get(0);

		newChoice.setText("new choice text mrawr");
		cm.update(c);
		// make sure you can find new choice
		mockChoices = cm.retrieve(newChoice);
		assertEquals(mockChoices.size(), 1);

		// make sure old version no longer exists
		assertFalse(c.getText().equals(newChoice.getText()));
	}

	/**
	 * Tests retrieving all the choices of a chapter
	 */
	public void testGetAllChoices() {
		cm = ChoiceManager.getInstance(getActivity());
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);

		UUID chapId1 = UUID.randomUUID();
		UUID chapId2 = UUID.randomUUID();

		Choice mockChoice = new Choice(chapId1, chapId2, "bob went away");
		cm.insert(mockChoice);
		Choice mockChoice2 = new Choice(chapId1, chapId2, "Lily drove");
		cm.insert(mockChoice2);
		Choice mockChoice3 = new Choice(chapId2, chapId1, "you hit the cow");
		cm.insert(mockChoice3);

		// Looking for all choices belonging to chapter id 1
		Choice criteria = new Choice(null, chapId1, null, null);

		ArrayList<Choice> mockChoices = cm.retrieve(criteria);
		assertEquals(mockChoices.size(), 2);
	}

	/**
	 * Tests the correct determining of whether a choice exists locally or not.
	 */
	public void testExistsLocally() {
		cm = ChoiceManager.getInstance(getActivity());
		
		UUID chapId1 = UUID.randomUUID();
		UUID chapId2 = UUID.randomUUID();
		Choice mockChoice = new Choice(chapId1, chapId2, "bob went away");
		cm.insert(mockChoice);
		Choice mockChoice2 = new Choice(chapId1, chapId2, "Lily drove");

		assertTrue(cm.existsLocally(mockChoice));
		assertFalse(cm.existsLocally(mockChoice2));
	}

	/**
	 * Tests synching a choice, which is really already tested by inserting and
	 * updating a choice.
	 */
	public void testSync() {
		cm = ChoiceManager.getInstance(getActivity());
		UUID chapId1 = UUID.randomUUID();
		UUID chapId2 = UUID.randomUUID();
		Choice mockChoice = new Choice(chapId1, chapId2, "bob went away");
		cm.syncChoice(mockChoice);
		ArrayList<Choice> mockChoices = cm.retrieve(mockChoice);
		assertEquals(mockChoices.size(), 1);
	}
	
	/**
	 * Tests getting all chapters from a chapter.
	 */
	public void testGetChoicesByChapter() {
		cm = ChoiceManager.getInstance(getActivity());
		UUID chapId = UUID.randomUUID();
		mockChoice = new Choice(chapId, UUID.randomUUID(), "bob went away");
		cm.insert(mockChoice);
		mockChoice2 = new Choice(chapId, UUID.randomUUID(), "Lily drove");
		cm.insert(mockChoice2);
		mockChoice3 = new Choice(UUID.randomUUID(), UUID.randomUUID(),
				"Lily drove");
		cm.insert(mockChoice3);

		mockChoices = cm.getChoicesByChapter(chapId);
		assertEquals(mockChoices.size(), 2);
	}

	/**
	 * Tests retrieving a random choice from a chapter.
	 */
	public void testRandomChoice() {
		cm = ChoiceManager.getInstance(getActivity());
		UUID chapId = UUID.randomUUID();
		mockChoice = new Choice(chapId, chapId, "");
		cm.insert(mockChoice);
		mockChoice2 = new Choice(chapId, chapId, "");
		cm.insert(mockChoice2);

		Choice random = cm.getRandomChoice(chapId);
		assertNotNull(random);
	}	
}
