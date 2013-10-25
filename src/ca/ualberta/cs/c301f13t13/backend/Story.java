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
 * @author Stephanie Gil
 *
 */
public class Story {
	private String id;
	private String author;
	private String title;
	private String description;
	//private ArrayList<Chapter> chapters;
	
	/**
	 * Initializes a new story object
	 * 
	 * @param id
	 * @param author
	 * @param title
	 * @param description
	 */
	public Story(String id, String author, String title, String description) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.description = description;
	}
	
	/**
	 * Returns the Id of the story.
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Returns the title of the story.
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	
	@Override
	public String toString() {
		return "Story [id=" + id + ", author=" + author + ", title=" + title 
				+ ", description=" + description + "]";
	}
}
