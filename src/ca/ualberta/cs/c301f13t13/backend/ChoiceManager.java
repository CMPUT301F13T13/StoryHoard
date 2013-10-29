package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ca.ualberta.cs.c301f13t13.backend.DBContract.ChoiceTable;
/**
 * @author adbrown
 *
 */

public class ChoiceManager extends Model implements StoringManager {
		private Context context;
		
		/**
		 * Initializes a new ChoiceManager object.
		 */
		public ChoiceManager(Context context) {
			this.context = context;
		}

		/**
		 * Saves a new choice locally (in the database).
		 */	
		@Override
		public void insert(Object object, DBHelper helper) {
			Choice choice = (Choice) object;
			SQLiteDatabase db = helper.getWritableDatabase();
					
			// Insert choice
			ContentValues values = new ContentValues();
			values.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, choice.getId().toString());		
			values.put(ChoiceTable.COLUMN_NAME_STORY_ID, choice.getStoryId().toString());
			values.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, choice.getChapterIdFrom().toString());
			values.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, choice.getChapterIdTo().toString());
			values.put(ChoiceTable.COLUMN_NAME_TEXT, choice.getText());
			values.put(ChoiceTable.COLUMN_NAME_CHOICE_NUMBER, Integer.toString(choice.getChoiceNum()));
			db.insert(ChoiceTable.TABLE_NAME, null, values);		
		}

		/**
		 * Updates a choice already in the database.
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
			Choice newC = (Choice) newObject;
			String[] sArgs = null;
			SQLiteDatabase db = helper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, newC.getId().toString());		
			values.put(ChoiceTable.COLUMN_NAME_STORY_ID, newC.getStoryId().toString());
			values.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER, newC.getChapterIdFrom().toString());
			values.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER, newC.getChapterIdTo().toString());
			values.put(ChoiceTable.COLUMN_NAME_TEXT, newC.getText());
			values.put(ChoiceTable.COLUMN_NAME_CHOICE_NUMBER, Integer.toString(newC.getChoiceNum()));	
			// Setting search criteria
			ArrayList<String> selectionArgs = new ArrayList<String>();
			String selection = setSearchCriteria(oldObject, selectionArgs);
			
			if (selectionArgs.size() > 0) {
				sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
			} else {
				selection = null;
			}		
			
			db.update(ChoiceTable.TABLE_NAME, values, selection, sArgs);	
		}

		/**
		 * Retrieves a choice from the database.
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
					ChoiceTable.COLUMN_NAME_CHOICE_ID,
					ChoiceTable.COLUMN_NAME_STORY_ID,
					ChoiceTable.COLUMN_NAME_CURR_CHAPTER,
					ChoiceTable.COLUMN_NAME_NEXT_CHAPTER,
					ChoiceTable.COLUMN_NAME_TEXT,
					ChoiceTable.COLUMN_NAME_CHOICE_NUMBER
			};
			String orderBy = ChoiceTable._ID + " DESC";
			
			// Setting search criteria
			ArrayList<String> selectionArgs = new ArrayList<String>();
			String selection = setSearchCriteria(criteria, selectionArgs);
			
			if (selectionArgs.size() > 0) {
				sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
			} else {
				selection = null;
			}
			
			// Querying the database
			Cursor cursor = db.query(ChoiceTable.TABLE_NAME, projection, selection, 
		            sArgs, null, null, orderBy);

			// Retrieving all the entries
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String choiceId = cursor.getString(0);
				
				Choice choice = new Choice(
						UUID.fromString(choiceId),
						UUID.fromString(cursor.getString(1)), // story id
						UUID.fromString(cursor.getString(2)), // current chapter
						UUID.fromString(cursor.getString(3)), // next chapter
						cursor.getString(4), // text
						cursor.getInt(5) // choice number
						);
				results.add(choice);
				cursor.moveToNext();
			}
			cursor.close();		
			
			return results;
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
			Choice choice = (Choice) object;
			HashMap<String,String> choiceCrit = choice.getSearchCriteria();		
			
			// Setting search criteria
			String selection = "";
		
			int counter = 0;
			int maxSize = choiceCrit.size();
			
			for (String key: choiceCrit.keySet()) {
				String value = choiceCrit.get(key);
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
