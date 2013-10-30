package ca.ualberta.cs.c301f13t13.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * 
 * @author Alexander Wong
 * 
 */
public class ViewBrowseStories extends Activity {

	private Spinner storyViewType;
	private GridView storyListGrid;
	private ArrayAdapter<Story> storyAdapter;
	private ArrayAdapter<CharSequence> viewTypeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				// TODO Auto-generated method stub

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		/* Setup the GridView Adapter */
		storyAdapter = new GridStoriesAdapter(this, R.layout.item_browse_story, null);
		// CHANGE THIS NULL TO THE LIST OF STORIES 
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
