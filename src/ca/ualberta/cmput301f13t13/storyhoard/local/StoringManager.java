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
 * The types of objects it assumes to be working with will be Story, Chapter, 
 * Choice, or Media.</br></br>
 * 
 * Design Pattern: This class uses the template method design pattern. The  
 * template methods are existsLocally(), and sync(). They both use some 
 * of the other abstract methods that will have to be defined to accomplish 
 * their tasks. </br></br>
 * 
 * For examples of how each method can be called, see the classes listed 
 * below. </br>
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

	/**
	 * Checks to see whether an object with the given id exists in the  
	 * database.
	 * 
	 * @param id
	 */
	public Boolean existsLocally(UUID id) {
		A object = getById(id);
		if (object == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Takes an object and its id and decides, checks whether or not it exists 
	 * in the database already, and if it does, updates it, or else it inserts 
	 * it.
	 * 
	 * @param object
	 * @param id
	 */
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
	 * 			Expects either a Story, Chapter, Choice, or Media.
	 */
	public abstract void insert(A object);

	/**
	 * Retrieves an object(s) from the database given an object of the same 
	 * type holding the desired search criteria.
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
	
	/**
	 * Retrieves an object from the database whose id matches the id 
	 * given. Null is returned if no object exists with that id. 
	 * 
	 * @param id
	 */
	public abstract A getById(UUID id);
	
	/**
	 * Removes an object from the database given its Id (as a UUID). 
	 * This method is a "hook", so it is optional to implement. 
	 * However, the one class that does need to implement it is the media  
	 * Manager class since deleting illustrations is part of the  
	 * functionality of the StoryHoard application. 
	 * 
	 * @param objId
	 */
	public void remove(UUID objId) {
	}
}
