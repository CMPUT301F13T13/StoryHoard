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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Activity for editing the story details (title, author, description, and cover
 * image). Also allows a user to publish the story to the server, or if the user
 * owns the story, to unpublish the story from the server.
 * 
 * @author Alexander Wong
 * @author Kim Wu
 * 
 */
public class EditStoryActivity extends Activity {
	LifecycleData lifedata;
	private EditText newTitle;
	private EditText newAuthor;
	private EditText newDescription;
	private StoryController storyCon;
	private ServerManager serverMan;
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
		if (!Utilities.isPublishedStoryMyStory(newStory, getBaseContext())) {
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
			return true;
		case R.id.unpublishStory:
			unpublishStory();
			return true;
		case R.id.info:
			getHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupFields() {
		lifedata = LifecycleData.getInstance();
		storyCon = StoryController.getInstance(this);
		serverMan = ServerManager.getInstance();

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
		} else {
			Toast.makeText(getBaseContext(),
					"Create a story before publishing", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void unpublishStory() {
		if (lifedata.isEditing()) {
			new UnPublish().execute(newStory.getId());
			Toast.makeText(getBaseContext(), "Unpublished story from server",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getBaseContext(),
					"Create a story before unpublishing", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private class Update extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected synchronized Boolean doInBackground(Void... params) {
			// publish or update story
			return storyCon.pushChangesToServer();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				Toast.makeText(getBaseContext(), "Story published to server",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(getBaseContext(), "Problems with server. Please"
						+ " try again.",
						Toast.LENGTH_SHORT).show();
			}
		}		
	}

	private class UnPublish extends AsyncTask<UUID, Void, Void> {
		@Override
		protected synchronized Void doInBackground(UUID... params) {
			// publish or update story
			serverMan.remove(params[0].toString());
			return null;
		}
	}

	private void saveChanges() {
		String title = newTitle.getText().toString();
		if (validTitle(title)) {
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
			finish();
		} else {
			alertDialog();
		}
	}

	private boolean validTitle(String title) {
		title = title.trim();
		int length = title.length();
		if (length == 0) {
			return false;
		} else {
			return true;
		}
	}

	private void alertDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(
				EditStoryActivity.this);
		alert.setTitle("Whoopsies!").setMessage("Story title is empty/invalid")
				.setCancelable(false)
				// cannot dismiss this dialog
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}); // parenthesis mean an anonymous class
		// Show alert dialog
		AlertDialog show_alert = alert.create();
		show_alert.show();
	}
	
		private void getHelp() {
		Intent intent = new Intent(this, InfoActivity.class);
		String helpInfo = "This activity allows you to edit your story details.\n\n"
				+ "You can set the title of the story in the first text box, "
				+ "then add an author name in the next one, "
				+ "followed by a brief description of your story in the last text box.\n\n"
				+ "You may also publish your story for the world to see "
				+ "by pressing 'Publish Story'.\n\n"
				+ "If you decide to unpublish your story, you may do so "
				+ "by pressing 'Unpublish Story'.\n\n"
				+ "To save your story settings, click 'Save Changes'.\n";
		intent.putExtra("theHelp", helpInfo);
		startActivity(intent);
	}
}
