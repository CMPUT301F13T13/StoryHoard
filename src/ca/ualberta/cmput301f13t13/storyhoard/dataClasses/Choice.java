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

import java.util.UUID;

/**
 * Role: A container to hold choice information. This includes id, chapteridto,
 * chapteridfrom, and text.
 * 
 * @author Ashley Brown
 * 
 */

public class Choice {
	private UUID id;
	private UUID currentChapter;
	private UUID nextChapter;
	private String text;

	/**
	 * Initializes a new choice object with no id.
	 * Example:</br>
	 * To search for a choice whose text is "turn left" and has the chapters "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * and "5231b533-ba17-4787-98a3-f2df37de2aD8", construct the 
	 * Choice criteria holder like this:</br>
	 * Choice criteria = new Choice( "the boss", "5231b533-ba17-4787-98a3-f2df37de2aD7", "5231b533-ba17-4787-98a3-f2df37de2aD8");</br></br>
	 * 
	 * To search for a choice whose id is 
	 * "5231b533-ba17-4787-98a3-f2df37de2aA7",nd has the chapters "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * and "5231b533-ba17-4787-98a3-f2df37de2aD8" build the choice criteria 
	 * holder like this:</br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aA7");</br>
	 * 
	 * Choice criteria = new Choice(id, "5231b533-ba17-4787-98a3-f2df37de2aA7", "5231b533-ba17-4787-98a3-f2df37de2aA8");
	 * 
	 * @param chapterIdFrom
	 *            The id of the current chapter being read
	 * @param chapterIdTo
	 *            The id of the chapter the choice links to
	 * @param text
	 * 				The text of the choice
	 * 
	 */
	public Choice(UUID chapterIdFrom, UUID chapterIdTo, String text) {
		this.id = UUID.randomUUID();
		this.currentChapter = chapterIdFrom;
		this.nextChapter = chapterIdTo;
		this.text = text;
	}

	/**
	 * Initializes a new choice object with a UUID id (needed for making a new
	 * choice after retrieving data from the database).
	 * 
	 * @param id
	 * 			The Id of the choice
	 * @param chapterIdFrom
	 *            The id of the current chapter being read
	 * @param chapterIdTo
	 *            The id of the chapter the choice links to
	 * @param text
	 * 				The text of the choice
	 */

	public Choice(UUID id, UUID chapterIdFrom, UUID chapterIdTo, String text) {
		this.id = id;
		this.currentChapter = chapterIdFrom;
		this.nextChapter = chapterIdTo;
		this.text = text;
	}

	/**
	 * Initializes a new choice object that will hold search criteria. For this
	 * reason, only the id, story id, and the id of the chapter the choice
	 * belongs to are needed.
	 * 
	  * @param id
	 * 			The Id of the choice
	 * @param chapterIdFrom
	 *            The id of the current chapter being read
	 * @param chapterIdTo
	 *            The id of the chapter the choice links to
	 */
	public Choice(UUID id, UUID chapterIdFrom) {
		this.id = id;
		this.currentChapter = chapterIdFrom;
		this.nextChapter = null;
		this.text = null;
	}

	// SETTERS
	
	/**
	 * Set the Id of the choice. The new Id provided must be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * UUID id = UUID.randomUUID();</br>
	 * mockChoice.setId(id);</br>
	 * 
	 * @param id
	 * 			New choice id. Must be a UUID or null.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Set the Id of the chapter the choice is in. The new Id provided must be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * UUID id = UUID.randomUUID();</br>
	 * mockChoice.setCurrentChapter(id);</br>
	 * 
	 * @param chapterIdFrom
	 */
	public void setCurrentChapter(UUID chapterIdFrom) {
		this.currentChapter = chapterIdFrom;
	}

	/**
	 * Set the Id of the chapter the choice is going to. The new Id provided must be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * UUID id = UUID.randomUUID();</br>
	 * mockChoice.setNextChapter(id);</br>
	 * 
	 * @param chapterIdFrom
	 */
	public void setNextChapter(UUID uuid) {
		this.nextChapter = uuid;
	}

	/**
	 * Set the text of the choice.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * String text = "working";</br>
	 * mockChoice.setText(text);</br>
	 * 
	 * @param setText
	 */
	public void setText(String text) {
		this.text = text;
	}

	// GETTERS
	
	/**
	 * Returns the Id of the choice as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * UUID id = mockChoice.getId();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * 
	 *  @return Id
	 * 
	 */
	public UUID getId() {
		return this.id;
	}

	/**
	 * Returns the Id of the chapter the choice  is in as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * UUID id = mockChoice.CurrentChapter();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * 
	 * @return currentChapterId
	 */
	public UUID getCurrentChapter() {
		return this.currentChapter;
	}

	/**
	 * Returns the Id of the chapter the choice  is going to as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * UUID id = mockChoice.getNextChapter();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * 
	 * @return nextChapterId
	 */
	public UUID getNextChapter() {
		return this.nextChapter;
	}

	/**
	 *Returns the text of the choice. </br></br>
	 * 
	 * Example:</br>
	 * 
	 * Choice mockChoice= (chapter1.getId(), chapter2.getId(), "turn around");</br>
	 * String text = mockChoice.getText();</br>
	 * System.out.println(text);</br></br>
	 * 
	 * Output would be: "turn around"
	 * 
	 * @return text
	 */
	public String getText() {
		return this.text;
	}
}
