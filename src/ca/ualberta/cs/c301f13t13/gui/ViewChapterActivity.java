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
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.GeneralController;

/**
 * Views the chapter provided through the intent. Does not allow going backwards
 * through the activity stack.
 * 
 * @author Alexander Wong
 * 
 */
public class ViewChapterActivity extends Activity {
	private UUID storyID;
	private UUID chapterID;
	private GeneralController gc;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private ArrayAdapter<Choice> choiceAdapter;

	private TextView chapterContent;
	private ListView chapterChoices;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_chapter);

		// Grab the necessary UUIDs and GC
		Bundle bundle = this.getIntent().getExtras();
		storyID = (UUID) bundle.get("storyID");
		chapterID = (UUID) bundle.get("chapterID");
		gc = GeneralController.getInstance();

		// Setup the activity fields
		chapterContent = (TextView) findViewById(R.id.chapterContent);
		chapterChoices = (ListView) findViewById(R.id.chapterChoices);

		// Setup the choices and choice adapters
		choiceAdapter = new ArrayAdapter<Choice>(this, android.R.id.text1,
				choices);
		chapterChoices.setAdapter(choiceAdapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		chapter = gc.getCompleteChapter(chapterID, this);
		choices.clear();
		chapterContent.setText(chapter.getText());
		choices.addAll(chapter.getChoices());
		choiceAdapter.notifyDataSetChanged();
	}
}
