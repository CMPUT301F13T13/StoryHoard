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
import android.widget.LinearLayout;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.EditChapterActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.LifecycleData;

/**
 * Tests the editChapterActivity
 * 
 * @author Joshua Tate
 * 
 */
public class TestEditChapterActivity extends
		ActivityInstrumentationTestCase2<EditChapterActivity> {
	private LifecycleData lifedata;
	private EditChapterActivity activity;
	private ListView viewChoices;
	private EditText chapterContent;
	private LinearLayout illustrations;

	public TestEditChapterActivity() {
		super(EditChapterActivity.class);
	}

	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		lifedata = LifecycleData.getInstance();

		Story story = new Story("title", "author", "es", "432432");
		lifedata.setEditing(true);
		lifedata.setStory(story);
		Chapter chapter = new Chapter(story.getId(), "chapter");
		Choice c1 = new Choice(chapter.getId(), UUID.randomUUID(), "c1");
		chapter.addChoice(c1);
		lifedata.setChapter(chapter);
		activity = getActivity();

		chapterContent = (EditText) activity.findViewById(R.id.chapterEditText);
		viewChoices = (ListView) activity.findViewById(R.id.chapterEditChoices);
		illustrations = (LinearLayout) activity
				.findViewById(R.id.editHorizontalIllustrations);
	}

	/**
	 * Tests that all the gui elements have been instantiated and are not null.
	 */
	public void testPreconditions() {
		assertTrue(activity != null);
		assertTrue(chapterContent != null);
		assertTrue(viewChoices != null);
		assertTrue(illustrations != null);
	}

	/**
	 * Tests the content / text of the chapter is right.
	 */
	public void testChapterContent() {
		String content = chapterContent.getText().toString();
		assertTrue(content.equals("chapter"));
	}

	@UiThreadTest
	public void testSetChapterContent() {
		String title = "My chap";
		chapterContent.setText(title);
		assertTrue(chapterContent.getText().toString().equals(title));
	}
}
