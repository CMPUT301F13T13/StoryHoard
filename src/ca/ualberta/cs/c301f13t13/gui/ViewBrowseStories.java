package ca.ualberta.cs.c301f13t13.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.GeneralController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * 
 * View Browse Stories handles the grid view for showing all stories. There is a
 * spinner to differentiate between story views (My Stories, Published Stories,
 * Downloaded Stories).
 * 
 * @author Alexander Wong
 * 
 */
public class ViewBrowseStories extends Activity {

	private Spinner storyViewType;
	private GridView storyListGrid;
	private ArrayAdapter<Story> storyAdapter;
	private ArrayAdapter<CharSequence> viewTypeAdapter;
	private int viewType = GeneralController.CREATED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Setup the content view */
		setContentView(R.layout.activity_view_browse_stories);
		storyViewType = (Spinner) findViewById(R.id.storyViewType);
		storyListGrid = (GridView) findViewById(R.id.viewAllTypeStories);

		/* Setup the spinner adapter */
		viewTypeAdapter = ArrayAdapter
				.createFromResource(this, R.array.view_type_choices,
						android.R.layout.simple_spinner_item);
		viewTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		storyViewType.setAdapter(viewTypeAdapter);
		storyViewType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					/* My Stories */
					viewType = GeneralController.CREATED;
				} else if (arg2 == 1) {
					/* Downloaded Stories */
					viewType = GeneralController.CACHED;
				} else if (arg2 == 2) {
					/* Published Stories */
					viewType = GeneralController.PUBLISHED;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		/* Setup the GridView Adapter */
		GeneralController gc = new GeneralController();

		storyAdapter = new GridStoriesAdapter(this, R.layout.item_browse_story,
				null);

		// THIS NEEDS TO BE FIXED
		/*
		 * storyAdapter = new GridStoriesAdapter(this,
		 * R.layout.item_browse_story, gc.getAllStories(viewType, this));
		 */
		storyListGrid.setAdapter(storyAdapter);
		storyListGrid.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, 0, 0, R.string.add_new_story);
		menu.add(0, 1, 1, R.string.search_stories);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			/* Add new story activity */
			Intent intent = new Intent(this, AddStoryActivity.class);
			startActivity(intent);
			return true;
		case 1:
			/* Search Stories activity */
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This class is the subclasses ArrayAdapter<Story> and handles the custom
	 * view type within the grid
	 * 
	 * @author Alexander Wong
	 * 
	 */
	private class GridStoriesAdapter extends ArrayAdapter<Story> {

		private ArrayList<Story> items;
		private StoryTypeHolder holder;

		public GridStoriesAdapter(Context context, int resource,
				ArrayList<Story> passedItems) {
			super(context, resource);
			this.items = passedItems;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.item_browse_story, null);
				holder = new StoryTypeHolder();
				holder.storyCover = (ImageView) findViewById(R.id.storyCover);
				holder.storyTitle = (TextView) findViewById(R.id.storyTitle);
				v.setTag(holder);
			} else {
				holder = (StoryTypeHolder) v.getTag();
			}
			Story s = items.get(pos);
			if (s != null) {
				holder.storyCover.setImageBitmap(s.getImage());
				holder.storyTitle.setText(s.getTitle());
			}
			return v;
		}

		public class StoryTypeHolder {
			ImageView storyCover;
			TextView storyTitle;
		}

	}

}
