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
 */

package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

/**
 * @author sgil
 *
 */
public class Utilities {
	
	/**
	 * Takes an array of objects and converts them all to Stories.
	 * @param objects
	 * @return
	 */
	public static ArrayList<Story> objectsToStories(ArrayList<Object> objects) {
		ArrayList<Story> stories = new ArrayList<Story>();
		
		for (Object obj : objects) {
			stories.add((Story) obj);
		}
		
		return stories;
	}
	
	/**
	 * Takes an array of objects and converts them all to Chapters.
	 * @param objects
	 * @return
	 */
	public static ArrayList<Chapter> objectsToChapters(ArrayList<Object> objects) {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		
		for (Object obj : objects) {
			chapters.add((Chapter) obj);
		}
		
		return chapters;
	}	

	/**
	 * Takes an array of objects and converts them all to Choices.
	 * @param objects
	 * @return
	 */
	public static ArrayList<Choice> objectsToChoices(ArrayList<Object> objects) {
		ArrayList<Choice> choices = new ArrayList<Choice>();
		
		for (Object obj : objects) {
			choices.add((Choice) obj);
		}
		
		return choices;
	}		
	
}
