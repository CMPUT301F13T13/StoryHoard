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
import java.util.UUID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;

/**
 * Views the chapter provided through the intent. Does not allow going backwards
 * through the activity stack.
 * 
 * @author Alexander Wong
 * 
 */
public class ViewChapter extends MediaActivity {
	LifecycleData lifedata;
	private SHController gc;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private ArrayList<Media> photoList;
	private ArrayList<Media> illList;
	private AdapterChoices choiceAdapter;
	private AlertDialog photoDialog;
	private LinearLayout illustrations;
	// private LinearLayout photos;

	private TextView chapterContent;
	private ListView chapterChoices;
	private Button addPhotoButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_chapter);
		setUpFields();
	}

	@Override
	public void onResume() {
		super.onResume();
		setNextChapterListener();
		setAddPhotoListener();
		updateData();
	}

	/**
	 * Initializes the private fields needed.
	 */
	public void setUpFields() {
		lifedata = LifecycleData.getInstance();
		gc = SHController.getInstance(this);

		// Setup the activity fields
		chapterContent = (TextView) findViewById(R.id.chapterContent);
		chapterChoices = (ListView) findViewById(R.id.chapterChoices);
		addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
		illustrations = (LinearLayout) findViewById(R.id.horizontalIllustraions);
		// photos = (LinearLayout) findViewById(R.id.horizontalPhotos);

		// Setup the choices and choice adapters
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		chapterChoices.setAdapter(choiceAdapter);
	}

	/**
	 * Gets the new chapter and updates the view's components.
	 */
	public void updateData() {
		UUID chapterID = lifedata.getChapterID();
		chapter = gc.getCompleteChapter(chapterID);
		choices.clear();

		// Check to see if the chapter exists, else terminate
		if (chapter == null) {
			Toast.makeText(getBaseContext(), "Chapter does not exist", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		// Check for no chapter text
		if (chapter.getText().equals("")) {
			chapterContent.setText("<No Chapter Content>");
		} else {
			chapterContent.setText(chapter.getText());
		}
		// Check for no choices
		if (chapter.getChoices().isEmpty()) {
			chapterContent.setText(chapterContent.getText()
					+ "\n\n<No Choices>");
		} else {
			choices.addAll(chapter.getChoices());
			if (chapter.getRandomChoice() == true) {
				choices.add(gc.getRandomChoice(chapterID));
			}
		}
		choiceAdapter.notifyDataSetChanged();

		photoList = chapter.getPhotos();
		illList = chapter.getIllustrations();
		
		// photos.removeAllViews();
		illustrations.removeAllViews();
		// Insert Photos
		for (Media photo : photoList) {
			illustrations.addView(insertImage(photo, this));
		}
		// Insert Illustrations
		for (Media ill : illList) {
			illustrations.addView(insertImage(ill, this));
		}
	}

	/**
	 * Sets up the onClick listener for the button to add a new photo.
	 */
	public void setAddPhotoListener() {
		addPhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(
						ViewChapter.this);
				// Set dialog title
				alert.setTitle("Choose method:");
				// Options that user may choose to add photo
				final String[] methods = { "Take Photo", "Choose from Gallery" };
				alert.setSingleChoiceItems(methods, -1,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int item) {
								switch (item) {
								case 0:
									takePhoto(Media.PHOTO);
									break;
								case 1:
									browseGallery(Media.PHOTO);
									break;
								}
								photoDialog.dismiss();
							}
						});
				photoDialog = alert.create();
				photoDialog.show();
			}
		});
	}

	/**
	 * Sets up the onClick listener for the button to flip to the next chapter
	 * (selecting a choice).
	 */
	public void setNextChapterListener() {
		chapterChoices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Go to the chapter in question
				Intent intent = new Intent(getBaseContext(), ViewChapter.class);
				lifedata.setChapter(gc.getCompleteChapter(choices.get(arg2).getNextChapter()));
				startActivity(intent);
				//photos.removeAllViews();
				illustrations.removeAllViews();
				finish();
			}
		});
	}
}
