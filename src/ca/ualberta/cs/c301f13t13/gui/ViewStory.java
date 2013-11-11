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

import java.util.UUID;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * Handles viewing the story metadata.
 * 
 * @author Alexander Wong
 * 
 */
public class ViewStory extends Activity {
	Story focusedStory;
	SHController gc;

	private ImageView storyCover;
	private TextView storyTitle;
	private TextView storyAuthor;
	private TextView storyDescription;
	private Button beginReading;
	private UUID storyID;

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
		focusedStory = gc.getCompleteStory(storyID);
		//storyCover.setImageBitmap(focusedStory.getImage());
		// Check no title
		if (focusedStory.getTitle().equals("")) {
			storyTitle.setText("<No Title>");
		} else {
			storyTitle.setText(focusedStory.getTitle());
		}
		// Check no author
		if (focusedStory.getAuthor().equals("")) {
			storyAuthor.setText("<No Author>");
		} else {
			storyAuthor.setText(focusedStory.getAuthor());
		}
		// Check no description
		if (focusedStory.getDescription().equals("")) {
			storyDescription.setText("<No Description>");
		} else {
			storyDescription.setText(focusedStory.getDescription());
		}
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
		switch (item.getItemId()) {
		case R.id.editExistingStory:
			// Handle editing the story content, like chapters and choices
			intent = new Intent(this, ViewBrowseChapters.class);
			intent.putExtra("storyID", storyID);
			startActivity(intent);
			return true;
		case R.id.editStoryMetaData:
			intent = new Intent(this, EditStoryActivity.class);
			intent.putExtra("isEditing", true);
			intent.putExtra("storyID", focusedStory.getId());
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Initialize private fields
	 */
	public void setUpFields() {
		// Grab GC and story info
		gc = SHController.getInstance(this);
		Bundle bundle = this.getIntent().getExtras();
		storyID = (UUID) bundle.getSerializable("storyID");
		focusedStory = gc.getCompleteStory(storyID);

		// Initialize the activity fields
		storyCover = (ImageView) findViewById(R.id.storyImage);
		storyTitle = (TextView) findViewById(R.id.storyTitle);
		storyAuthor = (TextView) findViewById(R.id.storyAuthor);
		storyDescription = (TextView) findViewById(R.id.storyDescription);
		beginReading = (Button) findViewById(R.id.viewFirstChapter);

		// Set up action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Story Metadata");
		actionBar.setDisplayShowTitleEnabled(true);
	}

	/**
	 * set begin reading OnClickListener
	 */
	public void setBeginReading() {
		beginReading.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Begin reading, go to first chapter
				Intent intent = new Intent(getBaseContext(), ViewChapter.class);
				intent.putExtra("storyID", storyID);
				intent.putExtra("chapterID", focusedStory.getFirstChapterId());
				startActivity(intent);
				finish();
			}
		});
	}
}
