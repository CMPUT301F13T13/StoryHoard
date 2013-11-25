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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.LifecycleData;

/**
 * Search Results activity
 * 
 * Displays a list of search results with the parameters given by the user
 * 
 * @author Kim Wu
 * 
 */
public class SearchResultsActivity extends Activity {
	private LifecycleData lifedata;
	private GridView gridView;
	private ArrayList<Story> gridArray = new ArrayList<Story>();
	private AdapterStories customGridAdapter;
	private TextView emptyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_search_results);	
	}

	@Override 
	protected void onResume() {
		super.onResume();

		lifedata = LifecycleData.getInstance();
		emptyList = (TextView) findViewById(R.id.empty);

		ArrayList<Story> newStories = lifedata.getStoryList();

		if (newStories == null || newStories.size() == 0) {
			Toast.makeText(getBaseContext(),
					"No stories matched your search.", Toast.LENGTH_LONG)
					.show();	
		} 
		
		gridArray.clear();
		gridArray.addAll(newStories);
		emptyList.setText(" ");
		// Setup the grid view for the stories
		gridView = (GridView) findViewById(R.id.gridStoriesView);
		customGridAdapter = new AdapterStories(this,
				R.layout.activity_view_browse_story, gridArray);
		gridView.setAdapter(customGridAdapter);

		// Setup the grid view click listener
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Handle going to view story activity
				lifedata = LifecycleData.getInstance();
				Story story = gridArray.get(arg2);
				lifedata.setStory(story);

				Intent intent = new Intent(getBaseContext(), ViewStory.class);
				startActivity(intent);
			}
		});	
		customGridAdapter.notifyDataSetChanged();			
	}

	// MENU INFORMATION
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.browse_stories:
			Intent browse = new Intent(this, ViewBrowseStories.class);
			startActivity(browse);
			return true;
		case R.id.add_story:
			Intent add = new Intent(this, EditStoryActivity.class);
			lifedata.setEditing(false);
			startActivity(add);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
