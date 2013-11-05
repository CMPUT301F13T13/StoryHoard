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

package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

/**
 * Interface for storing objects locally (to the database).
 * 
 * @author Stephanie
 * @author Ashley
 * 
 * @see StoryManager
 * @see ChapterManager
 * @see ChoiceManager
 * @see MediaManager
 */
public interface StoringManager {

	/**
	 * Inserts an object into a database.
	 * 
	 * @param object
	 */
	public void insert(Object object);

	/**
	 * Retrieves an object from the database.
	 * 
	 * @param criteria
	 * 
	 * @return
	 */
	public ArrayList<Object> retrieve(Object criteria);

	/**
	 * Updates an object in the database.
	 * 
	 * @param oldObject
	 * @param newObject
	 */
	public void update(Object newObject);

	/**
	 * Builds the selection string and the selection arguments to be used in a
	 * retrieval query.
	 * 
	 * @param object
	 * @param sArgs
	 * @return
	 */
	public String setSearchCriteria(Object object, ArrayList<String> sArgs);

	// public void delete();
}
