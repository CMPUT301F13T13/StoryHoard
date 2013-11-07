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

import android.content.Context;

/**
 * Role: Creates the correct object from the classes that implement the
 * StoringManager interface.
 * 
 * Design Pattern: Factory Class
 * 
 * @author Stephanie Gil
 * 
 */
public class ManagerFactory {
	private Context context;

	protected ManagerFactory(Context context) {
		this.context = context;
	}

	/**
	 * Depending on the type of StoringManager needed, returns the correct
	 * singleton object.
	 * 
	 * @param type
	 * @return
	 */
	public StoringManager getStoringManager(ObjectType type) {
		switch (type) {
		case PUBLISHED_STORY:
			return ServerManager.getInstance();
		case CHAPTER:
			return ChapterManager.getInstance(context);
		case CHOICE:
			return ChoiceManager.getInstance(context);
		case MEDIA:
			return MediaManager.getInstance(context);
		default: // cached or created story
			return StoryManager.getInstance(context);
		}
	}

}
