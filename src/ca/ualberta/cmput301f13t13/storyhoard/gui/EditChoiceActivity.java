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
import java.util.UUID;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;

/**
 * Activity class for adding and editing a choice.
 * 
 * @author Alexander Wong
 * 
 */
public class EditChoiceActivity extends Activity {
	LifecycleData lifedata;
	private EditText choiceText;
	private ListView chapters;
	private AdapterChapters chapterAdapter;
	private ArrayList<Chapter> data = new ArrayList<Chapter>();
	private Story story;
	private Chapter fromChapter;
	private Chapter toChapter;
	private ChapterController chapCon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lifedata = LifecycleData.getInstance();
		setContentView(R.layout.activity_edit_choice);
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpFields();
		setAddChoiceListener();

		data.clear();
		UUID mystory = story.getId();
		data.addAll(chapCon.getChaptersByStory(mystory));
		chapterAdapter.notifyDataSetChanged();
	}

	/**
	 * Initializes the private fields needed
	 */
	public void setUpFields() {
		chapCon = ChapterController.getInstance(this);
		
		story = lifedata.getStory();
		fromChapter = lifedata.getChapter();
		// Set up activity fields
		choiceText = (EditText) findViewById(R.id.choiceText);
		chapters = (ListView) findViewById(R.id.listAllLinkableChapters);

		// Set up adapter
		chapterAdapter = new AdapterChapters(this,
				R.layout.browse_chapter_item, data);
		chapters.setAdapter(chapterAdapter);
	}

	/**
	 * Set onClick listener for adding choice
	 */
	public void setAddChoiceListener() {
		// Handle adding the choice into the chapter
		chapters.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				toChapter = data.get(arg2);
				String text = choiceText.getText().toString();
				Choice addedChoice = new Choice(fromChapter.getId(), toChapter
						.getId(), text);
				lifedata.addToCurrChoices(addedChoice);
				finish();
			}
		});
	}
}
