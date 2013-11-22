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

import java.util.HashMap;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract.ChoiceTable;

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
	 * 
	 * @param chapterIdFrom
	 *            The id of the current chapter being read
	 * @param chapterIdTo
	 *            The id of the chapter the choice links to
	 * @param text
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
	 * @param chapterIdFrom
	 * @param chapterIdTo
	 * @param text
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
	 * @param chapterIdFrom
	 * @param chapterIdTo 
	 */
	public Choice(UUID id, UUID chapterIdFrom) {
		this.id = id;
		this.currentChapter = chapterIdFrom;
		this.nextChapter = null;
		this.text = null;
	}

	// SETTERS
	
	/**
	 * Set the Id of the choice.
	 * 
	 * @param id
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the chapterIdFrom of the choice.
	 * 
	 * @param chapterIdFrom
	 */
	public void setCurrentChapter(UUID chapterIdFrom) {
		this.currentChapter = chapterIdFrom;
	}

	/**
	 * Sets the chapterIdTo of the choice.
	 * 
	 * @param uuid
	 */
	public void setNextChapter(UUID uuid) {
		this.nextChapter = uuid;
	}

	/**
	 * Sets the text of the choice.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	// GETTERS
	
	/**
	 * Returns the Id of the choice.
	 * 
	 * @return id
	 */
	public UUID getId() {
		return this.id;
	}

	/**
	 * Returns the chapterIdFrom of the choice.
	 * 
	 * @return currentChapterId
	 */
	public UUID getCurrentChapter() {
		return this.currentChapter;
	}

	/**
	 * Returns the chapterIdTo of the choice.
	 * 
	 * @return nextChapterId
	 */
	public UUID getNextChapter() {
		return this.nextChapter;
	}

	/**
	 * Returns the text of the choice.
	 * 
	 * @return text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Returns the information of the choice (id, chapterIdFrom, chapterIdTo)
	 * that could be used in searching for a choice in the database. This
	 * information is returned in a HashMap where the keys are the 
	 * corresponding Choice Table column names.
	 * 
	 * @return HashMap
	 */
	public HashMap<String, String> getSearchCriteria() {
		HashMap<String, String> info = new HashMap<String, String>();

		if (id != null) {
			info.put(ChoiceTable.COLUMN_NAME_CHOICE_ID, id.toString());
		}

		if (currentChapter != null) {
			info.put(ChoiceTable.COLUMN_NAME_CURR_CHAPTER,
					currentChapter.toString());
		}
		
		if (nextChapter != null) {
			info.put(ChoiceTable.COLUMN_NAME_NEXT_CHAPTER,
					nextChapter.toString());
		}

		return info;
	}

	public void updateSelf(Context context) {
		ChoiceManager cm = ChoiceManager.getInstance(context);
		cm.update(this);
	}

	public void addSelf(Context context) {
		ChoiceManager cm = ChoiceManager.getInstance(context);
		cm.insert(this);
	}
	
	public void removeSelf(Context context) {
		ChoiceManager cm = ChoiceManager.getInstance(context);
		cm.remove(this);
	}
}
