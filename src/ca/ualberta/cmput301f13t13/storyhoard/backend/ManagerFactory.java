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

import android.content.Context;

/**
 * Role: Creates the correct object from the classes that implement the
 * StoringManager interface.
 * 
 * </br>
 * Design Pattern: Factory Class
 * 
 * @author Stephanie Gil
 * 
 */
public class ManagerFactory {
	private Context context;

	/**
	 * Instantiates a new ManagerFactory
	 * 
	 * @param context
	 */
	protected ManagerFactory(Context context) {
		this.context = context;
	}

	/**
	 * Depending on the type of StoringManager needed, returns the correct
	 * singleton object. There are five different types of objects that could
	 * be returned.
	 * 
	 * </br>
	 * <ul>
	 * <li> A ServerManager object (deals with all things server related).</li>
	 * <li> A ChapterManager object, deals with manipulating chapters. </li>
	 * <li> A ServerManager object (deals with all things server related).</li>
	 * <li> A ChapterManager object, deals with manipulating chapters. </li>
	 * <li> A ServerManager object (deals with all things server related).</li>
	 * </ul>
	 * 
	 * </br> 
	 * Eg. If you are wanting to manipulate a chapter object, you would 
	 * 	   call: </br>
	 * 
	 * ManagerFactory mf = new ManagerFactory();
	 * </br>
	 * StoringManager sm = mf.getStoringManager(CHAPTER);
	 * </br>
	 * You can now use sm as a chapterManager, although because it is a 
	 * StoringManager right now, you can only use methods defined in 
	 * the StoringManager interface.
	 * 
	 * @param type
	 * 			Will either be PUBLISHED_STORY, CHAPTER, CHOICE, MEDIA
	 * 			CREATED_STORY, or CACHED_STORY.
	 * @return StoringManager
	 */
	public StoringManager getStoringManager(ObjectType type) {
		switch (type) {
		case PUBLISHED_STORY:
			return ServerManager.getInstance();
		case CREATED_STORY:
			return OwnStoryManager.getInstance(context);	
		case CACHED_STORY:
			return CachedStoryManager.getInstance(context);
		case CHAPTER:
			return ChapterManager.getInstance(context);
		case CHOICE:
			return ChoiceManager.getInstance(context);
		case MEDIA:
			return MediaManager.getInstance(context);
		}
		return null;
	}

}
