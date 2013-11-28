package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChoiceManager;

public class ChoController {
	private static ChoiceManager choiceMan;
	private static Choice choice;
	private static ChoController self = null;
	
	protected ChoController(Context context) {
		choiceMan = ChoiceManager.getInstance(context);
		choice = new Choice(null, null, "");  // blank choice
	}
	
	public static ChoController getInstance(Context context) {
		if (self == null) {
			self = new ChoController(context);
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
	
	public void pushChangesToDb() {
		choiceMan.syncChoice(choice);
	}	
}