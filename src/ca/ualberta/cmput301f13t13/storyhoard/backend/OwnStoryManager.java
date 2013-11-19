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

package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.ArrayList;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract.OwnStoryTable;

/**
 * Role: Interacts with the database to store, update, and retrieve story
 * objects. It implements the StoringManager interface.
 * 
 * </br>
 * Design Pattern: Singleton
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Story
 * @see StoringManager
 */
public class OwnStoryManager extends StoryManagerHelper implements StoringManager {
	private static DBHelper helper = null;
	private static OwnStoryManager self = null;

	/**
	 * Initializes a new OwnStoryManager object.
	 */
	protected OwnStoryManager(Context context) {
		helper = DBHelper.getInstance(context);
	}

	/**
	 * Returns an instance of a OwnStoryManager. Used to implement the singleton
	 * design pattern.
	 * 
	 * @param context
	 * @return OwnStoryManager
	 */
	public static OwnStoryManager getInstance(Context context) {
		if (self == null) {
			self = new OwnStoryManager(context);
		}
		return self;
	}

	/**
	 * Saves a new story locally (in the database). You can insert
	 * a story that has any field empty except for its ID.
	 * 
	 * </br> Example Call.
	 * </br> Story story = new Story("The boat", "Bob Week", 
	 * 				"The boat that could", phoneId);
	 * </br> OwnStoryManager sm = new OwnStoryManager.getInstance();
	 * </br> sm.insert(story);
	 */
	@Override
	public void insert(Object object) {
		super.insert(object, OwnStoryTable.TABLE_NAME, helper);

	}

	/**
	 * Updates a story already in the database.
	 * 
	 * @param newObject
	 *            Contains the changes to the object.
	 */
	@Override
	public void update(Object newObject) {
		super.update(newObject, OwnStoryTable.TABLE_NAME, helper);
	}

	/**
	 * Retrieves a story /stories from the database.
	 * 
	 * @param criteria
	 *            Holds the search criteria.
	 */
	@Override
	public ArrayList<Object> retrieve(Object criteria) {
		return super.retrieve(criteria, OwnStoryTable.TABLE_NAME, helper);
	}
}
