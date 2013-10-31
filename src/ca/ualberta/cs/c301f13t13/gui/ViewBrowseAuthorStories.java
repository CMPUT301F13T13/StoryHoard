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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_author_stories);

		// Populates list with stories and displays author
		String[] stories = new String[] { "Story1 \nby:asd", "Stor2\nby:mandy",
				"Story3 \nby:Tom", "Story4 \nby:Dan", "Story5 \nby:Sue" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, stories);
		setListAdapter(adapter);
	}

	// When user clicks a story from the list:
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Dialog dialog = userAction(position, id);
		dialog.show();
	}

	/**
	 * userAction Choices: 3 possible actions can be taking from clicking a
	 * story on the list. 1) DELETE: will remove the note from the database and
	 * update the current list 2) EDIT: will take user to the note taking
	 * display and edit the note 3) CANCEL: will take users back to the ViewList
	 * display
	 * 
	 * @return
	 */
	private AlertDialog userAction(final int position, final long id) {
		// Initialize the Alert Dialog
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Source of the data in the DIalog
		CharSequence[] array = { "Settings", "Edit", "Read" };

		// Set the dialog title
		builder.setTitle("Select Priority")
				// Specify the list array, the items to be selected by default
				// (null for none),
				// and the listener through which to receive callbacks when
				// items are selected
				.setSingleChoiceItems(array, 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						})

				// Set the action buttons
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK, so save the result somewhere
						// or return them to the component that opened the
						// dialog

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		return builder.create();
	}
}