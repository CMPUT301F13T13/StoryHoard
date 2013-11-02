package ca.ualberta.cs.c301f13t13.gui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
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
	CustomGridViewAdapter customGridAdapter;

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
						Log.w("StoryHoard ViewType", "" + itemPosition);
						return false;
					}
				});
		// Testing to see if the stories actually work
		gridArray.add(new Story("My Bad Story", "I should test this better",
				"Wow, such story", true));

		// Setup the grid view for the stories
		gridView = (GridView) findViewById(R.id.gridStoriesView);
		customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid,
				gridArray);
		gridView.setAdapter(customGridAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_browse_stories, menu);
		return true;
	}

}
