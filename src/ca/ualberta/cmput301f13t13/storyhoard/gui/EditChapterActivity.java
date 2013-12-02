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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;

/**
 * Add Chapter Activity
 * 
 * Purpose: - To add a chapter to an existing story. - The author can: - Add
 * images through the use of the image button - Set the text of the chapter
 * through the Edit text space - View all chapters in the story upon pressing
 * the 'View All Chapters' button - Add a choice by pressing the 'Add Choice'
 * button. - This activity will also display the choices that exist or have been
 * added.
 * 
 * @author Alexander Wong
 * @author Kim Wu
 */

public class EditChapterActivity extends MediaActivity {
	private DeleteImageDialog dialBuilder = new DeleteImageDialog();
	LifecycleData lifedata;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private ListView viewChoices;
	private EditText chapterContent;
	private StoryController storyCon;
	private ChapterController chapCon;
	private ChoiceController choiceCon;

	private AdapterChoices choiceAdapter;
	private AlertDialog illustDialog;
	private LinearLayout illustrations;
	private CheckBox randChoiceCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_chapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
		updateICData();
	}

	/**
	 * Creates options in the menu bar
	 * 
	 * Add Choice: Allows user to add choice to chapter
	 * 
	 * Add Illus: Allows user to add an illustration to chapter
	 * 
	 * Save: Allows user to save chapter
	 * 
	 * Info: Displays help guide to help user navigate this activity
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_edit_chapter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.addChoice:
			addChoice();
			return true;
		case R.id.addIllus:
			addIllustration();
			return true;
		case R.id.Save:
			saveAction();
			return true;
		case R.id.info:
			getHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Ensures that the user creates a first chapter when creating a story
	 */
	public void onBackPressed() {
		if (lifedata.isFirstStory()) {
			Toast.makeText(getBaseContext(), "Must first create first chapter",
					Toast.LENGTH_SHORT).show();
		} else {
			finish();
		}
	}

	/**
	 * Sets up the fields, and gets the bundle from the intent.
	 */
	private void setUpFields() {
		chapCon = ChapterController.getInstance(this);
		storyCon = StoryController.getInstance(this);
		choiceCon = ChoiceController.getInstance(this);

		lifedata = LifecycleData.getInstance();
		chapterContent = (EditText) findViewById(R.id.chapterEditText);
		viewChoices = (ListView) findViewById(R.id.chapterEditChoices);
		illustrations = (LinearLayout) findViewById(R.id.editHorizontalIllustrations);
		randChoiceCheck = (CheckBox) findViewById(R.id.randChoiceCheck);

		// Setup the adapter
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		viewChoices.setAdapter(choiceAdapter);

		// handle setting up editing a choice
		viewChoices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				lifedata.setEditingChoice(true);
				choiceCon.setCurrChoice(choices.get(arg2));
				Intent intent = new Intent(EditChapterActivity.this,
						EditChoiceActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Updates the view components depending on the chapter data.
	 */
	protected void updateICData() {
		if (lifedata.isEditing()) {

			// Editing an existing chapter
			chapter = chapCon.getCurrChapter();
			chapterContent.setText(chapter.getText());
		} else {
			// Create a new chapter from the story's ID
			chapter = new Chapter(storyCon.getCurrStory().getId(), "");
			chapCon.setCurrChapterComplete(chapter);
			lifedata.setEditing(true);
		}

		// set up choices
		setRandomChoice();
		choices.clear();
		choices.addAll(chapter.getChoices());
		choiceAdapter.notifyDataSetChanged();

		setUpIllustrations();
	}

	/**
	 * Retrieves and sets up the illustrations for the chapter.
	 */
	public void setUpIllustrations() {
		// Clean up illustrations layout
		illustrations.removeAllViews();

		for (Media img : chapter.getIllustrations()) {
			View view = insertImage(img, EditChapterActivity.this,
					illustrations);
			view.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					dialBuilder.setDeleteDialog(EditChapterActivity.this, v,
							illustrations);
					return true;
				}
			});
		}
	}

	/**
	 * Set onClick listener for setting random choice
	 */
	public void setRandomChoice() {
		// If the chapter has been set to random choice, check box
		if (chapter.hasRandomChoice()) {
			randChoiceCheck.setChecked(true);
		}

		randChoiceCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// If checked, set random choice on chapter
				if (randChoiceCheck.isChecked()) {
					chapCon.editRandomChoice(true);
				} else {
					chapCon.editRandomChoice(false);
				}
			}
		});
	}

	/**
	 * Displays a dialog that allows user to add an illustration to a chapter.
	 * 
	 * User may add an illustration via Image Gallery or by taking a photo
	 */
	private void addIllustration() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		chapCon.editText(chapterContent.getText().toString());

		// Set dialog title
		alert.setTitle("Choose method:");

		// Options that user may choose to add illustration
		final String[] methods = { "Take Photo", "Choose from Gallery" };
		alert.setSingleChoiceItems(methods, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							takePhoto(Media.ILLUSTRATION);
							break;
						case 1:
							browseGallery(Media.ILLUSTRATION);
							break;
						}
						illustDialog.dismiss();
					}
				});

		illustDialog = alert.create();
		illustDialog.show();
	}

	/**
	 * Starts the EditChoiceActivity to link chapters
	 */
	private void addChoice() {
		chapCon.editText(chapterContent.getText().toString());
		Intent intent = new Intent(getBaseContext(), EditChoiceActivity.class);
		lifedata.setEditingChoice(false);
		startActivity(intent);
	}

	private void saveAction() {
		new SaveChapter().execute();
	}

	/**
	 * Async task to get all the chapter information from the database,
	 * including media and choices.
	 * 
	 */
	private class SaveChapter extends AsyncTask<Void, Void, Void> {
		@Override
		protected synchronized Void doInBackground(Void... params) {
			chapCon.editText(chapterContent.getText().toString());
			chapCon.pushChangesToDb();

			if (lifedata.isEditing()) {
				storyCon.updateChapter(chapter);
			} else {
				storyCon.addChapter(chapter);
			}

			if (lifedata.isFirstStory()) {
				storyCon.editFirstChapterId(chapter.getId());
				storyCon.pushChangesToDb();
				lifedata.setFirstStory(false);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			finish();
		}
	}

	/**
	 * Displays help guide for EditChapterActivity
	 */
	private void getHelp() {
		Intent intent = new Intent(this, InfoActivity.class);
		String helpInfo = "Simply enter text to set a chapters content.\n\n"
				+ "To add an illustration to your chapter, click on image icon in bottom left corner.\n\n"
				+ "To save your chapter, click on disk icon to the left of the info icon.\n\n"
				+ "Adding a choice:\n\n"
				+ "\t- Adding a choice allows you to link the current chapter you are editing to another one in your story.\n\n"
				+ "\t- You can set the text of your choice in the given text box.\n\n"
				+ "\t- To add a choice, simply click on one of the given chapters available in the list.\n\n"
				+ "\t- In addition, you can set a random choice by checking the 'Set random choice' option. "
				+ "This feature allows for the option of selecting a chapter at random in reading mode.\n";
		intent.putExtra("theHelp", helpInfo);
		startActivity(intent);
	}
}
