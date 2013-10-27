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
	 * Saves a new story locally.
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
	
	@Override
	public void update(Object oldObject, Object newObject, DBHelper helper) {
		Story oldS = (Story) oldObject;
		Story newS = (Story) newObject;
	}

	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		HashMap<String,String> storyCrit = ((Story)criteria).getSearchCriteria();
		ArrayList<Object> results = new ArrayList<Object>();
		
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] projection = {
				StoryTable.COLUMN_NAME_STORY_ID,
				StoryTable.COLUMN_NAME_TITLE,
				StoryTable.COLUMN_NAME_AUTHOR,
				StoryTable.COLUMN_NAME_DESCRIPTION,
				StoryTable.COLUMN_NAME_FIRST_CHAPTER
		};

		String orderBy = StoryTable._ID + " DESC";
		
		// Setting search criteria
		String selection = "";
		ArrayList<String> selectionArgs = new ArrayList<String>();
		int counter = 0;
		int maxSize = storyCrit.size();
		
		for (String key: storyCrit.keySet()) {
			if (!key.equals("")) {
				selection += key + " LIKE ?";
				selectionArgs.add(storyCrit.get(key));
			}
			counter++;
			if (counter < maxSize) {
				selection += " AND ";
			}			
		}
		
		String[] sArgs;
		
		if (selectionArgs.size() > 0) {
			//sArgs = new String[selectionArgs.size()];
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			sArgs = null;
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
					chapters
					);
			results.add(story);
			cursor.moveToNext();
		}
		cursor.close();		
		
		return results;
	}

	/**
	 * Returns a list of all cached stories.
	 * @return
	 */
	public ArrayList<Story> getCachedStories() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Story> getPublishedStories() {
		// TODO Auto-generated method stub
		return null;
	}

}
