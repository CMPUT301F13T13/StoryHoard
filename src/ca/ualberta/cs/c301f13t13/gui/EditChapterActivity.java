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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.ChapterManager;
import ca.ualberta.cs.c301f13t13.backend.GeneralController;
import ca.ualberta.cs.c301f13t13.backend.Story;

//import android.view.Menu; *Not sure if needed

/**
 * Add Chapter Activity
 * 
 * Purpose: - To add a chapter to an existing story. - The author can: - Add
 * images through the use of the image button - Set the text of the chapter
 * through the Edit text space - View all chapters in the story upon pressing
 * the 'View All Chapters' button - Add a choice by pressing the 'Add Choice'
 * button. - This activity will also display the choices that exist or have been
 * added.
 * 
 * author: Alexander Wong, Josh Tate
 */

public class EditChapterActivity extends Activity implements
		ca.ualberta.cs.c301f13t13.gui.SHView<ChapterManager> {

	private Context context = this;
	private Chapter chapt;
	private Story story;
	private ImageButton imageButton;
	private Button saveButton;
	private Button allChaptersButton;
	private Button addChoiceButton;
	private EditText chapterContent;
	private ListView choices;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_chapter);

		imageButton = (ImageButton) findViewById(R.id.chapterEditIllustration);
		chapterContent = (EditText) findViewById(R.id.chapterEditText);
		saveButton = (Button) findViewById(R.id.save_story);
		allChaptersButton = (Button) findViewById(R.id.add_chapter);
		addChoiceButton = (Button) findViewById(R.id.add_choice_button);
		choices = (ListView) findViewById(R.id.chapterEditChoices);
		ArrayList<String> testArray = new ArrayList<String>();


		// Get the story that chapter is being added to
		Bundle bundle = this.getIntent().getExtras();
		if (bundle.getBoolean("isEditing")) {
			story = (Story) bundle.get("Story");
			chapt = (Chapter) bundle.get("Chapter");
		} else {
			story = (Story) bundle.get("New Story");
			chapt = new Chapter(story.getId(), "");
		}

		// Setup the chapter image illustraion button
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// Set the chapter text, if new Chapter will simply be blank
		chapterContent.setText(chapt.getText());

		// Save the story to the general controller, locally
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				story.setFirstChapterId(chapt.getId());
				GeneralController.getInstance().addObjectLocally(story,
						GeneralController.STORY, getBaseContext());
				GeneralController.getInstance().addObjectLocally(chapt,
						GeneralController.CHAPTER, getBaseContext());
				finish();
			}
		});

		// Click to view all chapters
		allChaptersButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// Click to add a choice
		addChoiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						EditChoiceActivity.class);
				startActivity(intent);

			}
		});

		// Use choices to display choices
		for (int i = 0; i < 3; i++) {
			testArray.add("Test Choice " + (i + 1));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, testArray);
		choices.setAdapter(adapter);
	}

	@Override
	public void update(ChapterManager model) {
	};
}
