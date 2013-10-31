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

package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ca.ualberta.cs.c301f13t13.backend.DBContract.ChapterTable;
import ca.ualberta.cs.c301f13t13.backend.DBContract.StoryTable;
import ca.ualberta.cs.c301f13t13.gui.SHView;

/**
 * Design Pattern: Singleton
 * 
 * @author Steph 
 *
 */
public class ChapterManager extends Model<SHView> implements StoringManager{
	private Context context;
	private static ChapterManager self = null;

	/**
	 * Initializes a new ChapterManager object.
	 * 
	 * @param context
	 */
	protected ChapterManager(Context context) {
		this.context = context;
	}

	/**
	 * Returns an instance of itself. Used to accomplish the
	 * singleton design pattern.
	 * 
	 * @param context
	 * @return ChapterManager
	 */
	public static ChapterManager getInstance(Context context) {
		if (self == null) {
			self = new ChapterManager(context);
		} 
		return self;
	}

	/**
	 * Inserts a new chapter into the database.
	 * 
	 * @param object 
	 * 			Object to be stored in the database.
	 * @param helper
	 * 			Used to get the database.
	 */			
	@Override
	public void insert(Object object, DBHelper helper) {
		Chapter chapter = (Chapter) object;
		SQLiteDatabase db = helper.getWritableDatabase();

		// Insert chapter
		ContentValues values = new ContentValues();
		values.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, (chapter.getId()).toString());		
		values.put(ChapterTable.COLUMN_NAME_STORY_ID, chapter.getStoryId().toString());
		values.put(ChapterTable.COLUMN_NAME_TEXT, chapter.getText());

		db.insert(ChapterTable.TABLE_NAME, null, values);		
	}

	/**
	 * Retrieves a chapter / chapters from the database.
	 * 
	 * @param criteria 
	 * 			Criteria for the object(s) to be retrieved from the database.
	 * @param helper
	 * 			Used to get the database.
	 * @return ArrayList
	 * 			Contains the objects that matched the search criteria.
	 */	
	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		ArrayList<Object> results = new ArrayList<Object>();
		String[] sArgs = null;
		ArrayList<String> selectionArgs = new ArrayList<String>();

		SQLiteDatabase db = helper.getReadableDatabase();

		String[] projection = {
				ChapterTable.COLUMN_NAME_CHAPTER_ID,
				ChapterTable.COLUMN_NAME_STORY_ID,
				ChapterTable.COLUMN_NAME_TEXT
		};

		// Setting search criteria
		String selection = setSearchCriteria(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			sArgs = null;
			selection = null;
		}

		// Querying the database
		Cursor cursor = db.query(ChapterTable.TABLE_NAME, projection, 
				selection, sArgs, null, null, null);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String storyId = cursor.getString(1);

			/*
			 * GET ALL CHOICES BELONGING TO THIS CHAPTER WITH THE
			 * CHOICEMANAGER CLASS
			 *
			ChoiceManager cm = new ChoiceManager(context);
			Choice choice = new Choice(chapter.getId());
			ArrayList<Object> choiceObjs = cm.retrieve(choice, helper);

			 */
			Chapter newChapter = new Chapter(
					UUID.fromString(cursor.getString(0)), // chapter id
					UUID.fromString(storyId), // story id
					cursor.getString(2) // text
					);
			// newChapter.setChoices(Choices)
			results.add(newChapter);
			cursor.moveToNext();
		}
		cursor.close();		
		return results;		
	}

	/**
	 * Updates a chapter's data in the database.
	 * 
	 * @param oldObject
	 * 			Object we want to update.
	 * @param newObject
	 * 			Holds the new data that will be used to update.
	 * @param helper
	 * 			DB Helper used to get the database.
	 */
	@Override
	public void update(Object newObject, DBHelper helper) {
		Chapter newC = (Chapter) newObject;
		SQLiteDatabase db = helper.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, newC.getId().toString());
		values.put(ChapterTable.COLUMN_NAME_STORY_ID, newC.getStoryId().toString());
		values.put(ChapterTable.COLUMN_NAME_TEXT, newC.getText());

		String selection = ChapterTable.COLUMN_NAME_CHAPTER_ID + " LIKE ?";
		String[] sArgs = { newC.getId().toString()};	

		db.update(ChapterTable.TABLE_NAME, values, selection, sArgs);	
	}

	/**
	 * Creates the selection string (a prepared statement) to be used 
	 * in the database query. Also creates an array holding the items
	 * to be placed in the ? of the selection (the where clause).
	 *  
	 * @param object
	 * 			Holds the data needed to build the selection string 
	 * 			and the selection arguments array.
	 * @param sArgs
	 * 			Holds the arguments to be passed into the selection string.
	 * @return String
	 * 			The selection string.
	 */
	@Override
	public String setSearchCriteria(Object object, ArrayList<String> sArgs) {
		Chapter chapter = (Chapter) object;
		HashMap<String,String> chapCrit = chapter.getSearchCriteria();
		String selection = "";

		int maxSize = chapCrit.size();
		int counter = 0;
		for (String key: chapCrit.keySet()) {
			String value = chapCrit.get(key);
			selection += key + " LIKE ? ";
			sArgs.add(value);
			
			counter++;
			if (counter < maxSize) {
				selection += "AND ";
			}

		}
		return selection;
	}
}
