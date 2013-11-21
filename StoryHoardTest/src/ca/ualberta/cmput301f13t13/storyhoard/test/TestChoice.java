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

import java.util.HashMap;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the Choice class in the StoryHoard
 * application.
 * 
 * @author Ashley Brown
 * 
 * @see Choice
 */
public class TestChoice 
		extends ActivityInstrumentationTestCase2<ViewBrowseStories> {

	public TestChoice() {
		super(ViewBrowseStories.class);
	}

	/**
	 * Tests creating a choice.
	 */
	@SuppressWarnings("unused")
	public void testCreateChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", 
				Utilities.getPhoneId(this.getActivity()));
		UUID storyId = story.getId();
		Chapter chap1 = new Chapter(storyId, "test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		try {
			Choice c = new Choice(chap1.getId(), chap2.getId(), text);
		} catch (Exception e) {
			fail("error in creating a choice object");
		}
	}

	/**
	 * Tests retrieving the search information placed within the choice, i.e.
	 * the choice id and the chapter id to which it belongs.
	 */
	public void testSetSearchCriteria() {
		// empty everything
		Choice criteria = new Choice(null, null, null, null);
		HashMap<String, String> info = criteria.getSearchCriteria();

		assertEquals(info.size(), 0);

		// not empty arguments
		UUID choiceId = UUID.randomUUID();
		UUID chapId = UUID.randomUUID();

		criteria = new Choice(choiceId, chapId, null, null);
		info = criteria.getSearchCriteria();

		assertEquals(info.size(), 2);
		assertTrue(info.get("choice_id").equals(choiceId.toString()));
		assertTrue(info.get("curr_chapter").equals(chapId.toString()));
	}

	/**
	 * Tests the setters and getters methods
	 */
	public void testSettersGetters() {
		Choice mockChoice = new Choice(UUID.randomUUID(), UUID.randomUUID(),
				"opt1");

		UUID choiceId = UUID.randomUUID();
		UUID currentChapter = UUID.randomUUID();
		UUID nextChapter = UUID.randomUUID();
		String text = "hello";

		mockChoice.setId(choiceId);
		mockChoice.setNextChapter(nextChapter);
		mockChoice.setCurrentChapter(currentChapter);
		mockChoice.setText(text);

		assertSame(choiceId, mockChoice.getId());
		assertSame(currentChapter, mockChoice.getCurrentChapter());
		assertSame(nextChapter, mockChoice.getNextChapter());
		assertSame(text, mockChoice.getText());
	}

}
