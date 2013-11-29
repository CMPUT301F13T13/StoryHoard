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