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
import ca.ualberta.cs.c301f13t13.backend.DBContract.StoryTable;

import com.google.gson.Gson;

/**
 * @author sgil
 *
 */
public class StoryManager extends Model implements StoringManager{
	private Context context;
	
	/**
	 * Initializes a new StoryManager object.
	 */
	public StoryManager(Context context) {
		this.context = context;
	}

	/**
	 * Saves a new story locally (in the database).
	 */	
	@Override
	public void insert(Object object, DBHelper helper) {
		Story story = (Story) object;
		SQLiteDatabase db = helper.getWritableDatabase();
			
		UUID chapterId = story.getFirstChapterId();
				
		// Insert story
		ContentValues values = new ContentValues();
		values.put(StoryTable.COLUMN_NAME_STORY_ID, (story.getId()).toString());		
		values.put(StoryTable.COLUMN_NAME_TITLE, story.getTitle());
		values.put(StoryTable.COLUMN_NAME_AUTHOR, story.getAuthor());
		values.put(StoryTable.COLUMN_NAME_DESCRIPTION, story.getDescription());
		values.put(StoryTable.COLUMN_NAME_FIRST_CHAPTER, chapterId.toString());
		values.put(StoryTable.COLUMN_NAME_CREATED, story.getAuthorsOwn().toString());
		
		db.insert(StoryTable.TABLE_NAME, null, values);		
	}

	/**
	 * Saves a story to the server for other users to see.
	 * @param story
	 */
	public void publish(Story story) {
		
	}

	/**
	 * Saves a published story locally.
	 * @param story
	 */
	public void cacheStory(Story story) {
		
	}
	
	/**
	 * Updates a story already in the database.
	 * 
	 * @param oldObject
	 * 			The object before update, used to find it in the database.
	 * 
	 * @param newObject
	 * 			Contains the changes to the object, it is what the oldObject
	 * 			info will be replaced with.
	 */
	@Override
	public void update(Object oldObject, Object newObject, DBHelper helper) {
		Story newS = (Story) newObject;
		String[] sArgs = null;
		SQLiteDatabase db = helper.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(StoryTable.COLUMN_NAME_STORY_ID, newS.getId().toString());
		values.put(StoryTable.COLUMN_NAME_TITLE, newS.getTitle());
		values.put(StoryTable.COLUMN_NAME_AUTHOR, newS.getAuthor());
		values.put(StoryTable.COLUMN_NAME_DESCRIPTION, newS.getDescription());
		values.put(StoryTable.COLUMN_NAME_FIRST_CHAPTER, 
				newS.getFirstChapterId().toString());
		values.put(StoryTable.COLUMN_NAME_CREATED, newS.getAuthorsOwn().toString());

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		String selection = setSearchCriteria(oldObject, selectionArgs);
		
		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}		
		
		db.update(StoryTable.TABLE_NAME, values, selection, sArgs);	
	}

	/**
	 * Retrieves a story /stories from the database.
	 * 
	 * @param criteria 
	 * 			Holds the search criteria.
	 * @param helper
	 * 			Used to open the database connection.
	 */
	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		ArrayList<Object> results = new ArrayList<Object>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] sArgs = null;
		String[] projection = {
				StoryTable.COLUMN_NAME_STORY_ID,
				StoryTable.COLUMN_NAME_TITLE,
				StoryTable.COLUMN_NAME_AUTHOR,
				StoryTable.COLUMN_NAME_DESCRIPTION,
				StoryTable.COLUMN_NAME_FIRST_CHAPTER,
				StoryTable.COLUMN_NAME_CREATED
		};

		String orderBy = StoryTable._ID + " DESC";
		
		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		String selection = setSearchCriteria(criteria, selectionArgs);
		
		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}
		
		// Querying the database
		Cursor cursor = db.query(StoryTable.TABLE_NAME, projection, selection, 
	            sArgs, null, null, orderBy);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String storyId = cursor.getString(0);
			
			// Find all chapters of the story
			ChapterManager cm = new ChapterManager(context);
			Chapter chapter = new Chapter(UUID.fromString(storyId));
//			ArrayList<Object> chapterObjs = cm.retrieve(chapter, helper);
			HashMap<UUID, Chapter> chapters = new HashMap<UUID, Chapter>();
/*			
			for (Object obj : chapterObjs) {
				Chapter chap = (Chapter) obj;
				chapters.put(chap.getId(), chap);
			}
*/			
			Story story = new Story(
					storyId,
					cursor.getString(1), // title
					cursor.getString(2), // author
					cursor.getString(3), // description
					cursor.getString(4), // first chapter id
					chapters,
					Boolean.valueOf(cursor.getString(5))
					);
			results.add(story);
			cursor.moveToNext();
		}
		cursor.close();		
		
		return results;
	}
	
	/**
	 * Retrieves all stories from the server, i.e. the published stories.
	 * 
	 * @return ArrayList
	 */
	public ArrayList<Story> getPublishedStories() {
		ArrayList<Story> published = new ArrayList<Story>();
		return published;
	}	

	/**
	 * Creates the selection string (a prepared statement) to be used 
	 * in the database query. Also creates an array holding the items
	 * to be placed in the ? of the selection.
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
		Story story = (Story) object;
		HashMap<String,String> storyCrit = story.getSearchCriteria();		
		
		// Setting search criteria
		String selection = "";
	
		int counter = 0;
		int maxSize = storyCrit.size();
		
		for (String key: storyCrit.keySet()) {
			String value = storyCrit.get(key);
			if (!value.equals("")) {
				selection += key + " LIKE ?";
				sArgs.add(value);
				
				counter++;
				if (counter < maxSize) {
					selection += " AND ";
				}
			}			
		}
		return selection;
	}
}
