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
import java.util.UUID;


/**
 * Interface for storing, updating, and retrieving objects locally 
 * (to the database). The classes that implement this interface must be in
 * charge of opening a connection to the database and interacting with it.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see StoryManager
 * @see ChapterManager
 * @see ChoiceManager
 * @see MediaManager
 */
public abstract class StoringManager<A> {

	public Boolean existsLocally(UUID id) {
		A object = getById(id);
		if (object == null) {
			return false;
		}
		return true;
	}
	
	public void sync(A object, UUID id) {
		if (existsLocally(id)) {
			update(object);
		} else {
			insert(object);
		}	
	}
	
	/**
	 * Inserts an object into the database.
	 * 
	 * @param object
	 */
	public abstract void insert(A object);

	/**
	 * Retrieves an object(s) from the database.
	 * 
	 * @param criteria
	 * 
	 * @return objects
	 */
	public abstract ArrayList<A> retrieve(A criteria);

	/**
	 * Updates an object in the database.
	 * 
	 * @param newObject
	 */
	public abstract void update(A newObject);
	

	public void remove(UUID objId) {
		// hook, optional implementation for this iteration
	}
	
	public abstract A getById(UUID id);
	
	public abstract String setSearchCriteria(A object, ArrayList<String> sArgs);
}
