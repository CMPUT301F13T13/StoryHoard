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
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.LifecycleData;
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
	private LifecycleData lifedata;

	public TestViewStory() {
		super(ViewStory.class);
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		lifedata = LifecycleData.getInstance();

		Story story = new Story("title", "author", "es", "432432");
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapter.addChoice(c1);
		story.addChapter(chapter);
		lifedata.setStory(story);

		activity = getActivity();

		storyTitle = (TextView) activity.findViewById(R.id.storyTitle);
		storyAuthor = (TextView) activity.findViewById(R.id.storyAuthor);
		storyDescription = (TextView) activity
				.findViewById(R.id.storyDescription);
		beginReading = (Button) activity.findViewById(R.id.viewFirstChapter);

	}

	/**
	 * Tests that the ui widgets were correctly initialized.
	 */
	public void testPreConditions() {
		assertTrue(storyTitle != null);
		assertTrue(storyAuthor != null);
		assertTrue(storyDescription != null);
		assertTrue(beginReading != null);
	}

	/**
	 * Tests setting the ui widget for story author.
	 * 
	 * @UiThreadTest public void testAuthor() {
	 *               assertTrue(storyAuthor.getText().
	 *               toString().equals("author")); }
	 * 
	 *               /** Tests setting the ui widget for story title.
	 */
	@UiThreadTest
	public void testTitle() {
		assertTrue(storyTitle.getText().toString().equals("title"));
	}

	/**
	 * Tests setting the ui widget for story description.
	 */
	@UiThreadTest
	public void testSetDescription() {
		assertTrue(storyDescription.getText().toString().equals("es"));
	}
}
