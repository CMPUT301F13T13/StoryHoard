/**
 * 
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
import ca.ualberta.cs.c301f13t13.gui.View;

/**
 * @author Owner
 *
 */
public class ChapterManager extends Model<View> implements StoringManager{
	private Context context;
	
	public ChapterManager(Context context) {
		this.context = context;
	}
	
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

		String orderBy = ChapterTable._ID + " DESC";
		
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
				selection, sArgs, null, null, orderBy);

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

	@Override
	public void update(Object oldObject, Object newObject, DBHelper helper) {
		Chapter newC = (Chapter) newObject;
		String[] sArgs = null;
		SQLiteDatabase db = helper.getReadableDatabase();

		ContentValues values = new ContentValues();
		values.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, newC.getId().toString());
		values.put(ChapterTable.COLUMN_NAME_TEXT, newC.getText());
		values.put(ChapterTable.COLUMN_NAME_STORY_ID, newC.getStoryId().toString());

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		String selection = setSearchCriteria(oldObject, selectionArgs);
		
		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}		
		
		db.update(ChapterTable.TABLE_NAME, values, selection, sArgs);	
	}
	
	public String setSearchCriteria(Object object, ArrayList<String> sArgs) {
		Chapter chapter = (Chapter) object;
		HashMap<String,String> chapCrit = chapter.getSearchCriteria();
		String selection = "";
		
		int maxSize = chapCrit.size();
		int counter = 0;
		for (String key: chapCrit.keySet()) {
			String value = chapCrit.get(key);
			if (!value.equals("")) {
				selection += key + " LIKE ? ";
				sArgs.add(value);
			}
			counter++;
			if (counter < maxSize) {
				selection += "AND ";
			}
		}
		return selection;
	}
}
