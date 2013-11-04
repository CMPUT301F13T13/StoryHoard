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
import android.net.Uri;
import ca.ualberta.cs.c301f13t13.backend.DBContract.ChapterTable;
import ca.ualberta.cs.c301f13t13.backend.DBContract.MediaTable;
import ca.ualberta.cs.c301f13t13.gui.SHView;

/**
 * Role: Interacts with the database to store, update, and retrieve media
 * objects. It implements the StoringManager interface and inherits from the
 * Model class, meaning it can hold SHViews and notify them if they need to be
 * updated.
 * 
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Media
 * @see StoringManager
 * @see Model
 */
public class MediaManager extends Model<SHView> implements StoringManager{
	private Context context;
	private static MediaManager self = null;
	
	/**
	 * Initializes a new MediaManager object.
	 */
	protected MediaManager(Context context) {
		this.context = context;
	}
	
	/**
	 * Returns an instance of a StoryManager. Used to implement
	 * the singleton design pattern.
	 * 
	 * @param context
	 * @return
	 */
	public static MediaManager getInstance(Context context) {
		if (self == null) {
			self = new MediaManager(context);
		}
		return self;
	}
	
	@Override
	public void insert(Object object, DBHelper helper) {
		Media media = (Media) object;
		SQLiteDatabase db = helper.getWritableDatabase();

		// Insert Media
		ContentValues values = new ContentValues();
		values.put(MediaTable.COLUMN_NAME_MEDIA_ID, (media.getId()).toString());		
		values.put(MediaTable.COLUMN_NAME_CHAPTER_ID, (media.getChapterId()).toString());
		values.put(MediaTable.COLUMN_NAME_MEDIA_URI, (media.getUri()).toString());
		values.put(MediaTable.COLUMN_NAME_TYPE, media.getType());;

		db.insert(MediaTable.TABLE_NAME, null, values);	
		
	}

	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		ArrayList<Object> results = new ArrayList<Object>();
		String[] sArgs = null;
		ArrayList<String> selectionArgs = new ArrayList<String>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String[] projection = {
				MediaTable.COLUMN_NAME_MEDIA_ID,
				MediaTable.COLUMN_NAME_CHAPTER_ID,
				MediaTable.COLUMN_NAME_MEDIA_URI,
				MediaTable.COLUMN_NAME_TYPE
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
		Cursor cursor = db.query(MediaTable.TABLE_NAME, projection, 
				selection, sArgs, null, null, null);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
		
			Media newMedia = new Media(
					UUID.fromString(cursor.getString(0)), // media id
					UUID.fromString(cursor.getString(1)), // chapter id
					Uri.parse(cursor.getString(2)), // uri
					cursor.getString(3) // type
					);
			results.add(newMedia);
			cursor.moveToNext();
		}
		cursor.close();		
		return results;		
	}
	

	@Override
	public void update(Object newObject, DBHelper helper) {
		Media newM = (Media) newObject;
		SQLiteDatabase db = helper.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(MediaTable.COLUMN_NAME_MEDIA_ID, (newM.getId()).toString());		
		values.put(MediaTable.COLUMN_NAME_CHAPTER_ID, (newM.getChapterId()).toString());
		values.put(MediaTable.COLUMN_NAME_MEDIA_URI, (newM.getUri()).toString());
		values.put(MediaTable.COLUMN_NAME_TYPE, newM.getType());

		String selection = MediaTable.COLUMN_NAME_MEDIA_ID + " LIKE ?";
		String[] sArgs = { newM.getId().toString()};	

		db.update(MediaTable.TABLE_NAME, values, selection, sArgs);
		
	}

	@Override
	public String setSearchCriteria(Object object, ArrayList<String> sArgs) {
		Media media = (Media) object;
		HashMap<String,String> medCrit = media.getSearchCriteria();
		String selection = "";

		int maxSize = medCrit.size();
		int counter = 0;
		for (String key: medCrit.keySet()) {
			String value = medCrit.get(key);
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
