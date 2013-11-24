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

package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract.ChoiceTable;

/**
 * Role: Interacts with the database to store, update, and retrieve choice
 * objects. It implements the StoringManager interface.
 * 
 * </br>
 * Design Pattern: Singleton
 * 
 * @author Ashley Brown 
 * 
 * @see Choice
 * @see StoringManager
 *
 */

public class ChoiceManager implements StoringManager<Choice> {
	private static DBHelper helper = null;
	private static ChoiceManager self = null;

	/**
	 * Initializes a new ChoiceManager choice.
	 */
	protected ChoiceManager(Context context) {
		helper = DBHelper.getInstance(context);
	}

	/**
	 * Returns an instance of itself(ChoiceManager). Used to accomplish the
	 * singleton design pattern. 
	 *  
	 * @param context
	 * @return ChoiceManager
	 */
	public static ChoiceManager getInstance(Context context) {
		if (self == null) {
			self = new ChoiceManager(context);
		} 
		return self;			
	}

	/**
	 * Saves a new choice locally (in the database).
	 */	
	@Override
	public void insert(Choice choice) {
		SQLiteDatabase db = helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, choice.getId().toString());		
		values.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, choice.getCurrentChapter().toString());
		values.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, choice.getNextChapter().toString());
		values.put(ChoiceTable.COLUMN_NAME_TEXT, choice.getText());
		db.insert(ChoiceTable.TABLE_NAME, null, values);		
	}

	/**
	 * Updates a choice already in the database.
	 * 
	 * 
	 * @param newChoice
	 * 			Contains the changes to the choice.
	 */
	@Override
	public void update(Choice newChoice) {
		SQLiteDatabase db = helper.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, newChoice.getId().toString());		
		values.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, 
				newChoice.getCurrentChapter().toString());
		values.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, 
				newChoice.getNextChapter().toString());
		values.put(ChoiceTable.COLUMN_NAME_TEXT, newChoice.getText());

		// Setting search criteria
		String selection = ChoiceTable.COLUMN_NAME_CHOICE_ID + " LIKE ?";
		String[] sArgs = { newChoice.getId().toString()};	

		db.update(ChoiceTable.TABLE_NAME, values, selection, sArgs);	
	}

	/**
	 * Retrieves a choice from the database.
	 * 
	 * @param criteria 
	 * 			Holds the search criteria.
	 */
	@Override
	public ArrayList<Choice> retrieve(Choice criteria) {
		ArrayList<Choice> results = new ArrayList<Choice>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] sArgs = null;
		String[] projection = {
				ChoiceTable.COLUMN_NAME_CHOICE_ID,
				ChoiceTable.COLUMN_NAME_CURR_CHAPTER,
				ChoiceTable.COLUMN_NAME_NEXT_CHAPTER,
				ChoiceTable.COLUMN_NAME_TEXT
		};

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		String selection = setSearchCriteria(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}

		// Querying the database
		Cursor cursor = db.query(ChoiceTable.TABLE_NAME, projection, selection, 
				sArgs, null, null, null);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String choiceId = cursor.getString(0);

			Choice choice = new Choice(
					UUID.fromString(choiceId),
					UUID.fromString(cursor.getString(1)), 	// current chapter
					UUID.fromString(cursor.getString(2)), 	// next chapter
					cursor.getString(3) 	// text
					);
			results.add(choice);
			cursor.moveToNext();
		}
		cursor.close();		
		return results;
	}

	/**
	 * Creates the selection string (a prepared statement) to be used 
	 * in the database query. Also creates an array holding the items
	 * to be placed in the ? of the selection.
	 *  
	 * @param choice
	 * 			Holds the data needed to build the selection string 
	 * 			and the selection arguments array.
	 * @param sArgs
	 * 			Holds the arguments to be passed into the selection string.
	 * @return String
	 * 			The selection string.
	 */
	public String setSearchCriteria(Choice choice, ArrayList<String> sArgs) {
		HashMap<String,String> choiceCrit = choice.getSearchCriteria();		

		// Setting search criteria
		String selection = "";

		int counter = 0;
		int maxSize = choiceCrit.size();

		for (String key: choiceCrit.keySet()) {
			String value = choiceCrit.get(key);
			selection += key + " LIKE ?";
			sArgs.add(value);

			counter++;
			if (counter < maxSize) {
				selection += " AND ";
			}	
		}
		return selection;
	}

	@Override
	public void remove(UUID id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		// Delete entry 
		String selection = ChoiceTable.COLUMN_NAME_CHOICE_ID + " LIKE ?";
		String[] selectionArgs1 = { String.valueOf(id)};
		db.delete(ChoiceTable.TABLE_NAME, selection, selectionArgs1);
	}
	
	@Override
	public Boolean existsLocally(Choice choice) {
		Choice crit = new Choice(choice.getId(), null, null, null);
		ArrayList<Choice> choices = retrieve(crit);
		if (choices.size() != 1) {
			return false;
		}
		return true;		
	}

	public void syncChoice(Choice choice) {
		if (existsLocally(choice)) {
			update(choice);
		} else {
			insert(choice);
		}	
	}	
}
