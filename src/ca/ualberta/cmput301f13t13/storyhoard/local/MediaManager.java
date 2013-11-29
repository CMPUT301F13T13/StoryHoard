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
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract.MediaTable;

/**
 * Role: Interacts with the database to store, update, and retrieve media
 * objects. It implements the StoringManager interface.
 * 
 * </br>
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Media
 * @see StoringManager
 */
public class MediaManager extends StoringManager<Media>{
	private static DBHelper helper = null;
	private static MediaManager self = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;
	
	/**
	 * Initializes a new MediaManager media.
	 */
	protected MediaManager(Context context) {
		helper = DBHelper.getInstance(context);
	}
	
	/**
	 * Returns an instance of a OwnStoryManager. Used to implement
	 * the singleton design pattern.
	 * 
	 * @param context
	 * 
	 * @return MediaManager
	 */
	public static MediaManager getInstance(Context context) {
		if (self == null) {
			self = new MediaManager(context);
		}
		return self;
	}
	
	/**
	 * Inserts a new media media into the database.
	 * 
	 * @param media
	 * 			Media media to be inserted.
	 */
	@Override
	public void insert(Media media) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(media);
		db.insert(MediaTable.TABLE_NAME, null, values);	
	}

	private void setContentValues(Media media) {
		// Insert Media
		values = new ContentValues();
		values.put(MediaTable.COLUMN_NAME_MEDIA_ID, media.getId().toString());		
		values.put(MediaTable.COLUMN_NAME_CHAPTER_ID, media.getChapterId().toString());
		values.put(MediaTable.COLUMN_NAME_MEDIA_URI, media.getPath());
		values.put(MediaTable.COLUMN_NAME_TYPE, media.getType());;
		values.put(MediaTable.COLUMN_NAME_TEXT, media.getText());		
	}
	
	/**
	 * Retrieves a media media from the database.
	 * 
	 * @param criteria 
	 * 			Holds the search criteria.
	 */	
	@Override
	public ArrayList<Media> retrieve(Media criteria) {
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<Media> results = new ArrayList<Media>();
		
		setUpSearch(criteria);
		
		// Querying the database
		Cursor cursor = db.query(MediaTable.TABLE_NAME, projection, 
				selection, sArgs, null, null, null);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
		
			Media newMedia = new Media(
					UUID.fromString(cursor.getString(0)),  // media id
					UUID.fromString(cursor.getString(1)),  // chapter id
					cursor.getString(2),  // path
					cursor.getString(3),  // type
					cursor.getString(4)   // text
					);
			results.add(newMedia);
			cursor.moveToNext();
		}
		cursor.close();		
		return results;		
	}
	

	private void setUpSearch(Media criteria) {
		sArgs = null;
		ArrayList<String> selectionArgs = new ArrayList<String>();
		projection = new String[]{
				MediaTable.COLUMN_NAME_MEDIA_ID,
				MediaTable.COLUMN_NAME_CHAPTER_ID,
				MediaTable.COLUMN_NAME_MEDIA_URI,
				MediaTable.COLUMN_NAME_TYPE,
				MediaTable.COLUMN_NAME_TEXT
		};

		// Setting search criteria
		selection = setSearchCriteria(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			sArgs = null;
			selection = null;
		}
		
	}
	/**
	 * Updates a media media already in the database.
	 * 
	 * 
	 * @param newMedia
	 * 			Contains the changes to the media.
	 */	
	@Override
	public void update(Media newMedia) {
		SQLiteDatabase db = helper.getReadableDatabase();
		setContentValues(newMedia);
		selection = MediaTable.COLUMN_NAME_MEDIA_ID + " LIKE ?";
		sArgs = new String[]{ newMedia.getId().toString()};	
		db.update(MediaTable.TABLE_NAME, values, selection, sArgs);
		
	}

	/**
	 * Creates the selection string (a prepared statement) to be used 
	 * in the database query. Also creates an array holding the items
	 * to be placed in the ? of the selection.
	 *  
	 * @param media
	 * 			Holds the data needed to build the selection string 
	 * 			and the selection arguments array.
	 * @param sArgs
	 * 			Holds the arguments to be passed into the selection string.
	 * @return String
	 * 			The selection string.
	 */	
	@Override
	public String setSearchCriteria(Media media, ArrayList<String> sArgs) {
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

	@Override
	public void remove(UUID id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// Delete entry 
		selection = MediaTable.COLUMN_NAME_MEDIA_ID + " LIKE ?";
		sArgs = new String[]{ String.valueOf(id)};
		db.delete(MediaTable.TABLE_NAME, selection, sArgs);
	}

	public void syncDeletions(ArrayList<UUID> newMedias, UUID chapId) {
		ArrayList<Media> oldMedias = retrieve(new Media(null, chapId, null, null, ""));
		
		for (Media media : oldMedias) {
			if (!newMedias.contains(media.getId())) {
				remove(media.getId());
			}
		}
	}

	public ArrayList<Media> getPhotosByChapter(UUID chapterId) {
		return retrieve(new Media(null, chapterId, null, Media.PHOTO, ""));		
	}

	public ArrayList<Media> getIllustrationsByChapter(UUID chapterId) {
		return retrieve(new Media(null, chapterId, null, Media.ILLUSTRATION, ""));		
	}

	@Override
	public Media getById(UUID id) {
		ArrayList<Media> result = retrieve(new Media(id, null, null, null, null));
		if (result.size() != 1) {
			return null;
		}
		return result.get(0);
	}	
}
