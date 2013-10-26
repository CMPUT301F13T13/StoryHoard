
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
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * Purpose: 
 * Creates and sets up the database to be used using the information
 * about the database provided in the contract class (DbHelper). Also
 * keeps the tables updated.
 *
 * Design Rationale: 
 * To make sure there aren't a million databases being created and passed 
 * around, we made this class a singleton so that only one database is ever 
 * being opened and used. This is also to avoid inconsistency within the 
 * database.
 * 
 * @author Ashley Brown, Stephanie Gil
 *
 */
public class DBHelper extends SQLiteOpenHelper{

	/**
	 * 
	 * Instantiates a new DbHelper object.
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
	}

	/**
	 * Sets up the tables.
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBContract.StoryTable.SQL_CREATE_TABLE);
		db.execSQL(DBContract.ChapterTable.SQL_CREATE_TABLE);
		db.execSQL(DBContract.ChoiceTable.SQL_CREATE_TABLE);
		db.execSQL(DBContract.PhotoTable.SQL_CREATE_TABLE);
		db.execSQL(DBContract.IllustrationTable.SQL_CREATE_TABLE);
		db.execSQL(DBContract.PhotoRelationTable.SQL_CREATE_TABLE);
	}

	/**
	 * Deletes the tables and recreates them for an update.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBContract.StoryTable.SQL_DELETE_TABLE);
		db.execSQL(DBContract.ChapterTable.SQL_DELETE_TABLE);
		db.execSQL(DBContract.ChoiceTable.SQL_DELETE_TABLE);
		db.execSQL(DBContract.PhotoTable.SQL_DELETE_TABLE);
		db.execSQL(DBContract.IllustrationTable.SQL_DELETE_TABLE);
		db.execSQL(DBContract.PhotoRelationTable.SQL_DELETE_TABLE);
		onCreate(db);	
	}
}
