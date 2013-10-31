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

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;

/**
 * 
 * View Author Stories handles displays a ListActivity of all the names of the
 * stories and their authors.
 * 
 * @author Kim Wu
 * 
 */

public class ViewBrowseAuthorStories extends ListActivity {

	TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_author_stories);

		content = (TextView) findViewById(R.id.output);

		// SHOULD DISPLAY STORY AND BY AUTHOR;
		String[] values = new String[] { "Android Example ListActivity",
				"Adapter implementation", "Simple List View With ListActivity",
				"ListActivity Android", "Android Example",
				"ListActivity Source Code",
				"ListView ListActivity Array Adapter",
				"Android Example ListActivity" };

		// Define a new Adapter
		// First parameter - Context
		// Second parameter - Layout for the row
		// Third - the Array of data

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, values);

		// Assign adapter to List
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		// ListView Clicked item index
		int itemPosition = position;

		// ListView Clicked item value
		String itemValue = (String) l.getItemAtPosition(position);

		content.setText("Click : \n  Position :" + itemPosition
				+ "  \n  ListItem : " + itemValue);

	}
}