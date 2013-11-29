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

import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewChapter;

/**
 * Tests the View Chapter class.
 * 
 */
public class TestViewChapter extends
		ActivityInstrumentationTestCase2<ViewChapter> {
	private ViewChapter activity;
	private TextView chapterContent;
	private ListView chapterChoices;

	public TestViewChapter() {
		super(ViewChapter.class);
	}

	@Override
	public void setUp() throws Exception {
	}

	/**
	 * Tests that the ui widgets were correctly initialized.
	 */
	public void testPreConditions() {
		ChapterController chapCon = ChapterController.getInstance(getActivity());
		StoryController storyCon = StoryController.getInstance(getActivity());
		
		Story story = new Story("title", "author", "es", "432432");
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapter.getChoices().add(c1);
		
		storyCon.setCurrStoryComplete(story);
		storyCon.addChapter(chapter);
		chapCon.setCurrChapterComplete(chapter);

		activity = getActivity();

		// Setup the activity fields
		chapterContent = (TextView) activity.findViewById(R.id.chapterContent);
		chapterChoices = (ListView) activity.findViewById(R.id.chapterChoices);
		
		assertTrue(chapterContent != null);
		assertTrue(chapterChoices != null);
	}

	/**
	 * Tests the ui widget containing chapter content contains what it should.
	 */
	@UiThreadTest
	public void testChapterContent() {
		ChapterController chapCon = ChapterController.getInstance(getActivity());
		StoryController storyCon = StoryController.getInstance(getActivity());
		
		Story story = new Story("title", "author", "es", "432432");
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapter.getChoices().add(c1);
		
		storyCon.setCurrStoryComplete(story);
		storyCon.addChapter(chapter);
		chapCon.setCurrChapterComplete(chapter);

		activity = getActivity();

		// Setup the activity fields
		chapterContent = (TextView) activity.findViewById(R.id.chapterContent);
		chapterChoices = (ListView) activity.findViewById(R.id.chapterChoices);
		
		assertTrue(chapterContent.getText().toString().equals("chapter"));
	}
}
