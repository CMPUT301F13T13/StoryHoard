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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.LifecycleData;

/**
 * Takes a storyID bundle, displays all the chapters related to that story. Used
 * for editing chapters.
 * 
 * @author alexanderwwong
 * @author Kim Wu
 */

public class ViewBrowseChapters extends Activity {
	LifecycleData lifedata;
	private ChapterController chapCon;
	private Story story;
	private ListView storyChapters;
	private AdapterChapters chapterAdapter;
	private ArrayList<Chapter> data = new ArrayList<Chapter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			lifedata.setEditing(false);
			lifedata.setChapter(null);
			lifedata.setStory(story);
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
		new GetStoryChapters().execute();
	}
	
	/**
	 * Initialize private fields needed
	 */
	public void setUpFields() {
		lifedata = LifecycleData.getInstance();
		
		// Grab controllers and pull all chapters from story
		chapCon = ChapterController.getInstance(this);
		story = lifedata.getStory();
		
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
				lifedata.setEditing(true);
				lifedata.setStory(story);
				lifedata.setChapter(data.get(arg2));
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Async task to get all author's stories in the database. Used so main UI thread does
	 * not have to interact with database and skip too many frames.
	 *
	 */
	private class GetStoryChapters extends AsyncTask<Void, Void, Void>{
		@Override
		protected synchronized Void doInBackground(Void... params) {
			// get all story chapters
			data.addAll(chapCon.getChaptersByStory(story.getId()));
			return null;
		}
		
		@Override 
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			chapterAdapter.notifyDataSetChanged();
		}
	}	
}
