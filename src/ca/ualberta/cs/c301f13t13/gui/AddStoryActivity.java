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
package ca.ualberta.cs.c301f13t13.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cmput301f13t13.storyhoard.R;

/**
 * 
 * @author Alexander Wong
 *
 */
public class AddStoryActivity extends Activity {
	private EditText newTitle;
	private EditText newAuthor;
	private EditText newDescription;
	private Button firstChapter;
	private Button cancelStory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_story);
	
		newTitle = (EditText) findViewById(R.id.newStoryTitle);
		newAuthor = (EditText) findViewById(R.id.newStoryAuthor);
		newDescription = (EditText) findViewById(R.id.newStoryDescription);
		firstChapter = (Button) findViewById(R.id.addFirstChapter);
		cancelStory = (Button) findViewById(R.id.cancelStory);
		
		firstChapter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*
				 * Save all text forms to first story
				 * Switch to first chapter creation activity
				 */
				newTitle.getText();
				newAuthor.getText();
				newDescription.getText();
			}
		});
		
		cancelStory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Clear all fields, return to main activity
				finish();
			}
		});
	}

}
