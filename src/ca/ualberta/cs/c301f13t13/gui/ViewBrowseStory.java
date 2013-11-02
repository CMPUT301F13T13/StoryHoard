package ca.ualberta.cs.c301f13t13.gui;

import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
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
			// CURRENTLY BROKEN. WAITING FOR STEPH/ASHLEY FIX
			// focusedStory = gc.getCompleteStory(storyID, this);
		} finally {
			// Check if the story exists or not
			if (focusedStory == null) {
				Toast.makeText(this,
						"Story does not exist in back end. Database problem?",
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}

	}
}
