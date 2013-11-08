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
import java.util.UUID;

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
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * Takes a storyID bundle, displays all the chapters related to that story.
 * Used for editing chapters.
 * 
 * @author alexanderwwong
 */

public class ViewBrowseChapters extends Activity {

	private SHController gc;
	private UUID storyID;
	private Story story;
	private ListView storyChapters;
	private AdapterChapters chapterAdapter;
	private ArrayList<Chapter> data = new ArrayList<Chapter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_browse_chapters);

		gc = SHController.getInstance(getBaseContext());
		storyChapters = (ListView) findViewById(R.id.storyChapters);

		// Grab the story, pull all the available chapters
		Bundle bundle = this.getIntent().getExtras();
		storyID = (UUID) bundle.get("storyID");
		story = gc.getCompleteStory(storyID);

		// Setup the choices and choice adapters
		chapterAdapter = new AdapterChapters(this,
				R.layout.browse_chapter_item, data);
		storyChapters.setAdapter(chapterAdapter);
		storyChapters.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Go to edit that chapter
				Chapter chapter = data.get(arg2);
				Intent intent = new Intent(getBaseContext(),
						EditChapterActivity.class);
				intent.putExtra("isEditing", true);
				intent.putExtra("addingNewChapt", false);
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
		case R.id.addNewChapter:
			// Go to the add new chapter page for the specified story
			Intent intent = new Intent(getBaseContext(),
					EditChapterActivity.class);
			intent.putExtra("isEditing", false);
			intent.putExtra("addingNewChapt", true);
			intent.putExtra("Story", story);
			intent.putExtra("Chapter", new Chapter(storyID, null));
			startActivity(intent);
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
