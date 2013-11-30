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

public class ChoiceController {
	private static Choice choice;
	private static ChoiceController self = null;
	
	protected ChoiceController(Context context) {
		choice = new Choice(null, null, "");  // blank choice
	}
	
	public static ChoiceController getInstance(Context context) {
		if (self == null) {
			self = new ChoiceController(context);
		}
		return self;
	}
	
	public void setCurrChoice(Choice aChoice) {
		choice = aChoice;
	}
	
	public Choice getCurrChoice() {
		return choice;
	}
	
	public void editText(String text) {
		choice.setText(text);
	}
	
	public void editChapterTo(UUID id) {
		choice.setNextChapter(id);
	}
}