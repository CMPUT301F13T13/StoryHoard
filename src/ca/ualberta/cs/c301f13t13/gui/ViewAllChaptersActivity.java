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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.SHController;

/**
 * ViewAllChaptersActivity
 * 
 * Purpose:
 * 	- To allow an author to either
 * 		- View all the chapters in a current story
 * 		- Select an existing chapter to add as a
 * 		  choice to another chapter
 * 
 * @author joshuatate
 *
 */
public class ViewAllChaptersActivity extends Activity {

	private ListView chapters;
	private Choice choice;
	private ArrayList<Chapter> listOfChapters;
	private SHController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_chapters);
		chapters = (ListView) findViewById(R.id.allChaptersView);
		
		//Get list of chapters from SHController
		listOfChapters = controller.getAllChapters(null);
		
		ArrayAdapter<Chapter> adapter = new ArrayAdapter<Chapter> (this,
				android.R.layout.simple_list_item_1,listOfChapters);
		
		chapters.setAdapter(adapter);
		
		Bundle bundle = this.getIntent().getExtras();
		if (bundle.getBoolean("viewing")) {
			//Just viewing chapters; not selecting for
			//any purpose
			chapters.setOnItemClickListener(null);
		} else {
			//Selecting a chapter to add as a choice
			choice = (Choice) bundle.get("Choice");
			chapters.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					//Set choice's next chapter upon selection
					choice.setNextChapter(null);
				}
			});
		}
	}

}
