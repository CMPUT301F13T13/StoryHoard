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

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.EditChoiceActivity;

/**
 * Test case for the activity to edit choices
 * 
 * @author Joshua Tate
 * 
 */
public class TestEditChoiceActivity extends
		ActivityInstrumentationTestCase2<EditChoiceActivity> {
	private EditText choiceText;
	private ListView chapters;
	
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
		Story story = new Story("title", "author", "es", "432432");
		Intent intent = new Intent();
		intent.putExtra("isEditing", false);
		intent.putExtra("addingNewChapt", true);
//		intent.putExtra("story", story);
//		intent.putExtra("chapter", new Chapter(story.getId(), null));
		
		setActivityIntent(intent);
		
		mActivity = (EditChoiceActivity) getActivity();
		choiceText = (EditText) mActivity.findViewById(R.id.choiceText);
		chapters = (ListView) mActivity.findViewById(R.id.listAllLinkableChapters);
	}

	public void testPreConditions() {
		assertTrue(mActivity != null);
		assertTrue(choiceText != null);
		assertTrue(chapters != null);
	}
}
