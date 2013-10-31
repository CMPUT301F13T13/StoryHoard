package ca.ualberta.cs.c301f13t13.gui;
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


/**
 * 
 * ViewEditMode:
 * 
 * The ViewEditMode activity displays a scrolling list of all stories
 * created by the user. Information displayed includes the Title of the story
 * and the author(s) which wrote them.
 * 
 * Upon clicking a story, the user has the option to gain access of the
 * following: a) Settings: Update settings such as story information b) Edit:
 * Add/Edit chapters in the story c) Read: Read the story. Cancel will take the
 * user back to the ViewBrowseAuthorStories activity.
 * 
 * @author Kim Wu
 * 
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import ca.ualberta.cmput301f13t13.storyhoard.R;

public class ViewEditMode extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_edit_mode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
