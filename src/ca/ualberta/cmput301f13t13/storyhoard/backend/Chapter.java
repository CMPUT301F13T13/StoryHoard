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

package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract.ChapterTable;

/**
 * Role: A container to hold chapter information. This includes id, story id,
 * text, choices illustrations and photos
 * 
 * @author Ashley Brown
 * @author Stephanie Gil
 * 
 */
public class Chapter extends StoryPart {
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
		if (randomChoice != null) {
			this.randomChoice = Boolean.valueOf(randomChoice);
		}
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

	// OTHER METHODS

	/**
	 * Adds a choice to the chapter.
	 * 
	 * @param choice
	 */
	public void addChoice(Choice choice) {
		choices.add(choice);
	}

	/**
	 * Adds a photo to the chapter
	 * 
	 * @param photo
	 */
	public void addPhoto(Media photo) {
		photos.add(photo);
	}

	/**
	 * Adds an illustration to the chapter.
	 * 
	 * @param i
	 */
	public void addIllustration(Media i) {
		illustrations.add(i);
	}

	/**
	 * Returns the information of the chapter (id, storyId) that could be used
	 * in searching for a chapter in the database. This information is returned
	 * in a HashMap where the keys are the corresponding Chapter Table column
	 * names.
	 * 
	 * @return HashMap
	 */
	public HashMap<String, String> getSearchCriteria() {
		HashMap<String, String> info = new HashMap<String, String>();

		if (id != null) {
			info.put(ChapterTable.COLUMN_NAME_CHAPTER_ID, id.toString());
		}

		if (storyId != null) {
			info.put(ChapterTable.COLUMN_NAME_STORY_ID, storyId.toString());
		}

		return info;
	}

	/**
	 * Updates itself in the database
	 */
	public void updateSelf(Context context) {
		ChapterManager cm = ChapterManager.getInstance(context);
		cm.update(this);
		for (Media photo : photos) {
			photo.updateSelf(context);
		}
		for (Media ill : illustrations) {
			ill.updateSelf(context);
		}
		for (Choice choice : choices) {
			choice.updateSelf(context);
		}
	}

	/**
	 * Adds itself to the database
	 */
	public void addSelf(Context context) {
		ChapterManager cm = ChapterManager.getInstance(context);
		cm.insert(this);
		for (Media photo : photos) {
			photo.addSelf(context);
		}
		for (Media ill : illustrations) {
			ill.addSelf(context);
		}
		for (Choice choice : choices) {
			choice.addSelf(context);
		}
	}

	/**
	 * Gets the full content (choices + media) of the chapter and sets
	 * the corresponding fields.
	 */
	@Override
	public void setFullContent(Context context) {
		ChapterManager chapm = ChapterManager.getInstance(context);
		ArrayList<Object> objs = chapm.retrieve(this);
		Chapter self = (Chapter) objs.get(0);
		
		storyId = self.getStoryId();
		text = self.getText();
		randomChoice = self.hasRandomChoice();
		
		// Get all its choices
		Choice criteria = new Choice(null, getId());
		ChoiceManager cm = ChoiceManager.getInstance(context);
		ArrayList<Object> objects = cm.retrieve(criteria);
		ArrayList<Choice> choices = Utilities.objectsToChoices(objects);
		
		setChoices(choices);
		
		// Get all its illustrations
		MediaManager mm = MediaManager.getInstance(context);
		Media mCriteria = new Media(null, getId(), null, Media.ILLUSTRATION);
		objects = mm.retrieve(mCriteria);
		ArrayList<Media> illustrations = Utilities.objectsToMedia(objects);
		
		setIllustrations(illustrations);
		
		// Get all its photos
		mCriteria = new Media(null, getId(), null, Media.PHOTO);
		objects = mm.retrieve(mCriteria);
		ArrayList<Media> photos = Utilities.objectsToMedia(objects);
		
		setPhotos(photos);	
	}
}
