package ca.ualberta.cs.c301f13t13.gui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.GeneralController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * Class which displays all stories in a grid, handles different view types.
 * 
 * @author alexanderwong
 * 
 */
public class ViewBrowseStories extends Activity {

	GridView gridView;
	ArrayList<Story> gridArray = new ArrayList<Story>();
	StoriesViewAdapter customGridAdapter;
	GeneralController gc;
	int viewType = GeneralController.CREATED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse_stories);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("StoryHoard");
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_list_item_1,
				android.R.id.text1,
				new String[] {
						getString(R.string.title_viewBrowseStories_MyStories),
						getString(R.string.title_viewBrowseStories_CachedStories),
						getString(R.string.title_viewBrowseStories_PublishedStories), });
		actionBar.setListNavigationCallbacks(adapter,
				new OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition,
							long itemId) {
						if (itemPosition == 0) {
							viewType = GeneralController.CREATED;
						} else if (itemPosition == 1) {
							viewType = GeneralController.CACHED;
						} else if (itemPosition == 2) {
							viewType = GeneralController.PUBLISHED;
						}
						Log.w("StoryHoard ViewType", "" + itemPosition);
						refreshStories();
						return true;
					}
				});

		// Setup the grid view for the stories
		gridView = (GridView) findViewById(R.id.gridStoriesView);
		customGridAdapter = new StoriesViewAdapter(this, R.layout.row_grid,
				gridArray);
		gridView.setAdapter(customGridAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_browse_stories, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		Intent intent;
		switch (item.getItemId()) {
		case R.id.addNewStory:
			intent = new Intent(this, AddStoryActivity.class);
			startActivity(intent);
			return true;
		case R.id.searchStories:
			intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshStories() {
		gridArray.clear();
		gc = GeneralController.getInstance();
		// Implement this when ready
		// gridArray = gc.getAllStories(viewType, this);
		// Testing to see if the stories actually work
		if (viewType == GeneralController.CREATED) {
			gridArray.add(new Story("My Bad Story",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("My Bad Story The Sequel- Rawr",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("My Goodness Hi Santa",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("RAWR TESTING BADLY", "", "", true));
		}
		if (viewType == GeneralController.PUBLISHED) {
			gridArray.add(new Story("Life and Times of Chrono",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("Android: To do or not to do",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("Purple MacBook Camera Sticky",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("DIS SO BAD", "", "", true));

		}
		if (viewType == GeneralController.CACHED) {
			gridArray.add(new Story("Masterpiece: The Master Piece",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("Hot Dogs and Cream Cheese",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("My God, Santa Was Mean This Year",
					"I should test this better", "Wow, such story", true));
			gridArray.add(new Story("HUUUURRRRRRR", "", "", true));

		}
		customGridAdapter.notifyDataSetChanged();
	}
}
