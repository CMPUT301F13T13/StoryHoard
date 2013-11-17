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
package ca.ualberta.cs.c301f13t13.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.HolderApplication;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * Takes a storyID bundle, displays all the chapters related to that story. Used
 * for editing chapters.
 * 
 * @author alexanderwwong
 */

public class ViewBrowseChapters extends Activity {
	HolderApplication app;
	private SHController gc;
	private Story story;
	private ListView storyChapters;
	private AdapterChapters chapterAdapter;
	private ArrayList<Chapter> data = new ArrayList<Chapter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (HolderApplication) this.getApplication();
		setContentView(R.layout.activity_view_browse_chapters);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_browse_chapters, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.addNewChapter:
			// Go to the add new chapter page for the specified story
			Intent intent = new Intent(getBaseContext(),
					EditChapterActivity.class);
			app.setEditing(false);
			app.setStory(story);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
		setOnItemClickListener();
		data.clear();
		data.addAll(gc.getAllChapters(story.getId()));
		chapterAdapter.notifyDataSetChanged();
	}

	/**
	 * Initialize private fields needed
	 */
	public void setUpFields() {
		// Grab GC and pull all chapters from story
		gc = SHController.getInstance(this);
		story = app.getStory();
		// Set up activity field
		storyChapters = (ListView) findViewById(R.id.storyChapters);
		// Set adapter
		chapterAdapter = new AdapterChapters(this,
				R.layout.browse_chapter_item, data);
		storyChapters.setAdapter(chapterAdapter);
	}

	/**
	 * set chapter list OnItemClickListener
	 */
	public void setOnItemClickListener() {
		storyChapters.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Go to edit that chapter
				Intent intent = new Intent(getBaseContext(),
						EditChapterActivity.class);
				app.setEditing(true);
				app.setStory(story);
				app.setChapter(data.get(arg2));
				startActivity(intent);
			}
		});
	}
}
