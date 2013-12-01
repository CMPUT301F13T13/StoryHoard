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

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Class which displays all stories in a grid. The story will be in one of three
 * types, the 'My Stories', 'Downloaded Stories', and 'Published Stories' types.
 * My Stories are local stories that the user creates.
 * Downloaded Stories are stories that the user downloads from the server.
 * Published Stories are stories that exist on the server.
 * 
 * @author alexanderwong
 * @author Kim Wu
 * 
 */
public class ViewBrowseStories extends Activity {
	LifecycleData lifedata;
	private GridView gridView;
	private ArrayList<Story> gridArray = new ArrayList<Story>();
	private AdapterStories customGridAdapter;
	private AlertDialog overwriteDialog;
	private StoryManager storyMan;
	private Syncher syncher;
	private ServerManager serverMan;
	private enum Type {CREATED, CACHED, PUBLISHED};
	private Type viewType;
	private StoryController storyCon;
	private ProgressDialog progressDialog;
	ArrayList<Story> currentStories;

	/**
	 * Create the View Browse Stories activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse_stories);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setGridView();
		storyCon = StoryController.getInstance(this);
		lifedata = LifecycleData.getInstance();
		serverMan = ServerManager.getInstance();
		storyMan = StoryManager.getInstance(this);
		syncher = Syncher.getInstance(this);
		setActionBar();
		refreshStories();
	}	

	private void setActionBar() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("StoryHoard");
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Setup the action bar items
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				new String[] {
						getString(R.string.title_viewBrowseStories_MyStories),
						getString(R.string.title_viewBrowseStories_CachedStories),
						getString(R.string.title_viewBrowseStories_PublishedStories), });

		// Setup the action bar listener
		actionBar.setListNavigationCallbacks(adapter,
				new OnNavigationListener() {
					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						if (itemPosition == 0) {
							viewType = Type.CREATED;
						} else if (itemPosition == 1) {
							viewType = Type.CACHED;
						} else if (itemPosition == 2) {
							viewType = Type.PUBLISHED;
						}
						new GetAllStories().execute();
						return true;
					}
				});
	}
	
	/**
	 * Handle the creation of the View Browse Stories activity menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_browse_stories, menu);
		return true;
	}

	/**
	 * Handle the selection of the View Browse Stories activity menu items
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.addNewStory:
			intent = new Intent(this, EditStoryActivity.class);
			// Pass it a boolean to indicate it is not editing
			lifedata.setFirstStory(true);
			lifedata.setEditing(false);
			startActivity(intent);
			return true;
		case R.id.searchStories:
			intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			return true;
		case R.id.lucky:
			Story randomStory = serverMan.getRandom();
			storyCon.setCurrStoryComplete(randomStory);

			if (randomStory != null) {
				new CacheStory().execute();
			} else {
				Toast.makeText(getBaseContext(),
						"No Published Stories Available", Toast.LENGTH_LONG)
						.show();
			}
			return true;
		case R.id.info:
			getHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Called whenever the spinner is updated. Will story array based on
	 * whatever the general controller returns.
	 */
	private void refreshStories() {
		gridArray.clear();
		if (currentStories != null) {
			gridArray.addAll(currentStories);
		}
		customGridAdapter.notifyDataSetChanged();
	}
	
	private void setGridView() {
		// Setup the grid view for the stories
		gridView = (GridView) findViewById(R.id.gridStoriesView);
		customGridAdapter = new AdapterStories(this,
				R.layout.browse_story_item, gridArray);
		gridView.setAdapter(customGridAdapter);

		// Setup the grid view click listener
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (viewType == Type.PUBLISHED) {
					storyCon.setCurrStoryComplete(gridArray.get(arg2));
					if (storyMan.existsLocally(gridArray.get(arg2).getId())) {
						overwriteStory();
					} else {
						new CacheStory().execute();
					}
					return;
				}
				storyCon.setCurrStoryIncomplete(gridArray.get(arg2));
				Intent intent = new Intent(getBaseContext(), ViewStory.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * Displays the dialog that handles choosing to overwrite a story or not. If
	 * user chooses to potentially overwrite the story, return true otherwise
	 * return false
	 */
	private void overwriteStory() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Overwrite local version of this story?");
		final String[] overwriteChoices = { "Proceed", "Cancel" };
		alert.setSingleChoiceItems(overwriteChoices, -1, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					CacheStory storytoCache = new CacheStory();
					storytoCache.execute();
					break;
				case 1:
					break;
				}
				overwriteDialog.dismiss();
			}
		});
		overwriteDialog = alert.create();
		overwriteDialog.show();
	}



	/**
	 * Caches (and locally mirrors) a story in the phone's database. This
	 * includes converting all the encoded strings for the story's chapters back
	 * to bitmaps, saving them on to the SD card, and inserts all the chapter's
	 * medias and choices. In order to increase performance for some of those
	 * heavy operations, an async task is used.
	 * 
	 */
	private class CacheStory extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(ViewBrowseStories.this,
					"Downloading Story", "Please wait...", true);
		};

		@Override
		protected synchronized Void doInBackground(Void... params) {
			syncher.syncStoryFromServer(storyCon.getCurrStory());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Intent intent = new Intent(getBaseContext(), ViewStory.class);
			startActivity(intent);
			progressDialog.dismiss();
		}
	}

	/**
	 * Async task to get all stories of a type in the database. Used so main UI
	 * thread does not have to interact with database and skip too many frames.
	 * 
	 */
	private class GetAllStories extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setContentView(R.layout.activity_intro_screen);
		}

		@Override
		protected synchronized Void doInBackground(Void... params) {
			if (viewType == Type.CACHED) {
				currentStories = storyMan.getAllCachedStories();
			} else if (viewType == Type.CREATED) {
				currentStories = storyMan.getAllAuthorStories();
			} else {
				currentStories = serverMan.getAll();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			refreshStories();
			setContentView(R.layout.activity_view_browse_stories);
			setGridView();
		}
	}

	/**
	 * Displays help guide for ViewBrowseStories
	 */
	private void getHelp() {
		Intent intent = new Intent(this, InfoActivity.class);
		String helpInfo = "\t- To view downloaded, published, or your stories, "
				+ "press the button at top right.\n\n"
				+ "\t- To begin reading or editing a story, "
				+ "simply click on one of the story icons.\n\n"
				+ "\t- To search for a story by title, "
				+ "press icon with magnifying glass.\n\n"
				+ "\t- To view a random story, press '?' icon\n\n"
				+ "\t- To add a new story, press '+' icon\n\n";
		intent.putExtra("theHelp", helpInfo);
		startActivity(intent);
	}
}
