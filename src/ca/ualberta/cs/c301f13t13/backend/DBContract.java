
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
 * Citing: 
 * Idea to use a database contract and the code base was gotten from:
 * URL: http://developer.android.com/training/basics/data-storage/databases.html
 * When: September 17, 2013
 * License: Apache 2.0
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import android.provider.BaseColumns;

/**
 * 
 * Purpose: 
 * Defines the tables that will be created for the application.
 * 
 * Design Rationale: 
 * To have a class devoted to the actual structure
 * of the database and its tables. Makes working with the Database
 * more organized and cleaner, and if we need to change a column or
 * something, we only need to change it here and not in the other parts
 * of the code.
 * 
 * @author Ashley Brown, Stephanie Gil
 *
 */
public final class DBContract {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "StoryHoard.Db";

	public DBContract() {}	

	/** 
	 * Sets up column names and then create and delete SQL statements
	 * for the table containing the stories.
	 */
	protected static abstract class StoryTable implements BaseColumns {

		private StoryTable() {}

		public static final String TABLE_NAME = "story_table";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_AUTHOR = "author";
		public static final String COLUMN_NAME_DESCRIPTION = "description";
		
		/* Acts as a boolean; if author created the story, created = 1. If
		  someone else created it, created = 0; */
		public static final String COLUMN_NAME_CREATED = "created";	
		
		// TO DO: Add column with phone id
		public static final String SQL_CREATE_TABLE = 
				"CREATE TABLE " + StoryTable.TABLE_NAME + " (" 
				+ StoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ StoryTable.COLUMN_NAME_TITLE + " TEXT, " 
				+ StoryTable.COLUMN_NAME_AUTHOR + " TEXT, " 
				+ StoryTable.COLUMN_NAME_DESCRIPTION + " TEXT "
				+ StoryTable.COLUMN_NAME_CREATED + " INTEGER)";	

		public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " 
				+ StoryTable.TABLE_NAME; 
	}

	/** 
	 * Sets up column names and then create and delete SQL statements
	 * for the table containing the chapters
	 */
	protected static abstract class ChapterTable implements BaseColumns {

		private ChapterTable() {}

		public static final String TABLE_NAME = "chapter_table";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_AUTHOR = "author";
		public static final String COLUMN_NAME_STORY_ID = "story";

		public static final String SQL_CREATE_TABLE = 
		        "CREATE TABLE " + ChapterTable.TABLE_NAME + " (" 
				+ ChapterTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
		        + ChapterTable.COLUMN_NAME_TEXT + " TEXT, " 
		        + ChapterTable.COLUMN_NAME_AUTHOR + " TEXT, "
		        + ChapterTable.COLUMN_NAME_STORY_ID + " INTEGER)";	

		public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " 
		        + ChapterTable.TABLE_NAME; 
	}	
	
	/** 
	 * Sets up column names and then create and delete SQL statements
	 * for the table containing the choices
	 */
	protected static abstract class ChoiceTable implements BaseColumns {

		private ChoiceTable() {}

		public static final String TABLE_NAME = "choice_table";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_CURR_CHAPTER = "curr_chapter";
		public static final String COLUMN_NAME_NEXT_CHAPTER = "next_chapter";

		public static final String SQL_CREATE_TABLE = 
		        "CREATE TABLE " + ChoiceTable.TABLE_NAME + " (" 
				+ ChoiceTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
		        + ChoiceTable.COLUMN_NAME_TEXT + " TEXT, " 
		        + ChoiceTable.COLUMN_NAME_CURR_CHAPTER + " INTEGER, "
		        + ChoiceTable.COLUMN_NAME_NEXT_CHAPTER + " INTEGER)";	

		public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " 
		        + ChoiceTable.TABLE_NAME; 
	}		

	/** 
	 * Sets up column names and then create and delete SQL statements
	 * for the table containing the photos
	 */
	protected static abstract class PhotoTable implements BaseColumns {

		private PhotoTable() {}

		public static final String TABLE_NAME = "photos_table";
		public static final String COLUMN_NAME_PHOTO = "blob";

		public static final String SQL_CREATE_TABLE = 
		        "CREATE TABLE " + PhotoTable.TABLE_NAME + " (" 
				+ PhotoTable._ID + " INTEGER PRIMARY KEY," 
		        + PhotoTable.COLUMN_NAME_PHOTO + " BLOB)";	

		public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " 
		        + PhotoTable.TABLE_NAME; 
	}		
	
	/** 
	 * Sets up column names and then create and delete SQL statements
	 * for the table containing the illustrations
	 */
	protected static abstract class IllustrationTable implements BaseColumns {

		private IllustrationTable() {}

		public static final String TABLE_NAME = "illustration_table";
		public static final String COLUMN_NAME_ILL = "blob";
		public static final String COLUMN_NAME_CHAPTER_ID = "chapter_id";

		public static final String SQL_CREATE_TABLE = 
		        "CREATE TABLE " + IllustrationTable.TABLE_NAME + " (" 
				+ IllustrationTable._ID + " INTEGER PRIMARY KEY," 
		        + IllustrationTable.COLUMN_NAME_ILL + " BLOB, "
		        + IllustrationTable.COLUMN_NAME_CHAPTER_ID + " INTEGER)";	

		public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " 
		        + IllustrationTable.TABLE_NAME; 
	}		
	
	/** 
	 * Sets up column names and then create and delete SQL statements
	 * for the table containing the illustrations. Describes which 
	 * photos belong to which chapters.
	 */
	protected static abstract class PhotoRelationTable implements BaseColumns {

		private PhotoRelationTable() {}

		public static final String TABLE_NAME = "photo_relation_table";
		public static final String COLUMN_NAME_PHOTO_ID = "photo_id";
		public static final String COLUMN_NAME_CHAPTER_ID = "chapter_id";

		public static final String SQL_CREATE_TABLE = 
		        "CREATE TABLE " + PhotoRelationTable.TABLE_NAME + " (" 
				+ PhotoRelationTable.COLUMN_NAME_PHOTO_ID+ " INTEGER," 
		        + PhotoRelationTable.COLUMN_NAME_CHAPTER_ID + " INTEGER)";	

		public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " 
		        + PhotoRelationTable.TABLE_NAME; 
	}			
}
