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

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cmput301f13t13.storyhoard.R.id;
import ca.ualberta.cmput301f13t13.storyhoard.backend.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.gui.EditStoryActivity;



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
	
	public TestEditStoryActivity() {
		super(EditStoryActivity.class);
	}

	@Override
	public void setUp() throws Exception{
		super.setUp();
		
		Intent intent = new Intent();
		intent.putExtra("isEditing", false);
		intent.putExtra("storyId", UUID.randomUUID());
		
		setActivityIntent(intent);	
		LifecycleData lifedata = LifecycleData.getInstance();
		
		activity = getActivity();
		newTitle = (EditText) activity.findViewById(id.newStoryTitle);
		newAuthor = (EditText) activity.findViewById(id.newStoryAuthor);
		newDescription = (EditText) activity.findViewById(id.newStoryDescription);
	}
	
	public void testPreConditions() {
		assertTrue(activity != null);
		assertTrue(newTitle != null);
		assertTrue(newAuthor != null);
		assertTrue(newDescription != null);
		assertTrue(addfirstChapter != null);
	}
	
	public void testSetTitle() {
		String title = "My Title";
		newTitle.setText(title);
		assertTrue(newTitle.getText().toString().equals(title));
	}
	
	public void testSetAuthor() {
		String author = "The Best Author Ever";
		newAuthor.setText(author);
		assertTrue(newAuthor.getText().toString().equals(author));
	}
	
	public void testSetDescription() {
		String desc = "This is the story of a new description.";
		newDescription.setText(desc);
		assertTrue(newDescription.getText().toString().equals(desc));
	}
}
