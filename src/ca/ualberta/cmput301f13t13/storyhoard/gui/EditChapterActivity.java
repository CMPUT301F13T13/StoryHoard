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
import java.util.HashMap;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.LocalStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.MediaController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.LifecycleData;

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
 * author: Kim Wu
 */

public class EditChapterActivity extends MediaActivity {
	private ImageDialogBuilder dialBuilder = new ImageDialogBuilder();
	LifecycleData lifedata;
	private Story story;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
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
	private View viewClicked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_chapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
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
	
		// any choices that have not been saved
		ArrayList<Choice> myChoices = lifedata.getCurrChoices();
		for (Choice choice : myChoices) {
			choices.add(choice);
		}

		choiceAdapter.notifyDataSetChanged();

		// Getting illustrations
		illList = mediaCon.getIllustrationsByChapter(chapter.getId());

		// Clean up illustrations layout
		illustrations.removeAllViews();
		// Insert Illustrations
		for (Media ill : illList) {
			insertImage(ill, this, illustrations);
		}

		// any images that have not been saved
		ArrayList<Media> imgs = lifedata.getCurrImages();
		for (Media img : imgs) {
			View v = insertImage(img, EditChapterActivity.this, illustrations);
			if (img.getType().equals(Media.ILLUSTRATION)) {
				v.setOnLongClickListener(new OnLongClickListener () {
					@Override
					public boolean onLongClick(View v) {
						viewClicked = v;
						dialBuilder.setDialog(EditChapterActivity.this, viewClicked, illustrations);
						return false;
					}
				});
			}
		}
		lifedata.setCurrImage(null);
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
		viewChoices = (ListView) findViewById(R.id.chapterEditChoices);
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
			lifedata.setChapter(chapter);
		}
	}

	/**
	 * Set onClick listener for setting random choice
	 */
	public void setRandomChoice() {
		// If the chapter has been set to random choice, check box
		if (chapter.hasRandomChoice()) {
			randChoiceCheck.setChecked(true);
		}

		randChoiceCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// If checked, set random choice on chapter
				if (randChoiceCheck.isChecked()) {
					chapter.setRandomChoice(true);
				} else {
					chapter.setRandomChoice(false);
				}
			}
		});
	}

	// MENU INFORMATION
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_edit_chapter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.addChoice:
			addChoice();
			return true;
		case R.id.addIllus:
			addIllustration();
			return true;
		case R.id.Save:
			saveAction();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addIllustration() {
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

	private void addChoice() {
		Intent intent = new Intent(getBaseContext(),
				EditChoiceActivity.class);
		lifedata.setChapter(chapter);
		lifedata.setStory(story);
		startActivity(intent);
	}

	private void saveAction() {
		// saving any illustrations
		ArrayList<Media> ills = lifedata.getCurrImages();
		for (Media ill : ills) {
			mediaCon.insert(ill);
		}

		// saving any choices
		ArrayList<Choice> choices = lifedata.getCurrChoices();
		for (Choice choice : choices) {
				choiceCon.insert(choice);
		}
	
		lifedata.setCurrChoices(null);
		lifedata.setCurrImages(null);

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

	public void setMediaCon(MediaController mediaCon) {
		this.mediaCon = mediaCon;
	}

	public MediaController getMediaCon() {
		return mediaCon;
	}

}
