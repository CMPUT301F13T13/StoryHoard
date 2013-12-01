/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract.StoryTable;

/**
 * Role: Interacts with the database to store, update, and retrieve story
 * objects. It implements the StoringManager interface.</br></br>
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
 * @see Story
 * @see StoringManager
 * @see DBContract
 */
public class StoryManager extends StoringManager<Story> {
	private static DBHelper helper = null;
	private static StoryManager self = null;
	private static String phoneId = null;
	protected ContentValues values;
	protected String selection;
	protected String[] sArgs;
	protected String[] projection;

	/**
	 * Initializes a new StoryManager class. Must be given context in order to 
	 * create a new instance of DBHelper and also to get the phoneId of 
	 * whichever phone is using this application.</br></br>
	 * 
	 * Note that this constructor is protected, and it should never be used 
	 * outside of this class (except for any class that subclass it).
	 * 
	 * @param context
	 * 
	 */
	protected StoryManager(Context context) {
		helper = DBHelper.getInstance(context);
		phoneId = Utilities.getPhoneId(context);
	}

	/**
	 * Returns an instance of a StoryManager. Since this class is a singleton, 
	 * the same instance will always be returned. This is the method any class
	 * outside of this one and any subclasses should use to get an StoryManager
	 * object. </br></br>
	 * 
	 * Used to implement the singleton
	 * design pattern.
	 * 
	 * @param context
	 */
	public static StoryManager getInstance(Context context) {
		if (self == null) {
			self = new StoryManager(context);
		}
		return self;
	}

	/**
	 * Saves a new story locally (in the database). You can insert a story that 
	 * has any field empty / null except for its ID. The id is the one field 
	 * that can never be null.</br></br>
	 * 
	 * Example Call.</br>
	 * Story story = new Story("The boat", "Bob Week", 
	 * 				"The boat that could", "123ab5");</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * storyMan.insert(story);</br>
	 * 
	 * @param story
	 * 			A story object. Any field can be null except its id.
	 */
	@Override
	public void insert(Story story) {
		SQLiteDatabase db = helper.getWritableDatabase();
		setContentValues(story);
		db.insert(StoryTable.TABLE_NAME, null, values);
	}

	/**
	 * Updates a story already in the database. Once again, any of the fields
	 * of the new story you want to update the old version with can be null
	 * except for the id.
	 * 
	 * Example Call.</br>
	 * Story story = new Story("The boat", "Bob Week", 
	 * 				"The boat that could", "123ab5");</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * storyMan.insert(story);</br>
	 * story.setTitle("new title");</br>
	 * storyMan.update(story);</br>
	 * 
	 * @param newStory
	 * 			Story with changes. The old story's information will be 
	 * 			changed with the new story,s information.
	 */
	@Override
	public void update(Story newStory) {
		setContentValues(newStory);
		Story newS = (Story) newStory;
		selection = StoryTable.COLUMN_NAME_STORY_ID + " LIKE ?";
		sArgs = new String[]{ newS.getId().toString() };
		SQLiteDatabase db = helper.getReadableDatabase();
		db.update(StoryTable.TABLE_NAME, values, selection, 
				sArgs);	
	}

