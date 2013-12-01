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

package ca.ualberta.cmput301f13t13.storyhoard.dataClasses;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Role: A container to hold chapter information. This includes id, story id,
 * text, choices illustrations and photos
 * 
 * @author Ashley Brown
 * @author Stephanie Gil
 * 
 */
public class Chapter {
	private UUID id;
	private UUID storyId;
	private String text;
	private Boolean randomChoice;
	private ArrayList<Choice> choices;
	private ArrayList<Media> illustrations;
	private ArrayList<Media> photos;

	/**
	 * Initializes a new chapter object with no id.
	 * 
	 * @param storyId
	 *            The id of the story the chapter is from
	 * @param text
	 *            The chapter text that the user will be reading
	 */
	public Chapter(UUID storyId, String text) {
		this.text = text;
		this.storyId = storyId;
		this.id = UUID.randomUUID();
		this.randomChoice = false;
		choices = new ArrayList<Choice>();
		illustrations = new ArrayList<Media>();
		photos = new ArrayList<Media>();
	}

	/**
	 * Initialize a new chapter with an id. Can also be
	 * used to create a chapter that will serve to hold search criteria.
	 * 
	 * @param id
	 *            The unique id of the chapter
	 * @param storyId
	 *            The id of the story the chapter is from
	 * @param text
	 *            The chapter text that the user will be reading
	 */
	public Chapter(UUID id, UUID storyId, String text) {
		this.id = id;
		this.text = text;
		this.storyId = storyId;
		this.randomChoice = false;
		illustrations = new ArrayList<Media>();
		photos = new ArrayList<Media>();
		choices = new ArrayList<Choice>();
	}
	/**
	 * Initialize a new chapter with an id. Can also be used to make a chapter
	 * from the chapter information retrieved from the database. Can also be
	 * used to create a choice that will serve to hold search criteria.
	 * 
	 * @param id
	 *            The unique id of the chapter
	 * @param storyId
	 *            The id of the story the chapter is from
	 * @param text
	 *            The chapter text that the user will be reading
	 * @param randomChoice
	 *            A flag to indicate the random choice button 
	 */
	public Chapter(UUID id, UUID storyId, String text, Boolean randomChoice) {
		this.id = id;
		this.text = text;
		this.storyId = storyId;
		this.randomChoice = randomChoice;
		illustrations = new ArrayList<Media>();
		photos = new ArrayList<Media>();
		choices = new ArrayList<Choice>();
	}

	// Getters

	/**
	 * Returns the Id of the chapter.
	 * 
	 * @return id
	 */
	public UUID getId() {
		return this.id;
	}

	/**
	 * Returns the story Id of the chapter.
	 * 
	 * @return story id
	 */
	public UUID getStoryId() {
		return this.storyId;
	}

	/**
	 * Returns the text of the chapter.
	 * 
	 * @return text
	 */
	public String getText() {

		return this.text;
	}
	/**
	 * Returns the random choice flag of the chapter.
	 * 
	 * @return randomChoice
	 */
	public Boolean hasRandomChoice() {

		return this.randomChoice;
	}

	/**
	 * Returns the choices of the chapter.
	 * 
	 * @return choices
	 */
	public ArrayList<Choice> getChoices() {
		return this.choices;
	}

	/**
	 * Returns the photos of the chapter.
	 * 
	 * @return photos
	 */
	public ArrayList<Media> getPhotos() {
		return this.photos;
	}

	/**
	 * Returns the illustrations of the chapter.
	 * 
	 * @return illustrations
	 */
	public ArrayList<Media> getIllustrations() {
		return this.illustrations;
	}

	// SETTERS

	/**
	 * Sets the Id of the chapter.
	 * 
	 * @param id
	 *            The unique Id of the chapter
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the story Id of the chapter.
	 * 
	 * @param id
	 *            The id of the story the chapter is in
	 */
	public void setStoryId(UUID id) {
		this.storyId = id;
	}

	/**
	 * Sets the text of the chapter.
	 * 
	 * @param text
	 *            The text of the chapter that the user will read
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * Sets the random choice flag of the chapter.
	 * 
	 * @param randomChoice
	 *            
	 */
	public void setRandomChoice(Boolean randomChoice) {
		this.randomChoice = randomChoice;
	}

	/**
	 * Sets the choices of the chapter.
	 * 
	 * @param choices
	 *            The choices that link this chapter to another
	 */
	public void setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}

	/**
	 * Sets the photos of the chapter.
	 * 
	 * @param photos
	 */
	public void setPhotos(ArrayList<Media> photos) {
		this.photos = photos;
	}

	/**
	 * Sets the illustrations of the chapter.
	 * 
	 * @param illustrations
	 */
	public void setIllustrations(ArrayList<Media> illustrations) {
		this.illustrations = illustrations;
	}
}
