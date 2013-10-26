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
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.StoryTable;

/**
 * @author Stephanie Gil
 *
 */
public class Story {
	private UUID id;
	private String author;
	private String title;
	private String description;
	private Chapter firstChapter;
	private ArrayList<Chapter> chapters;
	
	/**
	 * For creating an empty story and then setting specific parameters.
	 * used for placing search criteria.
	 */
	protected Story() {
		this.id = null;
		this.author = "";
		this.title = "";
		this.description = "";
		this.firstChapter = null;
		this.chapters = null;
		
	}
	/**
	 * Initializes a new story object.
	 * 
	 * @param id
	 * @param author
	 * @param title
	 * @param description
	 */
	public Story(String title, String author, String description, Chapter chapter) {
		this.id = UUID.randomUUID();;
		this.author = author;
		this.title = title;
		this.description = description;
		this.firstChapter = chapter;
		chapters.add(firstChapter);
	}
	
	/**
	 * Initializes a new story object from a database entry.
	 * 
	 * @param id
	 * @param author
	 * @param title
	 * @param description
	 */
	public Story(String id, String title, String author, String description,
			Chapter chapter, ArrayList<Chapter> chapters) {
		this.id = UUID.fromString(id);;
		this.author = author;
		this.title = title;
		this.description = description;
		this.firstChapter = chapter;
		this.chapters = chapters;
	}	
	
	// GETTERS
	
	/**
	 * Returns the Id of the story.
	 * @return
	 */
	public String getId() {
		return this.id.toString();
	}
	
	/**
	 * Returns the title of the story.
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Returns the author of the story.
	 * @return
	 */
	public String getAuthor() {
		return this.author;
	}
	
	/**
	 * Returns the description of the story.
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Returns the chapters of the story.
	 * @return chapters
	 */
	public ArrayList<Chapter> getChapters() {
		return this.chapters;
	}
	
	/**
	 * Returns the first chapter of the story.
	 * @return firstChapter
	 */
	public Chapter getFirstChapter() {
		return this.firstChapter;
	}
	
	// SETTERS
	
	/**
	 * Set the Id of the story.
	 * @param id
	 */
	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
	
	/**
	 * Set the title of the story.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Set the author of the story.
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * Set the description of the story.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Set the chapters of the story.
	 * @param chapters
	 */
	public void getChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}
	
	
	/**
	 * Adds a chapter.
	 */
	public void addChapter(Chapter chapter) {
		chapters.add(chapter);
	}
	
	public HashMap<String,String> getInfo() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		if (id == null) {
			info.put("", StoryTable.COLUMN_NAME_STORY_ID);
		} else {
			info.put(id.toString(), StoryTable.COLUMN_NAME_STORY_ID);
		}
		info.put(title, StoryTable.COLUMN_NAME_TITLE);
		info.put(author, StoryTable.COLUMN_NAME_AUTHOR);
		info.put(description, StoryTable.COLUMN_NAME_DESCRIPTION);
		
		return info;
	}
	@Override
	public String toString() {
		return "Story [id=" + id + ", author=" + author + ", title=" + title 
				+ ", description=" + description + "]";
	}
}
