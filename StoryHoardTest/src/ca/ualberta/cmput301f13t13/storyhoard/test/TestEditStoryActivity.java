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

import ca.ualberta.cmput301f13t13.storyhoard.R.id;
import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;
import ca.ualberta.cmput301f13t13.storyhoard.gui.EditStoryActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

/**
 * Tests the EditStoryActivity class.
 *
 */
public class TestEditStoryActivity extends
		ActivityInstrumentationTestCase2<EditStoryActivity> {
	private EditStoryActivity activity;
	private EditText newTitle;
	private EditText newAuthor;
	private EditText newDescription;
	private Button addfirstChapter;
	private Button addStoryImage;
//	private Story newStory;
	private SHController gc;
	private boolean isEditing;
	private AlertDialog imageDialog;
	
	public TestEditStoryActivity() {
		super(EditStoryActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setUp() throws Exception{
		super.setUp();
		
		Intent intent = new Intent();
		intent.putExtra("isEditing", false);
		intent.putExtra("storyId", UUID.randomUUID());
		
		setActivityIntent(intent);		
		
		activity = getActivity();
		newTitle = (EditText) activity.findViewById(id.newStoryTitle);
		newAuthor = (EditText) activity.findViewById(id.newStoryAuthor);
		newDescription = (EditText) activity.findViewById(id.newStoryDescription);
		addfirstChapter = (Button) activity.findViewById(id.addFirstChapter);			
	}
	
	public void testPreConditions() {
		assertTrue(activity != null);
		assertTrue(newTitle != null);
		assertTrue(newAuthor != null);
		assertTrue(newDescription != null);
		assertTrue(addfirstChapter != null);
	}
}
