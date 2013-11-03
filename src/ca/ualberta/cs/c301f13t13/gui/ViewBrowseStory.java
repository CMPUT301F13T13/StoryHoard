package ca.ualberta.cs.c301f13t13.gui;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.GeneralController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

public class ViewBrowseStory extends Activity {
	Story focusedStory;
	GeneralController gc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_story);

		ImageView storyCover = (ImageView) findViewById(R.id.storyImage);
		TextView storyTitle = (TextView) findViewById(R.id.storyTitle);
		TextView storyAuthor = (TextView) findViewById(R.id.storyAuthor);
		TextView storyDescription = (TextView) findViewById(R.id.storyDescription);

		// Initialize the general controller and grab the story
		gc = GeneralController.getInstance();
		Bundle bundle = this.getIntent().getExtras();
		try {
			UUID storyID = null;
			if (bundle != null) {
				storyID = (UUID) bundle.getSerializable("storyID");
			} else {
				Toast.makeText(this, "StoryID failed to pass through activity",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			focusedStory = gc.getCompleteStory(storyID, this);
		} finally {
			// Check if the story exists or not
			if (focusedStory == null) {
				Toast.makeText(this,
						"Story does not exist in back end. Database problem?",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}

		// Populate the views
		storyCover.setImageBitmap(focusedStory.getImage());
		storyTitle.setText(focusedStory.getTitle());
		storyAuthor.setText(focusedStory.getAuthor());
		storyDescription.setText(focusedStory.getDescription());
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
			//intent = new Intent(this, EditStoryActivity.class);
			//startActivity(intent);
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
}
