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

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class EditChapterActivity extends Activity {
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
	private GUIMediaUtilities util;
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
			illustrations.addView(util.insertImage(ill,
					EditChapterActivity.this));
		}
	}

	/**
	 * Sets up the fields, and gets the bundle from the intent.
	 */
	private void setUpFields() {
		gc = SHController.getInstance(getBaseContext());
		util = new GUIMediaUtilities();
		
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
			chapter = app.getChapter();
			chapterContent.setText(chapter.getText());
		} else {
			chapter = new Chapter(app.getStory().getId(), "");
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
									takePhoto();
									break;
								case 1:
									browseGallery();
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
	 * Code for browsing gallery
	 * 
	 * CODE REUSE URL:
	 * http://stackoverflow.com/questions/6016000/how-to-open-phones
	 * -gallery-through-code
	 */
	private void browseGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				BROWSE_GALLERY_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * Code for taking a photo
	 * 
	 * CODE REUSE LonelyTweeter Camera Code from Lab Author: Joshua Charles
	 * Campbell License: Unlicense Date: Nov. 7, 2013
	 */
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageFileUri = util.getUri();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * Adds an image into the gallery
	 */
	public void insertIntoGallery(Media image) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(image.getPath());
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	/**
	 * Activity results for taking photos and browsing gallery.
	 * 
	 * CODE REUSE LonelyTweeter Camera Code from Lab Author: Joshua Charles
	 * Campbell License: Unlicense Date: Nov. 7, 2013
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Media ill = new Media(chapter.getId(), imageFileUri.getPath(),
						Media.ILLUSTRATION);
				gc.addObject(ill, ObjectType.MEDIA);
				insertIntoGallery(ill);
			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}

		} else if (requestCode == BROWSE_GALLERY_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				imageFileUri = intent.getData();
				String path = util.getRealPathFromURI(imageFileUri, this);
				Media ill = new Media(chapter.getId(), path, Media.ILLUSTRATION);
				gc.addObject(ill, ObjectType.MEDIA);
			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}
		}
	}

	public Uri getImageFileUri() {
		return this.imageFileUri;
	}

	public Chapter getChapter() {
		return this.chapter;
	}
}
