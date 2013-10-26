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
		values.put(ChapterTable.COLUMN_NAME_TEXT, chapter.getText());
		values.put(ChapterTable.COLUMN_NAME_STORY_ID, chapter.getStoryId());
		
		db.insert(ChapterTable.TABLE_NAME, null, values);		
	}

	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		Chapter chapter= (Chapter) criteria;
		HashMap<String,String> chapCrit = chapter.getInfo();
		ArrayList<Object> results = new ArrayList<Object>();
		
		SQLiteDatabase db = helper.getReadableDatabase();

		String[] projection = {
				ChapterTable.COLUMN_NAME_CHAPTER_ID,
				ChapterTable.COLUMN_NAME_TEXT,
				ChapterTable.COLUMN_NAME_STORY_ID,	
		};

		String orderBy = ChapterTable._ID + " DESC";
		
		// Setting search criteria
		String selection = null;
		String[] sArgs = null;
		ArrayList<String> selectionArgs = new ArrayList<String>();
		int counter = 1;
		int maxSize = chapCrit.size();
		
		for (String key: chapCrit.keySet()) {
			if (!key.equals("")) {
				selection += key + " LIKE ? ";
				selectionArgs.add(chapCrit.get(key));
			}
			counter++;
			if (counter < maxSize) {
				selection += "AND ";
			}
		}
		
		if (selectionArgs.size() > 0) {
			sArgs = (String[]) selectionArgs.toArray();
		}
		
		// Querying the database
		Cursor cursor = db.query(ChapterTable.TABLE_NAME, projection, 
				selection, sArgs, null, null, orderBy);

		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String storyId = cursor.getString(0);
			
			/*
			 * GET ALL CHOICES BELONGING TO THIS CHAPTER WITH THE
			 * CHOICEMANAGER CLASS
			 */
			ChoiceManager cm = new ChoiceManager(context);
			Choice choice = new Choice(chapter.getId());
			
			ArrayList<Object> choiceObjs = cm.retrieve(choice, helper);

			Chapter newChapter = new Chapter(
					cursor.getString(0), // chapter id
					cursor.getString(1), // text
					cursor.getString(2) // story id
					);
			results.add(newChapter);
			cursor.moveToNext();
		}
		cursor.close();		
		return results;		
	}

	@Override
	public void update(Object oldObj, Object newObj, DBHelper helper) {
		// TODO Auto-generated method stub
		
	}
}
