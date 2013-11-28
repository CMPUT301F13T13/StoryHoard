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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapController;
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
	private ChapController chapCon;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private AdapterChoices choiceAdapter;
	private LinearLayout illustrations;
	private TextView chapterContent;
	private ListView chapterChoices;
	private ProgressDialog progressDialog;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_chapter, menu);
		return true;
	}

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
	
	/**
	 * Initializes the private fields needed.
	 */
	public void setUpFields() {
		lifedata = LifecycleData.getInstance();
		chapCon = ChapController.getInstance(this);

		// Setup the activity fields
		chapterContent = (TextView) findViewById(R.id.chapterContent);
		chapterChoices = (ListView) findViewById(R.id.chapterChoices);
		illustrations = (LinearLayout) findViewById(R.id.horizontalIllustraions);

		// Setup the choices and choice adapters
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		chapterChoices.setAdapter(choiceAdapter);
	}

	/**
	 * Gets the new chapter and updates the view's components.
	 */
	public void updateData() {	
		chapter = chapCon.getCurrChapter();

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
		choices = chapter.getChoices();
		// Check for no choices
		if (choices.isEmpty()) {
			chapterContent.setText(chapterContent.getText()
					+ "\n\n<No Choices>");
		} else {
			if (chapter.hasRandomChoice() && choices.size() > 1) {
				chapCon.addRandomChoice();
				chapter = chapCon.getCurrChapter();
			}
			choices.addAll(choices);

		}
		choiceAdapter.notifyDataSetChanged();		
	}

	public void insertIllustrations() {
		illustrations.removeAllViews();

		// Insert Illustrations
		for (Media ill : chapter.getIllustrations()) {
			insertImage(ill, this, illustrations);
		}		
	}
	
	public void insertPhotos() {		
		// set listener to display photo text on click 
		for (Media photo : chapter.getPhotos()) {
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
	
				UUID nextChap = choices.get(arg2).getNextChapter();
				new LoadChapter().execute(nextChap);
			}
		});
	}

	/**
	 * Async task to get all the chapter information from the database, including media and 
	 * choices.
	 *
	 */
	private class LoadChapter extends AsyncTask<UUID, Void, Void>{
	    @Override
	    protected void onPreExecute()
	    {	
	        progressDialog= ProgressDialog.show(
	        		ViewChapter.this, 
	        		"Loading Chapter",
	        		"Please wait...", 
	        		true);

	    };  
	    
		@Override
		protected synchronized Void doInBackground(UUID... params) {	
			chapCon.setCurrChapter(params[0]);
			return null;
		}
		
		@Override 
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// Go to the chapter in question
			Intent intent = new Intent(getBaseContext(), ViewChapter.class);
			startActivity(intent);
			progressDialog.dismiss();
			finish();
		}
	}		
}
