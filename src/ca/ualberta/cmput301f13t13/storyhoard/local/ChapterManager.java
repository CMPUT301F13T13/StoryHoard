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
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Chapter
 * @see StoringManager
 */
public class ChapterManager implements StoringManager<Chapter> {
	private static DBHelper helper = null;
	private static ChapterManager self = null;
	private static ContentValues values;

	/**
	 * Initializes a new ChapterManager chapter.
	 * 
	 * @param context
	 */
	protected ChapterManager(Context context) {
		helper = DBHelper.getInstance(context);
	}

	/**
	 * Returns an instance of itself. Used to accomplish the singleton design
	 * pattern.
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
	 * @param chapter
	 *            Chapter to be stored in the database. In this case, it will be
	 *            a chapter chapter.
	 */
	@Override
	public void insert(Chapter chapter) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(chapter);
		db.insert(ChapterTable.TABLE_NAME, null, values);
	}

	/**
	 * Takes a chapter chapter that holds the desired search criteria (null
	 * values for any parameter that you don't want included in the search, for
	 * example, chapter text). It then retrieves a chapter or chapters from the
	 * database that matched the criteria and returns them in an ArrayList.
	 * 
	 * @param criteria
	 *            Criteria for the chapter(s) to be retrieved from the database.
	 * @return chapters Contains the chapters that matched the search criteria.
	 */
	@Override
	public ArrayList<Chapter> retrieve(Chapter criteria) {
		ArrayList<Chapter> results = new ArrayList<Chapter>();
		String[] sArgs = null;
		ArrayList<String> selectionArgs = new ArrayList<String>();

		SQLiteDatabase db = helper.getReadableDatabase();

		String[] projection = { ChapterTable.COLUMN_NAME_CHAPTER_ID,
				ChapterTable.COLUMN_NAME_STORY_ID,
				ChapterTable.COLUMN_NAME_TEXT,
				ChapterTable.COLUMN_NAME_RANDOM_CHOICE };

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
		String selection = ChapterTable.COLUMN_NAME_CHAPTER_ID + " LIKE ?";
		String[] sArgs = { newC.getId().toString() };

		db.update(ChapterTable.TABLE_NAME, values, selection, sArgs);
	}
	
	/**
	 * Sets the content values to be used in the sql query.
	 * 
	 * @param chapter
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
	public String setSearchCriteria(Chapter chapter, ArrayList<String> sArgs) {
		HashMap<String, String> chapCrit = chapter.getSearchCriteria();
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

	@Override
	public void remove(UUID id) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Boolean existsLocally(Chapter chap) {
		Chapter crit = new Chapter(chap.getId(), null, null, null);
		ArrayList<Chapter> chapters = retrieve(crit);
		if (chapters.size() != 1) {
			return false;
		}
		return true;		
	}

	public void syncChapter(Chapter chap) {
		if (existsLocally(chap)) {
			update(chap);
		} else {
			insert(chap);
		}
	}
}
