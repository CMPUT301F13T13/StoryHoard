package ca.ualberta.cs.c301f13t13.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import ca.ualberta.cmput301f13t13.storyhoard.R;

/**
 * 
 * @author Alexander Wong
 * 
 */
public class ViewBrowseStories extends Activity {

	private Spinner storyViewType;
	private GridView storyListGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse_stories);
		storyViewType = (Spinner) findViewById(R.id.storyViewType);
		ArrayAdapter<CharSequence> viewTypeAdapter;
		storyListGrid = (GridView) findViewById(R.id.viewAllTypeStories);

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
	}

}
