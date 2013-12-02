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

package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract.ChapterTable;

/**
 * Role: Interacts with the database to store, update, and retrieve chapter
 * chapters. It implements the StoringManager interface.
 * 
 * </br>
 * The setup of the database being used is defined in DBContract.java, so for 
 * more information on the actual tables and SQL statements used to make them, 
 * see that class.
 * 
 * Design Pattern: This class is a singleton, so there will ever only be one 
 * instance of it. Use the getInstance() static method to retrieve an  
 * instance of it, not the constructor.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Chapter
 * @see StoringManager
 * @see DBContract
 */
public class ChapterManager extends StoringManager<Chapter> {
	private static DBHelper helper = null;
	private static ChapterManager self = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;

	/**
	 * Initializes a new ChapterManager class. Must be given context in order to  
	 * create a new instance of DBHelper and also to get the phoneId of 
	 * whichever phone is using this application.</br></br>
	 * 
	 * Note that this constructor is protected, and it should never be used  
	 * outside of this class (except for any class that subclass it). 
	 * 
	 * @param context
	 * 
	 */
	protected ChapterManager(Context context) {
		helper = DBHelper.getInstance(context);
	}

	/**
	 * Returns an instance of a ChapterManager. Since this class is a singleton,  
	 * the same instance will always be returned. This is the method any class 
	 * outside of this one and any subclasses should use to get an ChapterManager 
	 * object. </br></br>
	 * 
	 * Used to implement the singleton
	 * design pattern.
	 * 
	 * @param context
	 */
	public static ChapterManager getInstance(Context context) {
		if (self == null) {
			self = new ChapterManager(context);
		}
		return self;
	}

