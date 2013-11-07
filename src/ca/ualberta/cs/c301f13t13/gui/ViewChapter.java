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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.SHController;

/**
 * Views the chapter provided through the intent. Does not allow going backwards
 * through the activity stack.
 * 
 * @author Alexander Wong
 * 
 */
public class ViewChapter extends Activity {
	private Context context = this;
	private UUID storyID;
	private UUID chapterID;
	private SHController gc;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private AdapterChoices choiceAdapter;
	private AlertDialog photoDialog;

	private TextView chapterContent;
	private ListView chapterChoices;
	private Button addPhotoButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_chapter);

		// Grab the necessary UUIDs and GC
		Bundle bundle = this.getIntent().getExtras();
		storyID = (UUID) bundle.get("storyID");
		chapterID = (UUID) bundle.get("chapterID");
		gc = SHController.getInstance(this);

		// Setup the activity fields
		chapterContent = (TextView) findViewById(R.id.chapterContent);
		chapterChoices = (ListView) findViewById(R.id.chapterChoices);
		addPhotoButton = (Button) findViewById(R.id.addPhotoButton);

		// Setup the choices and choice adapters
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		chapterChoices.setAdapter(choiceAdapter);
		chapterChoices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Go to the chapter in question
				UUID nextChapter = choices.get(arg2).getNextChapter();
				Intent intent = new Intent(getBaseContext(), ViewChapter.class);
				intent.putExtra("storyID", storyID);
				intent.putExtra("chapterID", nextChapter);
				startActivity(intent);
				finish();
			}
		});
		
		addPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);

				// Set dialog title
				alert.setTitle("Choose method:");

				// Options that user may choose to add photo
				final String[] methods = { "Take Photo", "Choose from Gallery" };

				alert.setSingleChoiceItems(methods, -1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int item) {
								switch (item) {
								case 0:
									// Take a photo
									break;
								case 1:
									// Choose from gallery
									break;
								}
								photoDialog.dismiss();
							}
						});
				photoDialog = alert.create();
				photoDialog.show();
			}
		});
		
	}

	@Override
	public void onResume() {
		chapter = gc.getCompleteChapter(chapterID);
		choices.clear();
		// Check for no chapter text
		if (chapter.getText().equals("")) {
			chapterContent.setText("<No Chapter Content>");
		} else {
			chapterContent.setText(chapter.getText());
		}
		// Check for no choices
		if (chapter.getChoices().isEmpty()) {
			chapterContent.setText(chapterContent.getText() + "\n\n<No Choices>");
		} else {
			choices.addAll(chapter.getChoices());
		}
		choiceAdapter.notifyDataSetChanged();
		super.onResume();
	}
}
