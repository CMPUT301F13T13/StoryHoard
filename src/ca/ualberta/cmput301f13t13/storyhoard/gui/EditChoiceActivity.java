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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;

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
	private ChapterController chapCon;
	private StoryController storyCon;
	private ChoiceController choiceCon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_choice);
	}

	@Override
	public void onResume() {
		super.onResume();
		lifedata = LifecycleData.getInstance();
		setUpFields();
		setAddChoiceListener();

		data.clear();
		data.addAll(storyCon.getCurrStory().getChapters());
		chapterAdapter.notifyDataSetChanged();
	}

	/**
	 * Initializes the private fields needed
	 */
	public void setUpFields() {
		chapCon = ChapterController.getInstance(this);
		storyCon = StoryController.getInstance(this);
		choiceCon = ChoiceController.getInstance(this);

		// Set up activity fields
		choiceText = (EditText) findViewById(R.id.choiceText);
		chapters = (ListView) findViewById(R.id.listAllLinkableChapters);

		if (lifedata.isEditingChoice()) {
			choiceText.setText(choiceCon.getCurrChoice().getText());
		}

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
				Chapter toChapter = data.get(arg2);
				String text = choiceText.getText().toString();
				if (lifedata.isEditingChoice()) {
					choiceCon.editText(text);
					choiceCon.editChapterTo(toChapter.getId());
					chapCon.updateChoice(choiceCon.getCurrChoice());
				} else {
					Choice addedChoice = new Choice(chapCon.getCurrChapter().getId(),
							toChapter.getId(), text);
					chapCon.addChoice(addedChoice);
				}
				finish();
			}
		});
	}
}
