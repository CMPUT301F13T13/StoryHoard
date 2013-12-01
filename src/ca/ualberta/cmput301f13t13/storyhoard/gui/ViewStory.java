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

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;

/**
 * Handles viewing the story details. Will display the story title, author, and
 * the description. Will also allow the user to navigate to the edit story
 * details activity, as well as the view chapter and view browse chapters
 * activity.
 * 
 * @author Alexander Wong
 * 
 */
public class ViewStory extends Activity {
	LifecycleData lifedata;
	private ChapterController chapCon;
	private StoryController storyCon;
	private Story story;
	private TextView storyTitle;
	private TextView storyAuthor;
	private TextView storyDescription;
	private Button beginReading;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse_story);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
		setBeginReading();
	}

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            this.finish();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_browse_story, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		lifedata.setEditing(true);
		switch (item.getItemId()) {
		case R.id.editExistingStory:
			intent = new Intent(this, ViewBrowseChapters.class);
			startActivity(intent);
			return true;
		case R.id.editStoryMetaData:
			intent = new Intent(this, EditStoryActivity.class);
			startActivity(intent);
			return true;
		case R.id.info:
			getHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Initialize private fields
	 */
	public void setUpFields() {
		
		// Initialize the activity fields
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(this);
		chapCon = ChapterController.getInstance(this);
		storyTitle = (TextView) findViewById(R.id.storyTitle);
		storyAuthor = (TextView) findViewById(R.id.storyAuthor);
		storyDescription = (TextView) findViewById(R.id.storyDescription);
		beginReading = (Button) findViewById(R.id.viewFirstChapter);

		// Set up action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Story Information");
		actionBar.setDisplayShowTitleEnabled(true);
		
		story = storyCon.getCurrStory();

		// Check no title
		if (story.getTitle().equals("")) {
			storyTitle.setText("<No Title>");
		} else {
			storyTitle.setText(story.getTitle());
		}
		// Check no author
		if (story.getAuthor().equals("")) {
			storyAuthor.setText("<No Author>");
		} else {
			storyAuthor.setText(story.getAuthor());
		}
		// Check no description
		if (story.getDescription().equals("")) {
			storyDescription.setText("<No Description>");
		} else {
			storyDescription.setText(story.getDescription());
		}
	}

	/**
	 * set begin reading OnClickListener
	 */
	public void setBeginReading() {
		beginReading.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Begin reading, go to first chapter
				UUID firstChapterID = story.getFirstChapterId();
				new LoadChapter().execute(firstChapterID);
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
	    protected void onPreExecute() {	
	        progressDialog= ProgressDialog.show(
	        		ViewStory.this, 
	        		"Loading Chapter",
	        		"Please wait...", 
	        		true);

	    };  
	    
		@Override
		protected synchronized Void doInBackground(UUID... params) {	
			chapCon.setCurrChapterIncomplete(params[0]);
			return null;
		}
		
		@Override 
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(getBaseContext(), ViewChapter.class);
			startActivity(intent);
			progressDialog.dismiss();

		}
	}		
	
	/**
	 * Displays the help guide for the View Story activity
	 */
	private void getHelp() {
		Intent intent = new Intent(this, InfoActivity.class);
		String helpInfo = "\t- Story data is displayed on this screen, "+
				"arranged from top to bottom by 'Title', 'Author', and "+
				"'Description'.\n\n "+
				"\t- To read the story, press the 'Begin Reading' button.\n\n"+
				"\t- To edit the story content, press the 'Edit Story' button.\n\n"+
				"\t- To edit the story settings, press the 'Settings' button.\n\n"+
				"\t- To navigate back to the main screen, press the back button on "+
				"your mobile device.\n";
		intent.putExtra("theHelp", helpInfo);
		startActivity(intent);
	}
}
