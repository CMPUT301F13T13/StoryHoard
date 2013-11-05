package ca.ualberta.cs.c301f13t13.gui;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Story;

public class ViewBrowseChapters extends Activity {

	private SHController gc;
	private UUID storyID;
	private ListView storyChapters;
	private ChaptersViewAdapter chapterAdapter;
	private ArrayList<Chapter> data = new ArrayList<Chapter>();
	
	/**
	 * Takes a storyID bundle, displays all the chapters related to that story.
	 * Used for editing chapters.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse_chapters);
		
		gc = SHController.getInstance(getBaseContext());
		storyChapters = (ListView) findViewById(R.id.storyChapters);
		
		// Grab the story ID, pull all the available chapters
		Bundle bundle = this.getIntent().getExtras();
		storyID = (UUID) bundle.get("storyID");
		
		// Setup the choices and choice adapters
		chapterAdapter = new ChaptersViewAdapter(this, R.layout.browse_chapter_item, data);
		storyChapters.setAdapter(chapterAdapter);
		storyChapters.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Go to edit that chapter
				Chapter chapter = data.get(arg2);
				UUID storyID = data.get(arg2).getStoryId();
				Story story = gc.getCompleteStory(storyID);
				Intent intent = new Intent(getBaseContext(), EditChapterActivity.class);
				intent.putExtra("isEditing", true);
				intent.putExtra("Story", story);
				intent.putExtra("Chapter", chapter);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_browse_chapters, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		data.clear();
		data.addAll(gc.getAllChapters(storyID));
		chapterAdapter.notifyDataSetChanged();
		
	}

}
