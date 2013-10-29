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

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cs.c301f13t13.backend.DBContract.StoryTable;

/**
 * @author Stephanie Gil
 *
 */
public class Story implements Serializable {
	private UUID id;
	private String author;
	private String title;
	private String description;
	private UUID firstChapterId;
	private HashMap<UUID, Chapter> chapters;
	private Boolean authorsOwn;

	/**
	 * Initializes a new story object with an id.
	 * 
	 * @param id
	 * @param author
	 * @param title
	 * @param description
	 * @param authorsOwn
	 */
	public Story(UUID id, String title, String author, String description, 
			Boolean authorsOwn) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.description = description;
		this.authorsOwn = authorsOwn;
		chapters = new HashMap<UUID, Chapter>();
	}	

	/**
	 * Initializes a new story object with no id.
	 * 
	 * @param author
	 * @param title
	 * @param description
	 * @param authorsOwn
	 */
	public Story(String title, String author, String description, 
			Boolean authorsOwn) {
		this.id = UUID.randomUUID();
		this.author = author;
		this.title = title;
		this.description = description;
		this.authorsOwn = authorsOwn;
		chapters = new HashMap<UUID, Chapter>();
	}	
	
	/**
	 * Initializes a new story object from a database entry. Only to be used by
	 * the story manager class.
	 * 
	 * @param id
	 * @param author
	 * @param title
	 * @param description
	 */
	protected Story(String id, String title, String author, String description,
			String chapterId, HashMap<UUID, Chapter> chapters, Boolean authorsOwn) {
		this.id = UUID.fromString(id);
		this.author = author;
		this.title = title;
		this.description = description;
		this.firstChapterId = UUID.fromString(chapterId);
		this.chapters = new HashMap<UUID, Chapter>(chapters);
		this.authorsOwn = authorsOwn;
	}	
	
	// GETTERS
	
	/**
	 * Returns the Id of the story.
	 * @return
	 */
	public UUID getId() {
		return this.id;
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
	public HashMap<UUID, Chapter> getChapters() {
		return this.chapters;
	}
	
	/**
	 * Returns the first chapter of the story.
	 * @return firstChapter
	 */
	public UUID getFirstChapterId() {
		return this.firstChapterId;
	}
	
	/**
	 * Returns the chapter matching the chapter id
	 * @return chapter
	 */
	public Chapter getChapter(UUID id) {
		Chapter chap = chapters.get(id);
		return chap;
	}	
	
	/**
	 * Returns boolean indicating whether or not the story was created
	 * by the phone's author or not.
	 * @return
	 */
	public Boolean getAuthorsOwn() {
		return this.authorsOwn;
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
	 * Sets the first chapter of the story.
	 * @param chapter
	 */
	public void setFirstChapterId(UUID chapterId) {
		firstChapterId = chapterId;
	}
	
	/**
	 * Set the chapters of the story.
	 * @param chapters
	 */
	public void setChapters(HashMap<UUID, Chapter> chapters) {
		this.chapters = chapters;
	}
	
	/**
	 * Sets the Boolean indicating whether or not the story
	 * was created by the phone's author.
	 * @param authorsOwn
	 */
	public void setAuthorsOwn(Boolean authorsOwn) {
		this.authorsOwn = authorsOwn;
	}
	
	// Other methods
	
	/**
	 * Adds a chapter onto the story object.
	 */
	public void addChapter(Chapter chapter) {
		chapters.put(chapter.getId(), chapter);
	}
	
	/**
	 * Returns the information of the story (id, title, author, etc)
	 * in a HashMap.
	 * 
	 * @return HashMap
	 */
	public HashMap<String,String> getSearchCriteria() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		info.put(StoryTable.COLUMN_NAME_STORY_ID, id.toString());
		info.put(StoryTable.COLUMN_NAME_TITLE, title);
		info.put(StoryTable.COLUMN_NAME_AUTHOR, author);
		info.put(StoryTable.COLUMN_NAME_DESCRIPTION, description);
		info.put(StoryTable.COLUMN_NAME_FIRST_CHAPTER, firstChapterId.toString());
		info.put(StoryTable.COLUMN_NAME_CREATED, authorsOwn.toString());
		
		return info;
	}
	
	@Override
	public String toString() {
		return "Story [id=" + id + ", author=" + author + ", title=" + title 
				+ ", description=" + description + "]";
	}
}
