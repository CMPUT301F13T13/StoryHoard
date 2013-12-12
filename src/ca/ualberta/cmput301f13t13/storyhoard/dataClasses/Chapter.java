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
 * text, choices, illustrations and photos. Note that all id's are stored as  
 * UUID's. A chapter will always belong to a specific story, and no chapter 
 * can belong to more than one story.
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
	 * Initializes a new chapter object without needing to specify its id. 
	 * A new Id will be randomly generated once the object is created. By
	 * default, the randomChoice boolean will be set to false, so you would
	 * have to use the setRandomChoice() method to set it to true.
	 * </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * 
	 * @param storyId
	 *            The id of the story the chapter belongs to. Must be a UUID. 
	 * @param text
	 *            The chapter text that the user will be reading. Must be a 
	 *            string.
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
	 * Initialize a new chapter while needing to specify an id. This is used 
	 * to make a chapter object that will be holding search criteria. 
	 * </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), UUID.randomUUID(), "text"); 
	 * </br>
	 * 
	 * 
	 * @param id
	 *            The unique id of the chapter. Must be a UUID.
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
	 * Initialize a new chapter with an id. This is always used when making a  
	 * chapter from data just retrieved from the database. Can also be used to  
	 * make a chapter that will hold search criteria. This constructor also 
	 * expects the randomChoice parameter specifying whether or not a random 
	 * choice should be used when viewing it. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), UUID.randomUUID(), 
	 * 			"text", true); 
	 * 
	 * @param id
	 *            The unique id of the chapter. Must be a UUID.
	 * @param storyId
	 *            The id of the story the chapter is from. Must be a UUID.
	 * @param text
	 *            The chapter text that the user will be reading
	 * @param randomChoice
	 *            A flag to indicate whether or not the chapter should display 
	 *            a random choice when reading it or not.
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
	 * Returns the id of the chapter as a UUID. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * UUID id = chap.getId();
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 */
	public UUID getId() {
		return this.id;
	}

	/**
	 * Returns the story id the media belongs to as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * UUID storyId =  chap.getStoryId();</br>
	 * System.out.println(storyId);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 */
	public UUID getStoryId() {
		return this.storyId;
	}

	/**
	 * Returns the text of the chapter as a string. </br></br>
	 * 
	 * Example:</br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * String text =  chap.getText();</br>
	 * System.out.println(text);</br></br>
	 * 
	 * Output would be: "text"
	 */
	public String getText() {

		return this.text;
	}
	
	/**
	 * Returns true if the chapter should display a random choice, and false 
	 * if it shouldn't. By default, it is set to false. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * Boolean bool = chap.hasRandomChoice(); </br>
	 * System.out.println(bool.toString());</br></br>
	 * 
	 * output would be: "false"
	 */
	public Boolean hasRandomChoice() {

		return this.randomChoice;
	}

	/**
	 * Returns the choices of the chapter as an array list. If the chapter 
	 * has no choices, an empty array list is returned. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * ArrayList<Choice> choices = chap.getChoices(); </br>
	 */
	public ArrayList<Choice> getChoices() {
		return this.choices;
	}

	/**
	 * Returns the photos of the chapter as an array list. If the chapter 
	 * has no photos, an empty array list is returned. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * ArrayList<Media> choices = chap.getPhotos(); </br>
	 */
	public ArrayList<Media> getPhotos() {
		return this.photos;
	}

	/**
	 * Returns the illustrations of the chapter as an array list. If the 
	 * chapter has no illustrations, an empty array list is returned. 
	 * </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * ArrayList<Media> choices = chap.getIllustrations(); </br>
	 */
	public ArrayList<Media> getIllustrations() {
		return this.illustrations;
	}

	// SETTERS

	/**
	 * Sets the Id of the chapter. The new Id provided must be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * UUID id = UUID.randomUUID();</br>
	 * chap.setId(id);</br>
	 * 
	 * @param id
	 * 			New chapter id. Must be a UUID or null.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the Id of the story the chapter belongs to. The new Id provided must 
	 * be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Chapter chap = new Chapter(UUID.randomUUID(), "text"); </br>
	 * UUID id = UUID.randomUUID();</br>
	 * chap.setStoryId(id);</br>
	 * 
	 * @param id
	 * 			New story id. Must be a UUID or null.
	 */
	public void setStoryId(UUID id) {
		this.storyId = id;
	}

	/**
	 * Sets the text of the chapter, which is what the user will read when they 
	 * are viewing a chapter. Example "on a dark cold night, you found a 
	 * house." </br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Chapter chap = new Chapter(UUID.randomUUID(), ""); </br>
	 * chap.setText("And so the man took the shovel with him to finish it);</br>
	 * 
	 * @param text
	 *            The text of the chapter that the user will read
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * Sets the random choice flag of the chapter. Will either be true or 
	 * false. By default, on the creation of a chapter it is set to false. 
	 * If it is true, a random choice will be displayed when viewing a 
	 * chapter. This choice will link to a chapter one of the other choices 
	 * displayed links to, and will decide which one to link to randomly. 
	 * </br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Chapter chap = new Chapter(UUID.randomUUID(), ""); </br>
	 * chap.setText("And so the man took the shovel with him to finish it);</br>
	 * 
	 * @param randomChoice
	 *         	Boolean to decided whether or not to display a random choice.   
	 */
	public void setRandomChoice(Boolean randomChoice) {
		this.randomChoice = randomChoice;
	}

	/**
	 * Sets the choices of the chapter (where the choices are in an array 
	 * list). It should never be passed in null. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), ""); </br>
	 * chap.setChoices(new ArrayList<Choice>()); </br>
	 * 
	 * @param choices
	 *            The choices that link this chapter to another. This must be
	 *            in the form of an ArrayList of choices and should never be
	 *            null.
	 */
	public void setChoices(ArrayList<Choice> choices) {
		this.choices = choices;
	}

	/**
	 * Sets the photos of the chapter (where the photos are in an array 
	 * list). It should never be passed in null. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), ""); </br>
	 * chap.setPhotos(new ArrayList<Media>()); </br>
	 * 
	 * @param photos
	 *            The photos of the chapter (can be posted by other users 
	 *            as well, may contain text comments, and can only be added 
	 *            when reading a chapter).
	 */
	public void setPhotos(ArrayList<Media> photos) {
		this.photos = photos;
	}

	/**
	 * Sets the illustrations of the chapter (where the photos are in an array 
	 * list). It should never be passed in null. </br></br>
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), ""); </br>
	 * chap.setillustrations(new ArrayList<Media>()); </br>
	 * 
	 * @param illustrations
	 *            The illustrations of the chapter (don't contain text 
	 *            comments and are only added when editing a chapter). 
	 */
	public void setIllustrations(ArrayList<Media> illustrations) {
		this.illustrations = illustrations;
	}
}
