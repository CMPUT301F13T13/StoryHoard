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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.MediaController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.local.LifecycleData;
/**
 * Views the chapter provided through the intent. Does not allow going backwards
 * through the activity stack.
 * 
 * @author Alexander Wong
 * @author Kim Wu
 * 
 */
public class ViewChapter extends MediaActivity {
	LifecycleData lifedata;
	private ChapterController chapCon;
	private ChoiceController choiceCon;
	private MediaController mediaCon;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private ArrayList<Media> photoList;
	private ArrayList<Media> illList;
	private AdapterChoices choiceAdapter;
	private LinearLayout illustrations;
	private TextView chapterContent;
	private ListView chapterChoices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_chapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
		setNextChapterListener();
		updateData();
	}

	/**
	 * Initializes the private fields needed.
	 */
	public void setUpFields() {
		lifedata = LifecycleData.getInstance();
		chapCon = ChapterController.getInstance(this);
		choiceCon = ChoiceController.getInstance(this);
		mediaCon = MediaController.getInstance(this);

		// Setup the activity fields
		chapterContent = (TextView) findViewById(R.id.chapterContent);
		chapterChoices = (ListView) findViewById(R.id.chapterChoices);
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
		chapter = lifedata.getChapter();

		// Check to see if the chapter exists, else terminate
		if (chapter == null) {
			Toast.makeText(getBaseContext(), "Next chapter does not exist",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		// Check for no chapter text
		if (chapter.getText().equals("")) {
			chapterContent.setText("<No Chapter Content>");
		} else {
			chapterContent.setText(chapter.getText());
		}
		
		insertChoices();
		insertIllustrations();
		insertPhotos();
	}
	
	public void insertChoices() {
		choices.clear();
		ArrayList<Choice> chapChoices = chapter.getChoices();
		// Check for no choices
		if (chapChoices.isEmpty()) {
			chapterContent.setText(chapterContent.getText()
					+ "\n\n<No Choices>");
		} else {
			if (chapter.hasRandomChoice() && chapChoices.size() > 1) {
				chapChoices.add(choiceCon.getRandomChoice(chapter.getId()));
			}
			choices.addAll(chapChoices);

		}
		choiceAdapter.notifyDataSetChanged();		
	}

	public void insertIllustrations() {
		illList = chapter.getIllustrations();

		// photos.removeAllViews();
		illustrations.removeAllViews();

		// Insert Illustrations
		for (Media ill : illList) {
			insertImage(ill, this, illustrations);
		}		
	}
	
	public void insertPhotos() {
		// Insert Photos
		Media img = lifedata.getCurrImage();
		if (img != null) {
			mediaCon.insert(img);
			lifedata.setCurrImage(null);
			lifedata.setCurrImages(null);
		}
		
		// set listener to display photo text on click 
		photoList = mediaCon.getPhotosByChapter(chapter.getId());
		for (Media photo : photoList) {
			View v = insertImage(photo, this, illustrations);
			v.setOnClickListener(new OnClickListener () {
				@Override
				public void onClick(View v) {
					Media media = (Media) v.getTag();
					Toast.makeText(getBaseContext(),
							media.getText(), Toast.LENGTH_LONG).show();	
				}
			});		
		}		
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

				UUID nextChap = choices.get(arg2).getNextChapter();
				lifedata.setChapter(chapCon.getFullChapter(nextChap));

				startActivity(intent);
				// photos.removeAllViews();
				illustrations.removeAllViews();
				finish();
			}
		});
	}

	/**
	 * Menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_chapter, menu);
		return true;
	}

	/**
	 * Menu
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addPhoto:
			addPhoto();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
