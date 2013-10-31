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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.Editable;
import android.text.TextWatcher;
//import android.view.Menu; *Not sure if needed
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.ChapterManager;
import ca.ualberta.cs.c301f13t13.backend.Story;
/**
 * Add Chapter Activity
 * 
 * Purpose:
 * 	- To add a chapter to an existing story.
 * 	- The author can:
 * 		- Add images through the use of the image button
 * 		- Set the text of the chapter through the Edit text space
 * 		- View all chapters in the story upon pressing the 'View All Chapters'
 * 		  button
 * 		- Add a choice by pressing the 'Add Choice' button.
 * 		- This activity will also display the choices that exist or have been
 * 		  added.
 * 
 * author: Josh Tate
 */

public class AddChapterActivity extends Activity 
		implements ca.ualberta.cs.c301f13t13.gui.SHView<ChapterManager> {
	
	private Chapter chapt;
	private Story story;
	private ImageButton imageButton;
	private Button saveButton;
	private Button allChaptersButton;
	private Button addChoiceButton;
	private EditText chapterContent;
	private ListView choices;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_chapter);
		
		//Get the story that chapter is being added to
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			story = (Story) bundle.getSerializable(Constants._ID);
			chapt = new Chapter(story.getId()," ");
		}
		
		imageButton = (ImageButton)findViewById(R.id.imageButton1);
		imageButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Change/Load images
				
			}
		});
		
		//Set the chapters text
		chapterContent = (EditText)findViewById(R.id.editText1);
		chapterContent.setText(chapt.getText());
		chapterContent.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(
					CharSequence c, int start, int end, int count) {
				chapt.setText(c.toString());
			}
			public void beforeTextChanged(
					CharSequence c, int start, int end, int count) {}
			public void afterTextChanged(Editable e) {}
		});
		
		saveButton = (Button)findViewById(R.id.save_story);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Save chapter/add chapter to story
				
			}
		});
		
		allChaptersButton = (Button)findViewById(R.id.add_chapter);
		allChaptersButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//View all chapters
				
			}
		});
		
		addChoiceButton = (Button)findViewById(R.id.add_choice_button);
		addChoiceButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Start add choice activity
				Intent intent = new Intent(getApplicationContext(),AddChoiceActivity.class);
				startActivity(intent);
				
			}
		});
		
		//Use choices to display choices
		choices = (ListView)findViewById(R.id.listView1);
		/* ArrayList<String> testArray = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			testArray.add("Test Choice "+(i+1));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,testArray);
		choices.setAdapter(adapter); */
		
		
	}
	
	@Override
	public void update(ChapterManager model) {
		// TODO Auto-generated method stub
		
	};
	
	/*  Not sure if needed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_chapter, menu);
		return true;
	}
	*/
}
