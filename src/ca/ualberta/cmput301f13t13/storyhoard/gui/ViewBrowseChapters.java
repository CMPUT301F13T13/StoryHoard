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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;

/**
 * When a specific story exists, the activity gets the chapters associated
 * with the story and displays them for the user. 
 * This activity allows a user to add a new chapter to the current story,
 * as well as edit the chapters in the current story.
 * 
 * @author Alexander Wong
 * @author Kim Wu
 */

public class ViewBrowseChapters extends Activity {
	LifecycleData lifedata;
	private ChapterController chapCon;
	private StoryController storyCon;
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
			startActivity(intent);
			return true;
		case R.id.info:
			getHelp();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
		setOnItemClickListener();
	}
	
	/**
	 * Initialize private fields needed
	 */
	public void setUpFields() {
		lifedata = LifecycleData.getInstance();
		
		// Grab controllers and pull all chapters from story
		chapCon = ChapterController.getInstance(this);
		storyCon = StoryController.getInstance(this);
		
		// Set up activity field
		storyChapters = (ListView) findViewById(R.id.storyChapters);
		
		// Set adapter
		chapterAdapter = new AdapterChapters(this,
				R.layout.browse_chapter_item, data);
		storyChapters.setAdapter(chapterAdapter);
		
		data.clear();
		data.addAll(storyCon.getCurrStory().getChapters());
		chapterAdapter.notifyDataSetChanged();
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
				chapCon.setCurrChapter(data.get(arg2).getId());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * Displays the help guide for the View Browse Chapters activity
	 */
	private void getHelp(){
		Intent intent = new Intent(this, InfoActivity.class);
		String helpInfo = "\t- All chapters composing the specific "+
				"story are contained in this screen.\n\n"+
				"\t- To add a new chapter, click on the 'New Chapter' "+
				"icon on the top right corner of the screen.\n\n"+
				"\t- To edit an existing chapter, click on the chapter "+
				"specified in the shown list of chapters.\n\n"+
				"\t- To return to the story screen, press the back button "+
				"on your mobile device.\n";
		intent.putExtra("theHelp", helpInfo);
		startActivity(intent);
	}
}
