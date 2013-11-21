/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Role: Provides the necessary methods for any class extending it to interact 
 * with the database to store, update, and retrieve story objects. 
 * 
 * </br>
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Story
 * @see StoryManager
 * @see CachedStoryManager
 */
public abstract class StoryManagerHelper {
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;
	
	/**
	 * Sets up the ContentValues for inserting or updating the database.
	 * @param object
	 */
	protected void setContentValues(Object object) {
		Story story = (Story) object;
		UUID chapterId = story.getFirstChapterId();

		// Insert story
		values = new ContentValues();
		values.put(DBContract.COLUMN_NAME_STORY_ID, 
				(story.getId()).toString());
		values.put(DBContract.COLUMN_NAME_TITLE, story.getTitle());
		values.put(DBContract.COLUMN_NAME_AUTHOR, story.getAuthor());
		values.put(DBContract.COLUMN_NAME_DESCRIPTION,
				story.getDescription());
		if (chapterId != null) {
			values.put(DBContract.COLUMN_NAME_FIRST_CHAPTER, 
				chapterId.toString());
		}
		values.put(DBContract.COLUMN_NAME_PHONE_ID, story.getPhoneId());
	}

	protected void setUpSearch(Object criteria) {
		sArgs = null;
		projection = new String[]{ 
				DBContract.COLUMN_NAME_STORY_ID,
				DBContract.COLUMN_NAME_TITLE, 
				DBContract.COLUMN_NAME_AUTHOR,
				DBContract.COLUMN_NAME_DESCRIPTION,
				DBContract.COLUMN_NAME_FIRST_CHAPTER,
				DBContract.COLUMN_NAME_PHONE_ID };

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		selection = setSearchCriteria(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}
	}
	
	protected ArrayList<Object> retrieveCursorEntries(Cursor cursor) {
		ArrayList<Object> results = new ArrayList<Object>();
		
		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String storyId = cursor.getString(0);

			Story story = new Story(
					storyId, 
					cursor.getString(1), // title
					cursor.getString(2), // author
					cursor.getString(3), // description
					cursor.getString(4), // first chapter id
					cursor.getString(5) // phoneId
					);
			results.add(story);
			cursor.moveToNext();
		}
		cursor.close();

		return results;		
	}
	
	/**
	 * Creates the selection string (a prepared statement) to be used in the
	 * database query. Also creates an array holding the items to be placed in
	 * the ? of the selection.
	 * 
	 * @param object
	 *            Holds the data needed to build the selection string and the
	 *            selection arguments array.
	 * @param sArgs
	 *            Holds the arguments to be passed into the selection string.
	 * @return String The selection string, i.e. the where clause that will be
	 *         used in the sql query.
	 */
	public String setSearchCriteria(Object object, ArrayList<String> sArgs) {
		Story story = (Story) object;
		HashMap<String, String> storyCrit = story.getSearchCriteria();

		// Setting search criteria
		String selection = "";

		int counter = 0;
		int maxSize = storyCrit.size();

		for (String key : storyCrit.keySet()) {
			String value = storyCrit.get(key);
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
	 * Inserts an object into the database.
	 * 
	 * @param object
	 */
	protected void insert(Object object,  String tableName, DBHelper helper) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(object);
		db.insert(tableName, null, values);
	}
	
	/**
	 * Retrieves an object(s) from the database.
	 * 
	 * @param criteria
	 * 
	 * @return objects
	 */
	protected ArrayList<Object> retrieve(Object criteria, String tableName, 
			DBHelper helper) {
		ArrayList<Object> results = new ArrayList<Object>();
		SQLiteDatabase db = helper.getReadableDatabase();
		setUpSearch(criteria);

		// Querying the database
		Cursor cursor = db.query(tableName, projection, selection,
				sArgs, null, null, null);

		// Retrieving all the entries
		results = retrieveCursorEntries(cursor);
		return results;
	}

	/**
	 * Updates an object in the database.
	 * 
	 * @param newObject
	 */
	protected void update(Object newObject, String tableName, DBHelper helper) {
		setContentValues(newObject);
		Story newS = (Story) newObject;
		selection = DBContract.COLUMN_NAME_STORY_ID + " LIKE ?";
		sArgs = new String[]{ newS.getId().toString() };
		SQLiteDatabase db = helper.getReadableDatabase();
		db.update(tableName, values, selection, 
				sArgs);
	}
}