	/**
	 * Saves a new chapter in the database. You can insert a chapter that  
	 * has any field empty except for its ID and story ID.</br></br>
	 * 
	 * Example Call.</br>
	 * Chapter mockChapter = new chapter("12312", "1233", 
	 * 				"Working");</br>
	 * ChapterManager cm = ChapterManager.getInstance(someActivity.this);</br>
	 * cm.insert(mockChapter);</br>
	 * 
	 * @param chapter
	 * 			A Chapter object. Any field can be null except its id and story id.
	 */
	@Override
	public void insert(Chapter chapter) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(chapter);
		db.insert(ChapterTable.TABLE_NAME, null, values);
	}

	/**
	 * Retrieves a chapter from the database. Always returns an array 
	 * list of chapter.</br><br> 
	 * 
	 * The chapter passed into this method is a chapter holding search criteria, 
	 * so any field you would like to include in the search, just set the  
	 * search criteria holding story to it.</br><br>
	 * 
	 * Example Call.</br>
	 * Chapter mockChapter = new chapter("12312", "1233", 
	 * 				"Working");</br>
	 * ChapterManager cm = ChapterManager.getInstance(someActivity.this);</br>
	 * cm.insert(mockChapter);</br>
	 * 
	 * To now search for this chapter based on its text: </br></br>
	 * 
	 * Chapter criteria = new Chapter(null, null,"Working");</br>
	 * 
	 * Notice that the first field was null. That is because when making a 
	 * search criteria object, you have to use the constructor that lets you 
	 * specify the id. This way, you are allowed to manually set any field 
	 * you want to be included in the field.</br></br>
	 * 
	 * 
	 * @param newChapter
	 * 			Chapter with changes. The old chapter's information will be 
	 * 			changed with the new chapter,s information.
	 */
	@Override
	public ArrayList<Chapter> retrieve(Chapter criteria) {
		ArrayList<Chapter> results = new ArrayList<Chapter>();
		SQLiteDatabase db = helper.getReadableDatabase();
		setupSearch(criteria);

		// Querying the database
		Cursor cursor = db.query(ChapterTable.TABLE_NAME, projection,
				selection, sArgs, null, null, null);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String storyId = cursor.getString(1);

			Chapter newChapter = new Chapter(UUID.fromString(cursor
					.getString(0)), // chapter id
					UUID.fromString(storyId), // story id
					cursor.getString(2), // text
					Boolean.valueOf(cursor.getString(3)) // random choice flag
			);
			results.add(newChapter);
			cursor.moveToNext();
		}
		cursor.close();
		return results;
	}
	/**
	 * A helper to set up what to be searched 
	 * or retrieved for the database tables.
	 * @param criteria
	 */
	private void setupSearch(Chapter criteria) {
		sArgs = null;
		ArrayList<String> selectionArgs = new ArrayList<String>();

		projection = new String[]{ ChapterTable.COLUMN_NAME_CHAPTER_ID,
				ChapterTable.COLUMN_NAME_STORY_ID,
				ChapterTable.COLUMN_NAME_TEXT,
				ChapterTable.COLUMN_NAME_RANDOM_CHOICE };

		// Setting search criteria
		selection = buildSelectionString(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			sArgs = null;
			selection = null;
		}		
	}
	
	/**
	 * Updates a chapter's data in the database.
	 * 
	 * @param newChapter
	 *            Holds the new data that will be used to update.
	 */
	@Override
	public void update(Chapter newChapter) {
		setContentValues(newChapter);
		Chapter newC = (Chapter) newChapter;
		SQLiteDatabase db = helper.getReadableDatabase();
		selection = ChapterTable.COLUMN_NAME_CHAPTER_ID + " LIKE ?";
		sArgs = new String[]{ newC.getId().toString() };
		db.update(ChapterTable.TABLE_NAME, values, selection, sArgs);
	}
	
	/**
	 * Sets the content values to be used in the sql query.
	 * 
	 * @param chapter
	 * 		All the chapter's fields will be put into the database.
	 */
	private void setContentValues(Chapter chapter) {
		// Insert chapter
		values = new ContentValues();
		values.put(ChapterTable.COLUMN_NAME_CHAPTER_ID,
				(chapter.getId()).toString());
		values.put(ChapterTable.COLUMN_NAME_STORY_ID, 
				chapter.getStoryId().toString());
		values.put(ChapterTable.COLUMN_NAME_TEXT, chapter.getText());
		values.put(ChapterTable.COLUMN_NAME_RANDOM_CHOICE, 
				chapter.hasRandomChoice().toString());
	}

	/**
	 * Creates the selection string (the sql where clause) to be used in the
	 * database query. Also creates an array holding the items the selection
	 * arguments, since the selection string is a prepared statement.
	 * 
	 * Eg. selection: WHERE CHAPTER_ID = ? selectionArg = "11244543"
	 * 
	 * @param chapter
	 *            Holds the data needed to build the selection string and the
	 *            selection arguments array.
	 * @param sArgs
	 *            Holds the arguments to be passed into the selection string.
	 * @return selection The selection string.
	 */
	private String buildSelectionString(Chapter chapter, ArrayList<String> sArgs) {
		HashMap<String, String> chapCrit = getSearchCriteria(chapter);
		String selection = "";

		int maxSize = chapCrit.size();
		int counter = 0;
		for (String key : chapCrit.keySet()) {
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

	/**
	 * Returns the information of the chapter (id, storyId) that could be used
	 * in searching for a chapter in the database. This information is returned
	 * in a HashMap where the keys are the corresponding Chapter Table column
	 * names.
	 * 
	 * @return HashMap
	 */
	private HashMap<String, String> getSearchCriteria(Chapter chapter) {
		HashMap<String, String> info = new HashMap<String, String>();

		if (chapter.getId() != null) {
			info.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, 
					chapter.getId().toString());
		}

		if (chapter.getStoryId() != null) {
			info.put(ChapterTable.COLUMN_NAME_STORY_ID, 
					chapter.getStoryId().toString());
		}

		return info;
	}
	
	/**
	 * Retrieves all the chapters that are in a given story.
	 * 
	 * @param storyId
	 *            Id of the story the chapters are wanted from.
	 * @return ArrayList of the chapters.
	 */
	public ArrayList<Chapter> getChaptersByStory(UUID storyId) {
		Chapter criteria = new Chapter(null, storyId, null, null);	
		return retrieve(criteria);
	}
	/**
	 * Retrieves the chapter whose id matches the id provided. It expects the id 
	 * provided to be a UUID. 
	 * 
	 * </br></br>
	 * 
	 * Example call:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * ChapterManager cm = ChapterManager.getInstance(someActivity.this);</br>
	 * Chapter mockChapter = cm.getById(id);</br>
	 * 
	 * @param id
	 * 			Id of the chapter we are looking for. Must be a UUID. 
	 */	

	@Override
	public Chapter getById(UUID id) {
		ArrayList<Chapter> result = retrieve(new Chapter(id, null, null, null));	
		if (result.size() != 1) {
			return null;
		}
		return result.get(0);
	}
}
