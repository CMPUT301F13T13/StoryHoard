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
package ca.ualberta.cmput301f13t13.storyhoard.dataClasses;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Role: A container to hold story information. This includes id, author, 
 * title, description, the id of the first chapter, a collection of
 * chapters belonging to it, the id of the phone it was created on. Note 
 * that all id's are stored as UUID's.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 */
public class Story {
	private UUID id;
	private String author;
	private String title;
	private String description;
	private UUID firstChapterId;
	private ArrayList<Chapter> chapters;
	private String phoneId;
	public static final String NOT_AUTHORS = "not";

	/**
	 * Initializes a new story object without needing to specify an id as an 
	 * argument. A random unique id will be automatically generated when this 
	 * constructor is used, and it will be a UUID.
	 * 
	 * @param title
	 *            Story title
	 * @param author
	 *            Story author
	 * @param description
	 *            Story description
	 * @param phoneId
	 *            The android id of the phone the story was made on
	 */
	public Story(String title, String author, String description, 
			      String phoneId) {
		this.id = UUID.randomUUID();
		this.author = author;
		this.title = title;
		this.description = description;
		this.phoneId = phoneId;
		this.firstChapterId = null;
		chapters = new ArrayList<Chapter>();
	}

	/**
	 * Initializes a new story object by specifying an id as the first 
	 * parameter. Usually used when making a story object that will be 
	 * holding search criteria since you can specify the id of the story
	 * that you would like to search for.
	 * 
	 * @param id
	 * 			  Story's unique id, must be a UUID.
	 * @param title
	 *            Story title
	 * @param author
	 *            Story author
	 * @param description
	 *            Story description
	 * @param phoneId
	 *            The android Id of the phone the story was made on
	 */
	public Story(UUID id, String title, String author, String description,
			      String phoneId) {
		this.id = id;
		this.author = author;
		this.title = title;
		this.description = description;
		this.phoneId = phoneId;
		this.firstChapterId = null;
		chapters = new ArrayList<Chapter>();
	}

	/**
	 * Initializes a new story object from data taken from the database.
	 * Should only be used by the StoryManager class when making a new story
	 * from the data just retrieved from the database.
	 * 
	 * @param id
	 * 			  Story's unique id, must be a UUID.
	 * @param title
	 *            Story title, must be null or a string.
	 * @param author
	 *            Story author. Must be null or a string.
	 * @param description
	 *            Story description, must be null or a string.
	 * @param chapterId
	 * 			  Id of the story's first chapter, must be a UUID.
	 * @param phoneId
	 *            The android id of the phone the story was made on. Must be
	 *            a string.
	 */
	public Story(String id, String title, String author, String description,
			UUID chapterId, String phoneId) {
		this.id = UUID.fromString(id);
		this.author = author;
		this.title = title;
		this.description = description;
		this.firstChapterId = chapterId;
		this.chapters = new ArrayList<Chapter>();
		this.phoneId = phoneId;
	}

	// GETTERS

	/**
	 * Returns the Id of the story as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * 
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID id = myStory.getId();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * 
	 */
	public UUID getId() {
		return this.id;
	}

	/**
	 * Returns the title of the story as a String (or null is the story title 
	 * was null).</br></br>
	 * 
	 * Example:</br>
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID id = myStory.getId();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be: "title"
	 * 
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Returns the author of the story as a String (or null is the story author
	 * was null).</br></br>
	 * 
	 * Example:</br>
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID id = myStory.getId();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be: "author"
	 * 
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Returns the description of the story as a String (or null is the story 
	 * title was null).</br></br>
	 * 
	 * Example:</br>
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID id = myStory.getId();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be: "desc"
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the chapters of the story as an array list of chapters.
	 * If the story had no chapters, because of the way stories get
	 * initialized, an empty array list will be returned not null. </br></br>
	 * 
	 * Example call: </br>
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * ArrayList<Chapter> chaps = myStory.getChapters();</br></br>
	 * 
	 * chaps is now an empty array list of chapters.
	 * 
	 */
	public ArrayList<Chapter> getChapters() {
		return this.chapters;
	}

	/**
	 * Returns the description of the story as a String (or null is the story 
	 * title was null).</br></br>
	 * 
	 * Example:</br>
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID chapid = UUID.randomUUID();</br>
	 * myStory.setFirstChapterId(chapid);</br>
	 * System.out.println(myStory.getFirstChapterId());</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 */
	public UUID getFirstChapterId() {
		return this.firstChapterId;
	}

	/**
	 * Returns the description of the story as a String (or null is the story 
	 * title was null).</br></br>
	 * 
	 * Example:</br>
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID phoneId = myStory.getPhoneId();</br>
	 * System.out.println(phoneId);</br></br>
	 * 
	 * Output would be: "123"
	 */
	public String getPhoneId() {
		return this.phoneId;
	}

	// SETTERS

	/**
	 * Set the Id of the story. The new Id provided must be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID id = UUID.randomUUID();</br>
	 * meStory.setId(id);</br>
	 * 
	 * @param id
	 * 		New Story id. Must be a UUID or null.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Set the title of the story. Title must be a string.
	 * 
	 * Example call:</br>
	 * 
	 * Story myStory = ("title", "author", "desc", "123");</br>
	 * UUID id = UUID.randomUUID();</br>
	 * meStory.setId(id);</br>
	 * 
	 * @param title
	 * 		The new story title. Must be a string or null.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Set the author of the story.
	 * 
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Set the description of the story.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the first chapter of the story.
	 * 
	 * @param chapterId
	 */
	public void setFirstChapterId(UUID chapterId) {
		firstChapterId = chapterId;
	}

	/**
	 * Set the chapters of the story.
	 * 
	 * @param chapters
	 */
	public void setChapters(ArrayList<Chapter> chapters) {
		this.chapters = chapters;
	}

	/**
	 * Sets the string of the phone id
	 * 
	 * @param phoneId
	 */
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
}
