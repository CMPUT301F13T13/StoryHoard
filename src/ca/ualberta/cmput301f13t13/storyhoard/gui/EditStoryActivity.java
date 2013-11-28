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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.LocalStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ServerStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

/**
 * Activity for editing the story metadata (title, author, description, and
 * cover image).
 * 
 * @author Alexander Wong
 * 
 */
public class EditStoryActivity extends Activity {
	LifecycleData lifedata;
	private EditText newTitle;
	private EditText newAuthor;
	private EditText newDescription;
	private StoryController storyCon;
	private ServerStoryController serverCon;
	private LocalStoryController localCon;
	private Story newStory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		setupFields();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_edit_story, menu);
		if (!localCon.isPublishedStoryMyStory(newStory, getBaseContext())) {
			menu.removeItem(R.id.unpublishStory);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.publishStory:
			publishStory();
			return true;
		case R.id.addfirstChapter:
			saveChanges();
			finish();
			return true;
		case R.id.unpublishStory:
			unpublishStory();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupFields() {
		lifedata = LifecycleData.getInstance();
		localCon = LocalStoryController.getInstance(this);
		storyCon = StoryController.getInstance(this);
		serverCon = ServerStoryController.getInstance(this);

		setContentView(R.layout.activity_edit_story);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Story Details");
		actionBar.setDisplayShowTitleEnabled(true);

		newTitle = (EditText) findViewById(R.id.newStoryTitle);
		newAuthor = (EditText) findViewById(R.id.newStoryAuthor);
		newDescription = (EditText) findViewById(R.id.newStoryDescription);

		// Check if we are editing the story or making a new story
		if (lifedata.isEditing()) {
			newStory = storyCon.getCurrStory();
			newTitle.setText(newStory.getTitle());
			newAuthor.setText(newStory.getAuthor());
			newDescription.setText(newStory.getDescription());
		}
	}

	private void publishStory() {
		if (lifedata.isEditing()) {
			// publish new story somehow
			saveChanges();
			new Update().execute();
			Toast.makeText(getBaseContext(), "Story published to server",
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(getBaseContext(),
					"Create a story before publishing", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void unpublishStory() {
		new UnPublish().execute(newStory.getId());
		Toast.makeText(getBaseContext(), "Unpublished story from server",
				Toast.LENGTH_SHORT).show();
		
	}

	private class Update extends AsyncTask<Void, Void, Void> {
		@Override
		protected synchronized Void doInBackground(Void... params) {
			// publish or update story
			storyCon.pushChangesToServer();
			return null;
		}
	}

	private class UnPublish extends AsyncTask<UUID, Void, Void>{
		@Override
		protected synchronized Void doInBackground(UUID... params) {
			// publish or update story
			serverCon.remove(params[0]);
			return null;
		}
	}

	private void saveChanges() {
		String title = newTitle.getText().toString();
		String author = newAuthor.getText().toString();
		String description = newDescription.getText().toString();
		if (lifedata.isEditing()) {
			storyCon.editAuthor(author);
			storyCon.editTitle(title);
			storyCon.editDescription(description);
			storyCon.pushChangesToDb();
		} else {
			newStory = new Story(title, author, description,
					Utilities.getPhoneId(getBaseContext()));
			lifedata.setEditing(false);
			lifedata.setFirstStory(true);
			storyCon.setCurrStoryComplete(newStory);
			Intent intent = new Intent(EditStoryActivity.this,
					EditChapterActivity.class);
			startActivity(intent);
		}
	}
}
