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
import android.widget.EditText;
import ca.ualberta.cmput301f13t13.storyhoard.R.id;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.EditStoryActivity;
import ca.ualberta.cmput301f13t13.storyhoard.gui.LifecycleData;

/**
 * Tests the EditStoryActivity class.
 * 
 */
public class TestEditStoryActivity extends
		ActivityInstrumentationTestCase2<EditStoryActivity> {
	private EditStoryActivity activity;
	private LifecycleData lifedata;
	private EditText newTitle;
	private EditText newAuthor;
	private EditText newDescription;
	private StoryController storyCon;
	private ChapterController chapCon;

	public TestEditStoryActivity() {
		super(EditStoryActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests the ui widgets have been properly initialized.
	 */
	public void testPreConditions() {
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(getActivity());
		chapCon = ChapterController.getInstance(getActivity());
		Story story = new Story("title", "author", "es", "432432");
		
		lifedata.setEditing(true);
		storyCon.setCurrStoryComplete(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapCon.setCurrChapterComplete(chapter);
		chapCon.addChoice(c1);
		activity = getActivity();

		newTitle = (EditText) activity.findViewById(id.newStoryTitle);
		newAuthor = (EditText) activity.findViewById(id.newStoryAuthor);
		newDescription = (EditText) activity
				.findViewById(id.newStoryDescription);
		
		assertTrue(activity != null);
		assertTrue(newTitle != null);
		assertTrue(newAuthor != null);
		assertTrue(newDescription != null);
	}

	/**
	 * Tests setting the title of a story on the ui widget.
	 */
	@UiThreadTest
	public void testSetTitle() {
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(getActivity());
		chapCon = ChapterController.getInstance(getActivity());
		Story story = new Story("title", "author", "es", "432432");
		
		lifedata.setEditing(true);
		storyCon.setCurrStoryComplete(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapCon.setCurrChapterComplete(chapter);
		chapCon.addChoice(c1);
		activity = getActivity();

		newTitle = (EditText) activity.findViewById(id.newStoryTitle);
		newAuthor = (EditText) activity.findViewById(id.newStoryAuthor);
		newDescription = (EditText) activity
				.findViewById(id.newStoryDescription);
		
		String title = "My Title";
		newTitle.setText(title);
		assertTrue(newTitle.getText().toString().equals(title));
	}

	/**
	 * Tests setting the author of a story on the ui widget.
	 */
	@UiThreadTest
	public void testSetAuthor() {
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(getActivity());
		chapCon = ChapterController.getInstance(getActivity());
		Story story = new Story("title", "author", "es", "432432");
		
		lifedata.setEditing(true);
		storyCon.setCurrStoryComplete(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapCon.setCurrChapterComplete(chapter);
		chapCon.addChoice(c1);
		activity = getActivity();

		newTitle = (EditText) activity.findViewById(id.newStoryTitle);
		newAuthor = (EditText) activity.findViewById(id.newStoryAuthor);
		newDescription = (EditText) activity
				.findViewById(id.newStoryDescription);
		
		String author = "The Best Author Ever";
		newAuthor.setText(author);
		assertTrue(newAuthor.getText().toString().equals(author));
	}

	/**
	 * Tests setting the description of a story on the ui widget.
	 */
	@UiThreadTest
	public void testSetDescription() {
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(getActivity());
		chapCon = ChapterController.getInstance(getActivity());
		Story story = new Story("title", "author", "es", "432432");
		
		lifedata.setEditing(true);
		storyCon.setCurrStoryComplete(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapCon.setCurrChapterComplete(chapter);
		chapCon.addChoice(c1);
		activity = getActivity();

		newTitle = (EditText) activity.findViewById(id.newStoryTitle);
		newAuthor = (EditText) activity.findViewById(id.newStoryAuthor);
		newDescription = (EditText) activity
				.findViewById(id.newStoryDescription);
		
		String desc = "This is the story of a new description.";
		newDescription.setText(desc);
		assertTrue(newDescription.getText().toString().equals(desc));
	}
}
