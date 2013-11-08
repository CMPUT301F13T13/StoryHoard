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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.ObjectType;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * Activity class for adding and editing a choice.
 * 
 * @author Alexander Wong
 *
 */
public class EditChoiceActivity extends Activity {

	private EditText choiceText;
	private ListView chapters;
	private AdapterChapters chapterAdapter;
	private ArrayList<Chapter> data = new ArrayList<Chapter>();
	private Story story;
	private Chapter fromChapter;
	private Chapter toChapter;
	private SHController gc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_choice);

		gc = SHController.getInstance(this);

		choiceText = (EditText) findViewById(R.id.choiceText);
		chapters = (ListView) findViewById(R.id.listAllLinkableChapters);
		chapterAdapter = new AdapterChapters(this,
				R.layout.browse_chapter_item, data);
		chapters.setAdapter(chapterAdapter);

		// Grab the data from the previous activity
		Bundle bundle = this.getIntent().getExtras();
		story = (Story) bundle.get("story");
		fromChapter = (Chapter) bundle.get("chapter");

		// Handle adding the choice into the chapter
		chapters.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				toChapter = data.get(arg2);
				String text = choiceText.getText().toString();
				Choice addedChoice = new Choice(fromChapter.getId(), toChapter
						.getId(), text);
				gc.addObject(addedChoice, ObjectType.CHOICE);
				finish();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		data.clear();
		data.addAll(gc.getAllChapters(story.getId()));
		chapterAdapter.notifyDataSetChanged();
	}
}
