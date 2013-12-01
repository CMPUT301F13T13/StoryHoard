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

package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;

/**
* Role: Responsible for manipulating the current choice model. It keeps a 
* reference to the current choice model as a field and updates that whenever 
* the user makes changes. This controller however does not push changes to
* the database. Since a choice is part of a chapter, then it will get added
* on to the chapter it belongs to, and when the chapter's changes are pushed
* to the database, the choices it contains are are also saved /updated in the
* database. </br></br>
* 
* Design Pattern: This class is a singleton, so only one instance of it will
* ever exist. use getInstance() to retrieve that instance, not the 
* constructor.
*/
public class ChoiceController {
	private static Choice choice;
	private static ChoiceController self = null;
	
	protected ChoiceController(Context context) {
		choice = new Choice(null, null, "");  // blank choice
	}
	
	/**
	 * Returns an instance of a ChoiceController. The same instance is always 
	 * returned since it is a singleton. This is the method every other class
	 * should be using to access the ChoiceController. Note that this method
	 * must be called statically. </br></br>
	 * 
	 * Example call: </br>
	 * ChoiceController control = ChoiceController.getInstance(someActivity.this);
	 * 
	 * @param context	
	 * 			Could be an activity or application context.
	 */
	public static ChoiceController getInstance(Context context) {
		if (self == null) {
			self = new ChoiceController(context);
		}
		return self;
	}
	
	/**
	 * Sets the current choice model as the choice field of the class. Now any
	 * modifications needed to be done by the user will be done using methods
	 * in this class that update the choice field. </br></br>
	 * 
	 * Example Call: </br>
	 * Choice myChoice = new Choice(UUID.randomUUID(), "choice text"); </br>
	 * ChoiceController choiceCon = ChoiceController.getInstance(someActivity.this);
	 * choiceCon.setCurrChoice(myChoice);
	 * 
	 * @param aChoice
	 */
	public void setCurrChoice(Choice aChoice) {
		choice = aChoice;
	}
	
	/**
	 * Returns the current choice model (whichever choice is the choice field). 
	 * </br></br>
	 * 
	 * Example Call: </br>
	 * ChoiceController choiceCon = ChoiceController.getInstance(someActivity.this);
	 * Choice myChoice = choiceCon.getCurrChoice();
	 * 
	 */
	public Choice getCurrChoice() {
		return choice;
	}
	
	/**
	 * Edits the choice model's text. No view is allowed to do this
	 * directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Choice myChoice = new Choice(UUID.randomUUID(), "chap text"); </br>
	 * ChoiceController control = ChoiceController.getInstance(someActivity.this); </br>
	 * control.setCurrChoice(myChoice);</br></br>
	 * String text = "new text"; </br>
	 * control.editText(text);
	 * 
	 * @param text
	 * 			String that you would like to set as the choice's text.
	 */
	public void editText(String text) {
		choice.setText(text);
	}
	
	/**
	 * Edits the choice model's next chapter id (the id of the chapter that the
	 * choice will link to when selected). No view is allowed to do this
	 * directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Choice myChoice = new Choice(UUID.randomUUID(), "chap text"); </br>
	 * ChoiceController control = ChoiceController.getInstance(someActivity.this); </br>
	 * control.setCurrChoice(myChoice);</br></br>
	 * control.editChapterTo(UUID.randomUUID());
	 * 
	 * @param id
	 * 			Id of the chapter you now want the choice to link to. Must be
	 * 			a UUID.
	 */	
	public void editChapterTo(UUID id) {
		choice.setNextChapter(id);
	}
}