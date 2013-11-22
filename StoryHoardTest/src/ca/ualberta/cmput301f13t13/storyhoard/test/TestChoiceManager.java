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
import ca.ualberta.cmput301f13t13.storyhoard.backend.*;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

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
}
