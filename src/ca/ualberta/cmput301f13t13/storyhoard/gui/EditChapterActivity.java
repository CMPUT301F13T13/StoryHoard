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

package ca.ualberta.cmput301f13t13.storyhoard.gui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.HolderApplication;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ObjectType;
import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;

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
 * author: Alexander Wong
 */

public class EditChapterActivity extends MediaActivity {
	HolderApplication app;
	private Story story;
	private Chapter chapter;
	
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private Button saveButton;
	private Button addIllust;
	private Button addChoice;
	private ListView viewChoices;
	private EditText chapterContent;
	private SHController gc;
	private AdapterChoices choiceAdapter;
	private AlertDialog illustDialog;
	private ArrayList<Media> illList;
	private LinearLayout illustrations;

	private Uri imageFileUri;
	public static final int BROWSE_GALLERY_ACTIVITY_REQUEST_CODE = 1;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (HolderApplication) this.getApplication();
		setContentView(R.layout.activity_edit_chapter);
		setUpFields();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSaveButtonListener();
		setAddChoiceListener();
		setAddIllustrationListener();
		updateICData();
	}

	/**
	 * Updates the view components depending on the chapter data.
	 */
	private void updateICData() {
		// Set the chapter text, if new Chapter will simply be blank
		choices.clear();
		choices.addAll(gc.getAllChoices(chapter.getId()));
		choiceAdapter.notifyDataSetChanged();
		// Getting illustrations
		illList = gc.getAllIllustrations(chapter.getId());
		// Clean up illustrations layout
		illustrations.removeAllViews();
		// Insert Illustrations
		for (Media ill : illList) {
			illustrations.addView(insertImage(ill,
					EditChapterActivity.this));
		}
	}

	/**
	 * Sets up the fields, and gets the bundle from the intent.
	 */
	private void setUpFields() {
		gc = SHController.getInstance(getBaseContext());
		
		chapterContent = (EditText) findViewById(R.id.chapterEditText);
		saveButton = (Button) findViewById(R.id.chapterSaveButton);
		addChoice = (Button) findViewById(R.id.addNewChoice);
		viewChoices = (ListView) findViewById(R.id.chapterEditChoices);
		addIllust = (Button) findViewById(R.id.chapterAddIllust);
		illustrations = (LinearLayout) findViewById(R.id.editHorizontalIllustrations);

		// Setup the adapter
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		viewChoices.setAdapter(choiceAdapter);
		
		story = app.getStory();
		if (app.isEditing()) {
			// Editing an existing chapter
			chapter = app.getChapter();
			chapterContent.setText(chapter.getText());
		} else {
			// Create a new chapter from the story's ID
			chapter = new Chapter(story.getId(), "");
		}
	}

	/**
	 * Sets the onClick listener for saving.
	 */
	private void setSaveButtonListener() {
		// Save the chapter to the database, or update if editing
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				chapter.setText(chapterContent.getText().toString());
				if (app.isEditing()) {
					gc.updateObject(chapter, ObjectType.CHAPTER);
				} else {
					story.addChapter(chapter);
					if (app.isFirstStory()) {
						gc.addObject(story, ObjectType.CREATED_STORY);
					}
					gc.addObject(chapter, ObjectType.CHAPTER);
				}
				finish();
			}
		});
	}

	/**
	 * Sets the onClick Listener for adding a choice.
	 */
	private void setAddChoiceListener() {
		// Add a choice to this chapter
		addChoice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (app.isEditing()) {
					Intent intent = new Intent(getBaseContext(),
							EditChoiceActivity.class);
					app.setChapter(chapter);
					app.setStory(story);
					startActivity(intent);
				} else {
					Toast.makeText(getBaseContext(),
							"Save chapter before adding first choice",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * Sets the onCLick listener for adding an illustration.
	 */
	private void setAddIllustrationListener() {
		addIllust.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!app.isEditing()) {
					Toast.makeText(getBaseContext(),
							"Save chapter before adding first illustration",
							Toast.LENGTH_SHORT).show();
					return;
				}
				AlertDialog.Builder alert = new AlertDialog.Builder(
						EditChapterActivity.this);
				// Set dialog title
				alert.setTitle("Choose method:");
				// Options that user may choose to add illustration
				final String[] methods = { "Take Photo", "Choose from Gallery" };
				alert.setSingleChoiceItems(methods, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								switch (item) {
								case 0:
									takePhoto(Media.ILLUSTRATION);
									break;
								case 1:
									browseGallery(Media.ILLUSTRATION);
									break;
								}
								illustDialog.dismiss();
							}
						});
				illustDialog = alert.create();
				illustDialog.show();
			}
		});
	}

	public Uri getImageFileUri() {
		return this.imageFileUri;
	}

	public Chapter getChapter() {
		return this.chapter;
	}
	
	
	// MENU INFORMATION
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.browse_stories:
			Intent browse = new Intent(this, ViewBrowseStories.class);
			startActivity(browse);
			return true;
		case R.id.add_story:
			Intent add = new Intent(this, EditStoryActivity.class);
			add.putExtra("isEditing", false);
			startActivity(add);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

	
	
	
	
	
	
	
}
