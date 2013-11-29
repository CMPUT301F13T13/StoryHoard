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
import android.widget.Button;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewStory;

/**
 * Tests the View Story activity.
 * 
 */
public class TestViewStory extends ActivityInstrumentationTestCase2<ViewStory> {
	private ViewStory activity;
	private TextView storyTitle;
	private TextView storyAuthor;
	private TextView storyDescription;
	private Button beginReading;

	public TestViewStory() {
		super(ViewStory.class);
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
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
		story.getChapters().add(chapter);
		
		storyCon.setCurrStoryComplete(story);
		chapCon.setCurrChapterComplete(chapter);

		activity = getActivity();

		storyTitle = (TextView) activity.findViewById(R.id.storyTitle);
		storyAuthor = (TextView) activity.findViewById(R.id.storyAuthor);
		storyDescription = (TextView) activity
				.findViewById(R.id.storyDescription);
		beginReading = (Button) activity.findViewById(R.id.viewFirstChapter);
		
		assertTrue(storyTitle != null);
		assertTrue(storyAuthor != null);
		assertTrue(storyDescription != null);
		assertTrue(beginReading != null);
	}

	/**
	 * Tests letting activity set ui widgest and reading their values to make sure
	 * they are what they should be.
	 */
	@UiThreadTest
	public void testTitleDescriptionAuthor() {
		ChapterController chapCon = ChapterController.getInstance(getActivity());
		StoryController storyCon = StoryController.getInstance(getActivity());

		Story story = new Story("title", "author", "es", "432432");
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapter.getChoices().add(c1);
		story.getChapters().add(chapter);
		
		storyCon.setCurrStoryComplete(story);
		chapCon.setCurrChapterComplete(chapter);

		activity = getActivity();

		storyTitle = (TextView) activity.findViewById(R.id.storyTitle);
		storyAuthor = (TextView) activity.findViewById(R.id.storyAuthor);
		storyDescription = (TextView) activity
				.findViewById(R.id.storyDescription);
		beginReading = (Button) activity.findViewById(R.id.viewFirstChapter);
		
		assertTrue(storyTitle.getText().toString().equals("title"));
		assertTrue(storyDescription.getText().toString().equals("es"));
		assertTrue(storyAuthor.getText().toString().equals("author"));
	}
}
