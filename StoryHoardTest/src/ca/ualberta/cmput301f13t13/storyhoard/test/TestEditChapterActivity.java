/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;

import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.gui.AdapterChoices;
import ca.ualberta.cs.c301f13t13.gui.EditChapterActivity;

/**
 * Tests the editChapterActivity
 * 
 * @author Joshua Tate
 *
 */
public class TestEditChapterActivity extends
		ActivityInstrumentationTestCase2<EditChapterActivity> {
	private EditChapterActivity activity;
	private Button saveButton;
	private Button addIllust;
	private Button addChoice;
	private ListView viewChoices;
	private EditText chapterContent;
	private AdapterChoices choiceAdapter;
	private AlertDialog illustDialog;
	private LinearLayout illustrations;
	private static Gson gson = null;	

	public TestEditChapterActivity() {
		super(EditChapterActivity.class);
	}
	
	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		gson = new Gson();
		Story story = new Story("title", "author", "es", "432432");
		Intent intent = new Intent();
		intent.putExtra("isEditing", false);
		intent.putExtra("addingNewChapt", true);
		intent.putExtra("Story", gson.toJson(story));
		intent.putExtra("Chapter", gson.toJson(new Chapter(story.getId(), null)));
		
		setActivityIntent(intent);
		
		activity = getActivity();
		chapterContent = (EditText) activity.findViewById(R.id.chapterEditText);
		saveButton = (Button) activity.findViewById(R.id.chapterSaveButton);
		addChoice = (Button) activity.findViewById(R.id.addNewChoice);
		viewChoices = (ListView) activity.findViewById(R.id.chapterEditChoices);
		addIllust = (Button) activity.findViewById(R.id.chapterAddIllust);
		illustrations = (LinearLayout) activity.findViewById(R.id.editHorizontalIllustrations);
	}

	public void testPreconditions() {
		assertTrue(activity != null);
		assertTrue(chapterContent != null);
		assertTrue(saveButton != null);
		assertTrue(addChoice != null);
		assertTrue(viewChoices != null);
		assertTrue(addIllust != null);
		assertTrue(illustrations != null);
	}
}
