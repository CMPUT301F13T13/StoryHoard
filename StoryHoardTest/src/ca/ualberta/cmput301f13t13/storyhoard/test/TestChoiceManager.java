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

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.ChoiceManager;
import ca.ualberta.cs.c301f13t13.backend.DBContract;
import ca.ualberta.cs.c301f13t13.backend.DBHelper;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.backend.Utilities;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the ChoiceManager class in the StoryHoard
 * application.
 * 
 * @author Ashley Brown
 * 
 * @see ChoiceManager
 */
public class TestChoiceManager extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	ChoiceManager cm = null;

	public TestChoiceManager() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		cm = ChoiceManager.getInstance(getActivity());
	}

	/**
	 * Tests adding a choice (saving locally to database)
	 */
	public void testSaveLoadChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", Utilities.getPhoneId(this.getActivity()));
		UUID storyId = story.getId();
		Chapter chap1 = new Chapter(storyId, "test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(chap1.getId(), chap2.getId(), text);

		cm.insert(c);

		// retrieving story in db that matches mockStory
		ArrayList<Object> choice = cm.retrieve(c);
		assertTrue(choice.size() == 1);
		assertTrue(hasChoice(choice, c));
	}

	/**
	 * Tests saving, loading and editing a choice.
	 */
	public void testEditChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", Utilities.getPhoneId(this.getActivity()));
		UUID storyId = story.getId();
		Chapter chap1 = new Chapter(storyId, "test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(chap1.getId(), chap2.getId(), text);

		cm.insert(c);

		ArrayList<Object> mockChoices = cm.retrieve(c);
		assertTrue(mockChoices.size() == 1);
		assertTrue(hasChoice(mockChoices, c));

		Choice newChoice = (Choice) mockChoices.get(0);

		newChoice.setText("new choice text mrawr");
		cm.update(c);
		// make sure you can find new choice
		mockChoices = cm.retrieve(newChoice);
		assertTrue(mockChoices.size() == 1);
		assertTrue(hasChoice(mockChoices, newChoice));

		// make sure old version no longer exists
		assertFalse(c.getText().equals(newChoice.getText()));
	}

	/**
	 * Tests retrieving all the choices of a chapter
	 */
	public void testGetAllChoices() {
		UUID chapId1 = UUID.randomUUID();
		UUID chapId2 = UUID.randomUUID();

		Choice mockChoice = new Choice(chapId1, chapId2, "bob went away");
		cm.insert(mockChoice);
		Choice mockChoice2 = new Choice(chapId1, chapId2, "Lily drove");
		cm.insert(mockChoice2);
		Choice mockChoice3 = new Choice(chapId2, chapId1, "you hit the cow");
		cm.insert(mockChoice3);

		// Looking for all choices belonging to chapter id 1
		Choice criteria = new Choice(null, chapId1);

		ArrayList<Object> mockChoices = cm.retrieve(criteria);
		assertTrue(mockChoices.size() == 2);
		assertTrue(hasChoice(mockChoices, mockChoice));
		assertTrue(hasChoice(mockChoices, mockChoice2));
		assertFalse(hasChoice(mockChoices, mockChoice3));
	}

	/**
	 * Checks whether a choice is contained in a choices ArrayList.
	 * 
	 * @param objs
	 *            ArrayList of objects.
	 * @param chap
	 *            Object for which we are testing whether or not it is contained
	 *            in the ArrayList.
	 * @return Boolean
	 */
	public Boolean hasChoice(ArrayList<Object> objs, Choice choice) {
		for (Object object : objs) {
			Choice newChoice = (Choice) object;
			if (newChoice.getId().equals(choice.getId())) {
				return true;
			}
		}
		return false;
	}
}
