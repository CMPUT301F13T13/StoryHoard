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
 * @see ServerManager
 */
public interface StoringManager<A> {

	/**
	 * Inserts an object into the database.
	 * 
	 * @param object
	 */
	public void insert(A object);

	/**
	 * Retrieves an object(s) from the database.
	 * 
	 * @param criteria
	 * 
	 * @return objects
	 */
	public ArrayList<A> retrieve(A criteria);

	/**
	 * Updates an object in the database.
	 * 
	 * @param newObject
	 */
	public void update(A newObject);

	/**
	 * Builds the selection string and the selection arguments to be used in a
	 * retrieval query. Only applicable for the database queries.
	 * 
	 * @param object
	 * @param sArgs
	 * @return selectionString
	 */
	public String setSearchCriteria(A object, ArrayList<String> sArgs);

	public void remove(A object);
}
