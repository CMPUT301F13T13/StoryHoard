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
import java.util.HashMap;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.HelpGuide;

/**
 * Class meant for the testing of the Chapter class in the StoryHoard
 * application.
 * 
 * @author Ashley Brown
 * @author Stephanie Gil
 * 
 * @see Chapter
 */
public class TestChapter 
		extends ActivityInstrumentationTestCase2<HelpGuide> {

	public TestChapter() {
		super(HelpGuide.class);
	}
	
	/**
	 * Tests creating a chapter two ways.
	 */
	public void testCreateChapter() {
		@SuppressWarnings("unused")
		Chapter newChapter = new Chapter(UUID.randomUUID(), "this is text");
		try {
			newChapter = new Chapter(UUID.randomUUID(), UUID.randomUUID(),
					"again");
		} catch (Exception e) {
			fail("error in creating a chapter object");
		}
	}

	/**
	 * Tests adding a choice to a chapter.
	 */
	public void testAddChoice() {
		Chapter chapter = new Chapter(UUID.randomUUID(), "hello there");
		Choice choice = new Choice(chapter.getId(), UUID.randomUUID(), "rawr");
		chapter.addChoice(choice);

		assertEquals(chapter.getChoices().size(), 1);
	}

	/**
	 * Tests retrieving the search information places within the story, i.e. 
	 * the id, title, author, description, and whether or not it was created 
	 * by the author.
	 */
	public void testSetSearchCriteria() {
		// empty everything
		Chapter criteria = new Chapter(null, null, null, null);
		HashMap<String, String> info = criteria.getSearchCriteria();

		assertEquals(info.size(), 0);

		// not empty arguments
		UUID id = UUID.randomUUID();
		UUID sId = UUID.randomUUID();
		criteria = new Chapter(id, sId, null, true);
		info = criteria.getSearchCriteria();

		assertEquals(info.size(), 2);
		assertTrue(info.get("chapter_id").equals(id.toString()));
		assertTrue(info.get("story_id").equals(sId.toString()));
	}

	/**
	 * Tests the setters and getter methods
	 */
	public void testSettersGetters() {
		Chapter mockChapter = new Chapter(UUID.randomUUID(), "chap texty");

		UUID id = UUID.randomUUID();
		UUID storyId = UUID.randomUUID();
		String text = "hello";
		ArrayList<Choice> choices = new ArrayList<Choice>();
		ArrayList<Media> photos = new ArrayList<Media>();
		ArrayList<Media> ills = new ArrayList<Media>();
		Boolean randomChoice = true;

		mockChapter.setId(id);
		mockChapter.setStoryId(storyId);
		mockChapter.setText(text);
		mockChapter.setChoices(choices);
		mockChapter.setPhotos(photos);
		mockChapter.setIllustrations(ills);
		mockChapter.setRandomChoice(randomChoice);

		assertTrue(mockChapter.getId().equals(id));
		assertTrue(mockChapter.getStoryId().equals(storyId));
		assertTrue(mockChapter.getText().equals(text));
		assertTrue(mockChapter.getChoices() != null);
		assertTrue(mockChapter.getPhotos() != null);
		assertTrue(mockChapter.getIllustrations() != null);
		assertTrue(mockChapter.hasRandomChoice());
	}
}
