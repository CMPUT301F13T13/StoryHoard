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

/**
 * Add Choice Activity
 * 
 * Purpose:
 * 	- To add a choice to an existing chapter
 * 	- The author can:
 * 
 *
 * 
 * @author joshuatate
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;

public class EditChoiceActivity extends Activity  {  // CHANGE THIS TO CHOICE MANAGER ONCE CLASS HAS BEEN MADE
	
	private Chapter chapt;
	private Button existingChapterButton;
	private Button newChapterButton;
	private Button cancelButton;
	private EditText chapterChoice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get the chapter that choice is being added to
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			chapt = (Chapter) bundle.getSerializable(Constants._ID);
		}
		
		setContentView(R.layout.activity_edit_choice);

		existingChapterButton = (Button)findViewById(R.id.existing_chapter_button);
		existingChapterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Connect chapter to an existing one
				Intent intent = new Intent(getApplicationContext(),
						ViewAllChaptersActivity.class);
				//Notify activity that we are selecting
				//rather than viewing
				intent.putExtra("viewing", false);
				startActivity(intent);
			}
		});

		newChapterButton = (Button)findViewById(R.id.new_chapter_button);
		newChapterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Connect chapter to a new one

			}
		});

		chapterChoice = (EditText)findViewById(R.id.chapter_title);
		chapterChoice.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(
					CharSequence c, int start, int end, int count) {
				//Chapter title
			}
			public void beforeTextChanged(
					CharSequence c, int start, int end, int count) {}
			public void afterTextChanged(Editable e) {}
		});

		cancelButton = (Button)findViewById(R.id.dialog_cancel_button);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

//	@Override
//	public void update(Model model) {
//		// TODO Auto-generated method stub
//
//	}
}
