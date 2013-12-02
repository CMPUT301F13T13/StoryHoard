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
 * </br></br>
 * The setup of the database being used is defined in DBContract.java, so for 
 * more information on the actual tables and SQL statements used to make them, 
 * see that class.
 * </br></br>
 * Design Pattern: This class is a singleton, so there will ever only be one 
 * instance of it. Use the getInstance() static method to retrieve an  
 * instance of it, not the constructor.
 * Design Pattern: Singleton
 * </br></br>
 * @author Ashley Brown 
 * 
 * @see Choice
 * @see StoringManager
 * @see DBContract
 */

public class ChoiceManager extends StoringManager<Choice> {
	private static DBHelper helper = null;
	private static ChoiceManager self = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;	

	/**
	 * Initializes a new ChoiceManager class. Must be given context in order to  
	 * create a new instance of DBHelper and also to get the phoneId of 
	 * whichever phone is using this application.</br></br>
	 * 
	 * Note that this constructor is protected, and it should never be used  
	 * outside of this class (except for any class that subclass it). 
	 * 
	 * @param context
	 * 
	 */
	protected ChoiceManager(Context context) {
		helper = DBHelper.getInstance(context);
	}

	/**
	 * Returns an instance of a ChoiceManager. Since this class is a singleton,  
	 * the same instance will always be returned. This is the method any class 
	 * outside of this one and any subclasses should use to get an choiceManager 
	 * object. </br></br>
	 * 
	 * Used to implement the singleton
	 * design pattern.
	 * 
	 * @param context
	 */
	public static ChoiceManager getInstance(Context context) {
		if (self == null) {
			self = new ChoiceManager(context);
		} 
		return self;			
	}

	/**
	 * Saves a new choice to the database.</br></br>
	 * 
	 * Example Call.</br>
	 * Choice c = new choice("123ad4", "123de", 
	 * 				"d342a", "text");</br>
	 * ChoiceManager chm = ChoiceManager.getInstance(someActivity.this);</br>
	 * chm.insert(c);</br>
	 * 
	 * @param Choice
	 * 			A choice object. 
	 */
	@Override
	public void insert(Choice choice) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(choice);
		db.insert(ChoiceTable.TABLE_NAME, null, values);		
	}
	/**
	 * Sets up the ContentValues for inserting or updating the database. This 
	 * specifies the columns to be inserted into and what content will be  
	 * going into those columns. 
	 * 
	 * @param choice
	 * 			All the choice's fields will be put into the database.
	 */

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
	 * Example Call.</br>
	 * Choice c = new choice("123ad4", "123de", 
	 * 				"d342a", "text");</br>
	 * ChoiceManager chm = ChoiceManager.getInstance(someActivity.this);</br>
	 * chm.insert(c);</br>
	 * c.setTitle("new");</br>
	 * chm.update(c);</br>
	 * 
	 * @param newChoice
	 * 			choice with changes.
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
	 * Retrieves a choice from the database.</br><br> 
	 * 
	 * The choice passed into this method is a story holding search criteria, 
	 * so any field you would like to include in the search, just set the  
	 * search criteria holding choice to it.</br><br>
	 * 
	 *  * Example Call.</br>
	 * Choice c = new choice("123ad4", "123de", 
	 * 				"d342a", "text");</br>
	 * ChoiceManager chm = ChoiceManager.getInstance(someActivity.this);</br>
	 * chm.insert(c);</br>
	 * 
	 * To now search for this choice based on its tect: </br></br>
	 * 
	 * Choice criteria = new choice(null, null, null, "text");</br>
	
	 * @param choice
	 * 			Choice with the search criteria 
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
	/**
	 * A helper function to set up what table columns and rows to be searched 
	 * or retrieved. Basically, building the sql query, but using content 
	 * values to abstract the sql.
	 * 
	 * @param criteria
	 */
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
	/**
	 * Removes the choice from the chapter
	 * 
	 * @param choiceID
	 *            Id of  the choices that the choice is for.
	 */
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
	 * @param chapterId 
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
	/**
	 * Retrieves the choice whose id matches the id provided. It expects the id 
	 * provided to be a UUID. 
	 * 
	 * Example call:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * ChoiceManager chm = ChoiceManager.getInstance(someActivity.this);</br>
	 * Choice ch = ChoiceManager.getById(id);</br>
	 * 
	 * @param id
	 * 			Id of the choice we are looking for. Must be a UUID. 
	 */	
	@Override
	public Choice getById(UUID id) {
		ArrayList<Choice> result = retrieve(new Choice(id, null, null, null));
		if (result.size() != 1) {
			return null;
		}
		return result.get(0);
	}	
}
