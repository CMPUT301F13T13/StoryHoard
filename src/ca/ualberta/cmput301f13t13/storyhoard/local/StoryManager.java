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

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract.StoryTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Role: Interacts with the database to store, update, and retrieve story
 * objects. It implements the StoringManager interface.
 * 
 * </br>
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Story
 * @see StoringManager
 */
public class StoryManager implements StoringManager<Story> {
	private static DBHelper helper = null;
	private static StoryManager self = null;
	private static String phoneId = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;

	/**
	 * Initializes a new OwnStoryManager story.
	 */
	protected StoryManager(Context context) {
		helper = DBHelper.getInstance(context);
		phoneId = Utilities.getPhoneId(context);
	}

	/**
	 * Returns an instance of a OwnStoryManager. Used to implement the singleton
	 * design pattern.
	 * 
	 * @param context
	 * @return OwnStoryManager
	 */
	public static StoryManager getInstance(Context context) {
		if (self == null) {
			self = new StoryManager(context);
		}
		return self;
	}

	/**
	 * Saves a new story locally (in the database). You can insert
	 * a story that has any field empty except for its ID.
	 * 
	 * </br> Example Call.
	 * </br> Story story = new Story("The boat", "Bob Week", 
	 * 				"The boat that could", phoneId);
	 * </br> OwnStoryManager sm = new OwnStoryManager.getInstance();
	 * </br> sm.insert(story);
	 */
	@Override
	public void insert(Story story) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(story);
		db.insert(StoryTable.TABLE_NAME, null, values);
	}

	/**
	 * Updates a story already in the database.
	 * 
	 * @param newStory
	 *            Contains the changes to the story.
	 */
	@Override
	public void update(Story newStory) {
		setContentValues(newStory);
		Story newS = (Story) newStory;
		selection = StoryTable.COLUMN_NAME_STORY_ID + " LIKE ?";
		sArgs = new String[]{ newS.getId().toString() };
		SQLiteDatabase db = helper.getReadableDatabase();
		db.update(StoryTable.TABLE_NAME, values, selection, 
				sArgs);	
	}

	/**
	 * Retrieves a story /stories from the database.
	 * 
	 * @param criteria
	 *            Holds the search criteria.
	 */
	@Override
	public ArrayList<Story> retrieve(Story criteria) {
		ArrayList<Story> results = new ArrayList<Story>();
		SQLiteDatabase db = helper.getReadableDatabase();
		setUpSearch(criteria);

		// Querying the database
		Cursor cursor = db.query(StoryTable.TABLE_NAME, projection, selection,
				sArgs, null, null, null);
		
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
	
	protected void setUpSearch(Story criteria) {
		sArgs = null;
		projection = new String[]{ 
				StoryTable.COLUMN_NAME_STORY_ID,
				StoryTable.COLUMN_NAME_TITLE, 
				StoryTable.COLUMN_NAME_AUTHOR,
				StoryTable.COLUMN_NAME_DESCRIPTION,
				StoryTable.COLUMN_NAME_FIRST_CHAPTER,
				StoryTable.COLUMN_NAME_PHONE_ID };

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		selection = setSearchCriteria(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}
	}	
	
	/**
	 * Sets up the ContentValues for inserting or updating the database.
	 * @param story
	 */
	protected void setContentValues(Story story) {
		UUID chapterId = story.getFirstChapterId();

		// Insert story
		values = new ContentValues();
		values.put(StoryTable.COLUMN_NAME_STORY_ID, 
				(story.getId()).toString());
		values.put(StoryTable.COLUMN_NAME_TITLE, story.getTitle());
		values.put(StoryTable.COLUMN_NAME_AUTHOR, story.getAuthor());
		values.put(StoryTable.COLUMN_NAME_DESCRIPTION,
				story.getDescription());
		if (chapterId != null) {
			values.put(StoryTable.COLUMN_NAME_FIRST_CHAPTER, 
				chapterId.toString());
		}
		values.put(StoryTable.COLUMN_NAME_PHONE_ID, story.getPhoneId());
	}
	
	/**
	 * Creates the selection string (a prepared statement) to be used in the
	 * database query. Also creates an array holding the items to be placed in
	 * the ? of the selection.
	 * 
	 * @param story
	 *            Holds the data needed to build the selection string and the
	 *            selection arguments array.
	 * @param sArgs
	 *            Holds the arguments to be passed into the selection string.
	 * @return String The selection string, i.e. the where clause that will be
	 *         used in the sql query.
	 */
	public String setSearchCriteria(Story story, ArrayList<String> sArgs) {
		HashMap<String, String> storyCrit = story.getSearchCriteria();

		// Setting search criteria
		String selection = "1 LIKE ?";
		sArgs.add("1");
		
		for (String key : storyCrit.keySet()) {
			
			if (key.equals(StoryTable.COLUMN_NAME_PHONE_ID) 
					&& story.getPhoneId().equals(Story.NOT_AUTHORS)) {
				selection += " AND " + key	+ " NOT LIKE ?"; 
				sArgs.add(phoneId);
			} else {
				String value = storyCrit.get(key);
				selection += " AND " + key + " LIKE ?";
				sArgs.add(value);
			}
		}
		
		return selection;
	}

	@Override
	public void remove(UUID id) {
		// Not implemented for this iteration of the project
	}	
	
	@Override
	public Boolean existsLocally(Story story) {
		Story crit = new Story(story.getId(), null, null, null, null);
		ArrayList<Story> stories = retrieve(crit);
		if (stories.size() != 1) {
			return false;
		}
		return true;		
	}

	public void syncStory(Story story) {
		if (existsLocally(story)) {
			update(story);
		} else {
			insert(story);
		}
	}	
}
