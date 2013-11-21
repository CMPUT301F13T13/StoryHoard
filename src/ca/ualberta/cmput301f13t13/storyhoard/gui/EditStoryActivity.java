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
import ca.ualberta.cmput301f13t13.storyhoard.backend.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ObjectType;
import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;

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
	private Story newStory;
	private SHController gc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        try {
	          Class.forName("android.os.AsyncTask");
	        } catch (ClassNotFoundException e) {
	          e.printStackTrace();
	        }

		lifedata = LifecycleData.getInstance();

		setContentView(R.layout.activity_edit_story);

		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("Story Details");
		actionBar.setDisplayShowTitleEnabled(true);
		
		gc = SHController.getInstance(this);

		newTitle = (EditText) findViewById(R.id.newStoryTitle);
		newAuthor = (EditText) findViewById(R.id.newStoryAuthor);
		newDescription = (EditText) findViewById(R.id.newStoryDescription);

		// Check if we are editing the story or making a new story
		if (lifedata.isEditing()) {
			newStory = lifedata.getStory();
			newTitle.setText(newStory.getTitle());
			newAuthor.setText(newStory.getAuthor());
			newDescription.setText(newStory.getDescription());
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_edit_story, menu);
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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void publishStory() {
		if (lifedata.isEditing()) {
			// publish new story somehow
			saveChanges();
			new Update().execute();
			Toast.makeText(getBaseContext(),
					"Story published to server", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(getBaseContext(),
					"Create a story before publishing", Toast.LENGTH_SHORT)
					.show();
		}		
	}
	
	private class Update extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// publish or update story 
			gc.updateObject(gc.getCompleteStory(newStory.getId(), lifedata.getStoryType()),
					ObjectType.PUBLISHED_STORY);
			return null;
		}
	}


	private void saveChanges() {
		String title = newTitle.getText().toString();
		String author = newAuthor.getText().toString();
		String description = newDescription.getText().toString();
		if (lifedata.isEditing()) {
			newStory.setAuthor(author);
			newStory.setTitle(title);
			newStory.setDescription(description);
			lifedata.setStory(newStory);
			
			// May not be needed...
			if (lifedata.getStoryType().equals(ObjectType.CREATED_STORY)) {
				gc.updateObject(newStory, ObjectType.CREATED_STORY);
			} else {
				gc.updateObject(newStory, ObjectType.CACHED_STORY);
			}
//			gc.updateObject(newStory, ObjectType.CREATED_STORY);
		} else {
			newStory = new Story(title, author, description, 
					Utilities.getPhoneId(getBaseContext()));
			Intent intent = new Intent(EditStoryActivity.this,
					EditChapterActivity.class);

			lifedata.setEditing(false);
			lifedata.setFirstStory(true);
			lifedata.setStory(newStory);
			startActivity(intent);
		}
		finish();
	}
}
