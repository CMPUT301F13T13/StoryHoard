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
import java.util.Random;
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

public class ChoiceManager extends StoringManager<Choice> {
	private static DBHelper helper = null;
	private static ChoiceManager self = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;	

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
		setContentValues(choice);
		db.insert(ChoiceTable.TABLE_NAME, null, values);		
	}

	private void setContentValues(Choice choice) {
		values = new ContentValues();
		values.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, choice.getId().toString());		
		values.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, choice.getCurrentChapter().toString());
		values.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, choice.getNextChapter().toString());
		values.put(ChoiceTable.COLUMN_NAME_TEXT, choice.getText());		
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
		setContentValues(newChoice);

		// Setting search criteria
		selection = ChoiceTable.COLUMN_NAME_CHOICE_ID + " LIKE ?";
		sArgs = new String[]{ newChoice.getId().toString()};	
		
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
		
		setupSearch(criteria);
		
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

	private void setupSearch(Choice criteria) {
		sArgs = null;
		projection = new String[]{
				ChoiceTable.COLUMN_NAME_CHOICE_ID,
				ChoiceTable.COLUMN_NAME_CURR_CHAPTER,
				ChoiceTable.COLUMN_NAME_NEXT_CHAPTER,
				ChoiceTable.COLUMN_NAME_TEXT
		};

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		selection = buildSelectionString(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}
		
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
	private String buildSelectionString(Choice choice, ArrayList<String> sArgs) {
		HashMap<String,String> choiceCrit = getSearchCriteria(choice);		

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

	/**
	 * Returns the information of the choice (id, chapterIdFrom, chapterIdTo)
	 * that could be used in searching for a choice in the database. This
	 * information is returned in a HashMap where the keys are the 
	 * corresponding Choice Table column names.
	 * 
	 * @return HashMap
	 */
	private HashMap<String, String> getSearchCriteria(Choice choice) {
		HashMap<String, String> info = new HashMap<String, String>();

		if (choice.getId() != null) {
			info.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, choice.getId().toString());
		}

		if (choice.getCurrentChapter() != null) {
			info.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER,
					choice.getCurrentChapter().toString());
		}
		
		if (choice.getNextChapter() != null) {
			info.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER,
					choice.getNextChapter().toString());
		}

		return info;
	}
	
	@Override
	public void remove(UUID id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		selection = ChoiceTable.COLUMN_NAME_CHOICE_ID + " LIKE ?";
		sArgs = new String[]{ String.valueOf(id)};
		db.delete(ChoiceTable.TABLE_NAME, selection, sArgs);
	}

	public ArrayList<Choice> getChoicesByChapter(UUID chapterId) {
		return retrieve(new Choice(null, chapterId, null, null));		
	}

	/**
	 * Retrieves a random choice from the choice.
	 * 
	 * @param choiceID
	 *            Id of  the choices that the choice is for.
	 * 
	 * @param chapterId TODO
	 * @return a choice
	 */
	public Choice getRandomChoice(UUID chapterId) {
		ArrayList<Choice> choices = getChoicesByChapter(chapterId);
		
		if (choices.size() == 0) {
			return null;
		}
		Random rand = new Random(); 
		int num = rand.nextInt(choices.size());
		Choice choice = choices.get(num);
		choice.setText("I'm feeling lucky...");
	
		return choice;
	}

	@Override
	public Choice getById(UUID id) {
		ArrayList<Choice> result = retrieve(new Choice(id, null, null, null));
		if (result.size() != 1) {
			return null;
		}
		return result.get(0);
	}	
}