	/**
	 * Retrieves a story /stories from the database. Always returns an array
	 * list of stories, even if only one story was found or if only one
	 * story matching the search criteria was expected to be found.</br?<br>
	 * 
	 * The story passed into this method is a story holding search criteria,
	 * so any field you would like to include in the search, just set the 
	 * search criteria holding story to it.</br><br>
	 * 
	 * Example Call.</br>
	 * Story story = new Story("The boat", "Bob Week", 
	 * 				"The boat that could", "123ab5");</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * storyMan.insert(story);</br></br>
	 * 
	 * To now search for this story based on its title: </br></br>
	 * 
	 * Story criteria = new Story(null, "The boat", null, null, null);</br>
	 * 
	 * Notice that the first field was null. That is because when making a
	 * search criteria object, you have to use the constructor that lets you
	 * specify the id. This way, you are allowed to manually set any field
	 * you want to be included in the field.</br></br>
	 * 
	 * Let's say you want to change the story's title and update the changes: 
	 * </br>
	 * story.setTitle("new title");</br>
	 * storyMan.update(story);</br>
	 * 
	 * @param newStory
	 * 			Story with changes. The old story's information will be 
	 * 			changed with the new story,s information.
	 */
	@Override
	public ArrayList<Story> retrieve(Story criteria) {
		ArrayList<Story> results = new ArrayList<Story>();
		SQLiteDatabase db = helper.getReadableDatabase();
		setUpSearch(criteria);

		// Querying the database
		Cursor cursor = db.query(StoryTable.TABLE_NAME, projection, selection,
				sArgs, null, null, null);
		
		// Retrieving all the entries
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String storyId = cursor.getString(0);
			String firstchap = cursor.getString(4);
			UUID firstchapUUID = null;
			if (firstchap != null) {
				firstchapUUID = UUID.fromString(firstchap);
			}

			Story story = new Story(
					storyId, 
					cursor.getString(1), // title
					cursor.getString(2), // author
					cursor.getString(3), // description
					firstchapUUID, // first chapter id
					cursor.getString(5) // phoneId
					);
			results.add(story);
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
	private void setUpSearch(Story criteria) {
		sArgs = null;
		projection = new String[]{ 
				StoryTable.COLUMN_NAME_STORY_ID,
				StoryTable.COLUMN_NAME_TITLE, 
				StoryTable.COLUMN_NAME_AUTHOR,
				StoryTable.COLUMN_NAME_DESCRIPTION,
				StoryTable.COLUMN_NAME_FIRST_CHAPTER,
				StoryTable.COLUMN_NAME_PHONE_ID };

		// Setting search criteria
		ArrayList<String> selectionArgs = new ArrayList<String>();
		selection = buildSelectionString(criteria, selectionArgs);

		if (selectionArgs.size() > 0) {
			sArgs = selectionArgs.toArray(new String[selectionArgs.size()]);
		} else {
			selection = null;
		}
	}	
	
	/**
	 * Sets up the ContentValues for inserting or updating the database. This
	 * specifies the columns to be inserted into and what content will be 
	 * going into those columns.
	 * 
	 * @param story
	 * 			All the story's fields will be put into the database.
	 */
	private void setContentValues(Story story) {
		UUID chapterId = story.getFirstChapterId();

		// Insert story
		UUID id = story.getId();
		values = new ContentValues();
		values.put(StoryTable.COLUMN_NAME_STORY_ID, 
				story.getId().toString());
		values.put(StoryTable.COLUMN_NAME_TITLE, story.getTitle());
		values.put(StoryTable.COLUMN_NAME_AUTHOR, story.getAuthor());
		values.put(StoryTable.COLUMN_NAME_DESCRIPTION,
				story.getDescription());
		if (chapterId != null) {
			values.put(StoryTable.COLUMN_NAME_FIRST_CHAPTER, 
				chapterId.toString());
		}
		values.put(StoryTable.COLUMN_NAME_PHONE_ID, story.getPhoneId());
	}
	
	/**
	 * Creates the selection string (a prepared statement) to be used in the
	 * database query. Also creates an array holding the items to be placed in
	 * the ? of the selection, so the selection arguments.
	 * 
	 */
	private String buildSelectionString(Story story, ArrayList<String> sArgs) {
		HashMap<String, String> storyCrit = getSearchCriteria(story);
		splitKeywords(storyCrit, story.getTitle());

		// Setting search criteria
		String selection = "1 LIKE ?";
		sArgs.add("1");
		
		for (String key : storyCrit.keySet()) {
			if (key.equals(StoryTable.COLUMN_NAME_PHONE_ID) 
					&& story.getPhoneId().equals(Story.NOT_AUTHORS)) {
				selection += " AND " + key	+ " NOT LIKE ?"; 
				sArgs.add(phoneId);
			} else {
				String value = storyCrit.get(key);
				selection += " AND " + key + " LIKE ?";
				sArgs.add(value);
			}
		}
		
		return selection;
	}
	
	/**
	 * Returns the information of the story (id, title, author, PhoneId) that
	 * could be used in searching for a story in the database. This information
	 * is returned in a HashMap where the keys are the Story Table column names
	 * you want to include in the search, and the values are the content of
	 * those columns.
	 * 
	 * @return HashMap
	 */
	private HashMap<String, String> getSearchCriteria(Story story) {
		HashMap<String, String> info = new HashMap<String, String>();
		if (story.getId() != null) {
			info.put(StoryTable.COLUMN_NAME_STORY_ID, story.getId().toString());
		}
		if (story.getPhoneId() != null) {
			info.put(StoryTable.COLUMN_NAME_PHONE_ID, phoneId);
		}
		return info;
	}
	
	/**
	 * Helper function for getSearchCriteria(). If searching by keywords in
	 * the story title, this function splits up the title by its keywords
	 * and adds the to the hashmap. The column name for each keyword is 
	 * StoryTable.COLUMN_NAME_TITLE (whatever this macro holds). This macro
	 * is defined in DBcontract.java, so for more information on the setup of
	 * the database, see that class.
	 * 
	 * @param info
	 */
	private void splitKeywords(HashMap<String, String> info, String keywords) {
		List<String> words;
		
		// No title specified in search criteria
		if (keywords == null) {
			return;
		}
		
		words = Arrays.asList(keywords.split("\\s+"));
		
		for (String keyword : words) {
			info.put(StoryTable.COLUMN_NAME_TITLE, "%" + keyword + "%");
		}
	}

	/**
	 * Gets all the stories that are cached from the database. The way it we
	 * determine whether or not it is a cached story or author's own story
	 * is by its phone Id. If the id matches the phone id currently using
	 * the app, the story is considered to be the author's own story.</br></br>
	 * 
	 * Note that the stories are returned in an array list of story objects.
	 * If no cached stories are found, an empty array list will be returned,
	 * not null.</br></br>
	 * 
	 * Example call:</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * ArrayList<Story> stories = StoryManager.getAllCachedStories();</br>
	 */
	public ArrayList<Story> getAllCachedStories() {
		Story criteria = new Story(null, null, null, null, Story.NOT_AUTHORS);
		return retrieve(criteria);
	}

	/**
	 * Gets all the stories that are the author's own from the database. The 
	 * way it we determine whether or not it is a cached story or author's own 
	 * story is by its phone Id. If the id matches the phone id currently using
	 * the app, the story is considered to be the author's own story.</br></br>
	 * 
	 * Note that the stories are returned in an array list of story objects.
	 * If no stories are found, an empty array list will be returned, not null.
	 * </br></br>
	 * 
	 * Example call:</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * ArrayList<Story> stories = StoryManager.getAllAuthorStories();</br>
	 */
	public ArrayList<Story> getAllAuthorStories() {
		Story criteria = new Story(null, null, null, null, phoneId);
		return retrieve(criteria);
	}

	/**
	 * Gets all the stories whose title contains all of the keywords provided. 
	 * I.e. it is a search by keywords in title. It also only searches for
	 * stories that the author has created, not cached (downloaded). </br></br>
	 * 
	 * The  way it we determine whether or not it is a cached story or author's 
	 * own story is by its phone Id. If the id matches the phone id currently
	 * using the app, the story is considered to be the author's own story.
	 * </br></br>
	 * 
	 * Note that the stories are returned in an array list of story objects.
	 * If no stories are found, an empty array list will be returned, not null.
	 * </br></br>
	 * 
	 * Example call:</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * ArrayList<Story> stories = StoryManager.searchAuthorStories("The dog");</br>
	 * 
	 * @param keywords
	 * 			The keywords that appear in the title of the stories we are
	 * 			searching for.
	 */
	public ArrayList<Story> searchAuthorStories(String keywords) {
		Story criteria = new Story(null, keywords, null, null, phoneId);
		return retrieve(criteria);
	}

	/**
	 * Gets all the stories whose title contains all of the keywords provided. 
	 * I.e. it is a search by keywords in title. It also only searches for
	 * stories that the user has cached (downloaded), not the stories the
	 * user has created. </br></br>
	 * 
	 * The  way it we determine whether or not it is a cached story or author's 
	 * own story is by its phone Id. If the id matches the phone id currently
	 * using the app, the story is considered to be the author's own story.
	 * </br></br>
	 * 
	 * Note that the stories are returned in an array list of story objects.
	 * If no stories are found, an empty array list will be returned, not null.
	 * </br></br>
	 * 
	 * Example call:</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * ArrayList<Story> stories = StoryManager.searchCachedStories("The dog");</br>
	 * 
	 * @param keywords
	 * 			The keywords that appear in the title of the stories we are
	 * 			searching for.
	 */
	public ArrayList<Story> searchCachedStories(String title) {
		Story criteria = new Story(null, title, null, null, Story.NOT_AUTHORS);
		return retrieve(criteria);
	}

	/**
	 * Retrieves the story whose id matches the id provided. It expects the id
	 * provided to be a UUID. 
	 * 
	 * Note that if no story id matches the provided id, null is returned.
	 * </br></br>
	 * 
	 * Example call:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * StoryManager storyMan = StoryManager.getInstance(someActivity.this);</br>
	 * Story story = StoryManager.getById(id);</br>
	 * 
	 * @param id
	 * 			Id of the story we are looking for. Must be a UUID.
	 */	
	@Override
	public Story getById(UUID id) {
		ArrayList<Story> result = retrieve(new Story(id, null, null, null, null));
		if (result.size() != 1) {
			return null;
		}
		return result.get(0);
	}
}
