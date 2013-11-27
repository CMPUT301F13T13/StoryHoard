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

package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Role: Interface defining the main operations that can be called to send and
 * retrieve data from the server and database. It has a generic type A, and 
 * every class implementing this interface will define what type A is. </br></br>
 * 
 * ChapterController defines type A as the Chapter class. </br>
 * ChoiceController defines type A as the Choice class. </br>
 * LocalStoryController defines type A as the Story class. </br>
 * ServerStoryController defines type A as the Story class. </br>
 * MediaController defines type A as the Media class. </br></br>
 * 
 * Design Pattern: Singleton </br></br>
 * 
 * For more detailed information on each method in the interface, see the 
 * classes specified below which implement it in this application.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see ChapterController
 * @see ChoiceController
 * @see LocalStoryController
 * @see ServerStoryController
 * @see MediaController
 * 
 */
public interface SHController<A> {

	/**
	 * Returns all types of object A available in the database or the server.
	 * 
	 * @return 
	 */
	public ArrayList<A> getAll();
	
	/**
	 * Inserts an object into the database or server.
	 * 
	 * @param object
	 */
	public void insert(A object);
	
	/**
	 * Updates and an object. The object passed contains the new data to be
	 * updated with.
	 * 
	 * @param object
	 */
	public void update(A object);
	
	/**
	 * Removes an object from the database or server (depending on which 
	 * class is implementing this interface). An id must be given, and it
	 * must be of type UUID.
	 * 
	 * @param objId
	 */
	public void remove(UUID objId);
}
