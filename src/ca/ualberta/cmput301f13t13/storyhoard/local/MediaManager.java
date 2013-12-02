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
 * @see Media
 * @see StoringManager
 * @see DBContract
 */
public class MediaManager extends StoringManager<Media>{
	private static DBHelper helper = null;
	private static MediaManager self = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;
	
	/**
	 * Initializes a new MediaManager class. Must be given context in order to  
	 * create a new instance of DBHelper and also to get the phoneId of 
	 * whichever phone is using this application.</br></br>
	 * 
	 * Note that this constructor is protected, and it should never be used  
	 * outside of this class (except for any class that subclass it). 
	 * 
	 * @param context
	 * 
	 */
	protected MediaManager(Context context) {
		helper = DBHelper.getInstance(context);
	}
	
	/**
	 * Returns an instance of a MediaManager. Since this class is a singleton,  
	 * the same instance will always be returned. This is the method any class 
	 * outside of this one and any subclasses should use to get an mediaManager 
	 * object. </br></br>
	 * 
	 * Used to implement the singleton
	 * design pattern.
	 * 
	 * @param context
	 */
	public static MediaManager getInstance(Context context) {
		if (self == null) {
			self = new MediaManager(context);
		}
		return self;
	}
	
	/**
	 * Saves a new media into the database.</br></br>
	 * 
	 * Example Call.</br>
	 * Media media = new Media("3242a21d", "/path/", 
	 * 				"photo", "hello world");</br>
	 * MediaManager mm = MediaManager.getInstance(someActivity.this);</br>
	 * mm.insert(media);</br>
	 * 
	 * @param media
	 * 			A media object.
	 */
	@Override
	public void insert(Media media) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(media);
		db.insert(MediaTable.TABLE_NAME, null, values);	
	}
	/**
	 * Sets up the ContentValues for inserting or updating the database. This 
	 * specifies the columns to be inserted into and what content will be  
	 * going into those columns. 
	 * 
	 * @param media
	 * 			All the media's fields will be put into the database.
	 */
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
	 * Retrieves a media from the database.</br><br> 
	 * 
	 * The media passed into this method is a media holding search criteria, 
	 * so any field you would like to include in the search, just set the  
	 * search criteria holding story to it.</br><br>
	 * 
	 * Example Call.</br>
	 * Media media = new Media("3242a21d", "/path/", 
	 * 				"photo", "hello world");</br>
	 * MediaManager mm = MediaManager.getInstance(someActivity.this);</br>
	 * mm.insert(media);</br>
	 * 
	 * 
	 * To now search for this media based on its text: </br></br>
	 * 
	 * Media criteria = new Media(null, null, null, null, "hello world");</br>
	 * 
	 * 
	 * @param criteria 
	 * 			A media with the criteria in it.
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
	/**
	 * A helper function to set up what table columns and rows to be searched 
	 * or retrieved. Basically, building the sql query, but using content 
	 * values to abstract the sql.
	 * 
	 * @param criteria
	 */

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
		selection = buildSelectionString(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			sArgs = null;
			selection = null;
		}
		
	}
	/**
	 * Updates a media already in the database.
	 * 
	 * Example Call.</br>
	* Example Call.</br>
	 * Media media = new Media("3242a21d", "/path/", 
	 * 				"photo", "hello world");</br>
	 * MediaManager mm = MediaManager.getInstance(someActivity.this);</br>
	 * mm.insert(media);</br>
	 * media.setText("bah humbug");</br>
	 * mm.update(media);</br>
	 * 
	 * @param newmedia
	 * 			Media with changes.
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
	private String buildSelectionString(Media media, ArrayList<String> sArgs) {
		HashMap<String,String> medCrit = getSearchCriteria(media);
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

	/**
	 * Returns the information of the media (id, chapterId, type) that could be
	 * used in searching for a media in the database. This information is
	 * returned in a HashMap where the keys are the corresponding Media Table
	 * column names.
	 * 
	 * @return HashMap
	 */
	private HashMap<String, String> getSearchCriteria(Media media) {
		HashMap<String, String> info = new HashMap<String, String>();

		if (media.getId() != null) {
			info.put(MediaTable.COLUMN_NAME_MEDIA_ID, media.getId().toString());
		}

		if (media.getChapterId() != null) {
			info.put(MediaTable.COLUMN_NAME_CHAPTER_ID, media.getChapterId().toString());
		}

		if (media.getType() != null) {
			info.put(MediaTable.COLUMN_NAME_TYPE, media.getType());
		}

		return info;
	}
	/**
	 * Removes the media from the database
	 * 
	 * @param id
	 * 		This is the id of the media you want to be removed.
	 */
	@Override
	public void remove(UUID id) {
		SQLiteDatabase db = helper.getWritableDatabase();
		// Delete entry 
		selection = MediaTable.COLUMN_NAME_MEDIA_ID + " LIKE ?";
		sArgs = new String[]{ String.valueOf(id)};
		db.delete(MediaTable.TABLE_NAME, selection, sArgs);
	}
	/**
	 * Removes the media no longer in a chapter
	 * 
	 * @param ArrayList<UUID> newMedias
	 * 		The ArrayList of the media you want in the chapter.
	 * @param id
	 * 		This is the id of the chapter the new media will be in.
	 */
	public void syncDeletions(ArrayList<UUID> newMedias, UUID chapId) {
		ArrayList<Media> oldMedias = retrieve(new Media(null, chapId, null, null, ""));
		
		for (Media media : oldMedias) {
			if (!newMedias.contains(media.getId())) {
				remove(media.getId());
			}
		}
	}
	/**
	 * Retrieves the photos whose chapter id matches the id provided. It expects the id 
	 * provided to be a UUID. 
	 *  
	 * </br></br>
	 * 
	 * Example call:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * MediaManager mm = MediaManager.getInstance(someActivity.this);</br>
	 * ArrayList<Media> photos = mm.getById(id);</br>
	 * 
	 * @param id
	 * 			Id of the chapter we are looking in. Must be a UUID. 
	 */	
	public ArrayList<Media> getPhotosByChapter(UUID chapterId) {
		return retrieve(new Media(null, chapterId, null, Media.PHOTO, ""));		
	}
	/**
	 * Retrieves the illustrations whose chapter id matches the id provided. It expects the id 
	 * provided to be a UUID. 
	 *  
	 * </br></br>
	 * 
	 * Example call:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * MediaManager mm = MediaManager.getInstance(someActivity.this);</br>
	 * ArrayList<Media> illustrations = mm.getById(id);</br>
	 * 
	 * @param id
	 * 			Id of the chapter we are looking in. Must be a UUID. 
	 */	
	public ArrayList<Media> getIllustrationsByChapter(UUID chapterId) {
		return retrieve(new Media(null, chapterId, null, Media.ILLUSTRATION, ""));		
	}

	/**
	 * Retrieves the media whose id matches the id provided. It expects the id 
	 * provided to be a UUID. 
	 *  
	 * </br></br>
	 * 
	 * Example call:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * MediaManager mm = MediaManager.getInstance(someActivity.this);</br>
	 * Media  m = mm.getById(id);</br>
	 * 
	 * @param id
	 * 			Id of the media we are looking for. Must be a UUID. 
	 */	
	@Override
	public Media getById(UUID id) {
		ArrayList<Media> result = retrieve(new Media(id, null, null, null, null));
		if (result.size() != 1) {
			return null;
		}
		return result.get(0);
	}	
}
