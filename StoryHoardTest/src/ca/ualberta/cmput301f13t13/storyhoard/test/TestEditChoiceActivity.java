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
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.EditChoiceActivity;

/**
 * Test case for the activity to edit choices
 * 
 * @author Joshua Tate
 * 
 */
public class TestEditChoiceActivity extends
		ActivityInstrumentationTestCase2<EditChoiceActivity> {
	LifecycleData lifedata;
	private EditText choiceText;
	private ListView chapters;
	private StoryController storyCon;
	private ChapterController chapCon;
	private EditChoiceActivity mActivity;

	public TestEditChoiceActivity() {
		super(EditChoiceActivity.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
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
		lifedata.setEditing(false);
		storyCon.setCurrStoryComplete(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapCon.setCurrChapterComplete(chapter);
		chapCon.addChoice(c1);

		mActivity = (EditChoiceActivity) getActivity();
		choiceText = (EditText) mActivity.findViewById(R.id.choiceText);
		chapters = (ListView) mActivity
				.findViewById(R.id.listAllLinkableChapters);
		
		assertTrue(mActivity != null);
		assertTrue(choiceText != null);
		assertTrue(chapters != null);
	}

	/**
	 * Tests setting the edit text for the widget containing choice text.
	 */
	@UiThreadTest
	public void testSetChoiceText() {
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(getActivity());
		chapCon = ChapterController.getInstance(getActivity());
		
		Story story = new Story("title", "author", "es", "432432");
		lifedata.setEditing(false);
		storyCon.setCurrStoryComplete(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapCon.setCurrChapterComplete(chapter);
		chapCon.addChoice(c1);

		mActivity = (EditChoiceActivity) getActivity();
		choiceText = (EditText) mActivity.findViewById(R.id.choiceText);
		chapters = (ListView) mActivity
				.findViewById(R.id.listAllLinkableChapters);
		
		String title = "My choice";
		choiceText.setText(title);
		assertTrue(choiceText.getText().toString().equals(title));
	}
}
