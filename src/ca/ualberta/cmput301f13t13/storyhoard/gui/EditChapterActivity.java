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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.LocalStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.MediaController;

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
	LifecycleData lifedata;
	private Story story;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private Button saveButton;
	private Button addIllust;
	private Button addChoice;
	private ListView viewChoices;
	private EditText chapterContent;
	private ChapterController chapCon;
	private ChoiceController choiceCon;
	private MediaController mediaCon;
	private AdapterChoices choiceAdapter;
	private AlertDialog illustDialog;
	private ArrayList<Media> illList;
	private LinearLayout illustrations;
	private CheckBox randChoiceCheck;

	public static final int BROWSE_GALLERY_ACTIVITY_REQUEST_CODE = 1;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_chapter);
		setUpFields();
	}

	@Override
	public void onResume() {
		super.onResume();
		setSaveButtonListener();
		setAddChoiceListener();
		setAddIllustrationListener();
		setRandomChoice();
		updateICData();
	}

	/**
	 * Updates the view components depending on the chapter data.
	 */
	private void updateICData() {
		// Set the chapter text, if new Chapter will simply be blank
		choices.clear();
		choices.addAll(choiceCon.getChoicesByChapter(chapter.getId()));
		choiceAdapter.notifyDataSetChanged();
		// Getting illustrations
		illList = mediaCon.getIllustrationsByChapter(chapter.getId());
		// Clean up illustrations layout
		illustrations.removeAllViews();
		// Insert Illustrations
		for (Media ill : illList) {
			illustrations.addView(insertImage(ill, EditChapterActivity.this));
		}
	}

	/**
	 * Sets up the fields, and gets the bundle from the intent.
	 */
	private void setUpFields() {
		chapCon = ChapterController.getInstance(this);
		choiceCon = ChoiceController.getInstance(this);
		mediaCon = MediaController.getInstance(this);
		
		lifedata = LifecycleData.getInstance();
		chapterContent = (EditText) findViewById(R.id.chapterEditText);
		saveButton = (Button) findViewById(R.id.chapterSaveButton);
		addChoice = (Button) findViewById(R.id.addNewChoice);
		viewChoices = (ListView) findViewById(R.id.chapterEditChoices);
		addIllust = (Button) findViewById(R.id.chapterAddIllust);
		illustrations = (LinearLayout) findViewById(R.id.editHorizontalIllustrations);
		randChoiceCheck = (CheckBox) findViewById(R.id.randChoiceCheck);

		// Setup the adapter
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		viewChoices.setAdapter(choiceAdapter);

		story = lifedata.getStory();
		if (lifedata.isEditing()) {
			// Editing an existing chapter
			chapter = lifedata.getChapter();
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
				if (lifedata.isEditing()) {
					chapCon.update(chapter);
				} else {
					chapCon.insert(chapter);
					if (lifedata.isFirstStory()) {
						LocalStoryController storyCon = LocalStoryController.getInstance(EditChapterActivity.this);
						story.setFirstChapterId(chapter.getId());
						storyCon.insert(story);
						lifedata.setFirstStory(false);
					}
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
				if (lifedata.isEditing()) {
					Intent intent = new Intent(getBaseContext(),
							EditChoiceActivity.class);
					lifedata.setChapter(chapter);
					lifedata.setStory(story);
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
				if (!lifedata.isEditing()) {
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

	/**
	 * Set onClick listener for setting random choice
	 */
	public void setRandomChoice() {
		//If the chapter has been set to random choice, check box
		if (chapter.hasRandomChoice()) {
			randChoiceCheck.setChecked(true);
		}
		
		randChoiceCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//If checked, set random choice on chapter
				if (randChoiceCheck.isChecked()) {
					chapter.setRandomChoice(true);
				} else {
					chapter.setRandomChoice(false);
				}
			}
		});
	}
}
