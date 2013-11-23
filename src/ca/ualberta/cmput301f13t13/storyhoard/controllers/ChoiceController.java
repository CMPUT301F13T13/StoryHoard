package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;

public class ChoiceController implements SHController<Choice>{
	private static ChoiceController self = null;   
	private static ChoiceManager choiceMan;

	protected ChoiceController(Context context) {
		choiceMan = ChoiceManager.getInstance(context);
	}
	
	public static ChoiceController getInstance(Context context) {
		if (self == null) {
			self = new ChoiceController(context);
		}
		return self;
	}

	/**
	 * Retrieves a random choice from the choice.
	 * 
	 * @param choiceID
	 *            Id of  the choices that the choice is for.
	 * 
	 * @return a choice
	 */
	public Choice getRandomChoice(UUID chapterId) {
		ArrayList<Choice> choices = getChoicesByChapter(chapterId);
		int max = 0;
		max = choices.size();
		Random rand = new Random(); 
		int num;
		num = rand.nextInt(max);
		Choice choice = choices.get(num);
		choice.setText("I'm feeling lucky...");

		return choice;
	}
	
	public ArrayList<Choice> getChoicesByChapter(UUID chapterId) {
		return choiceMan.retrieve(new Choice(null, chapterId, null, null));		
	}	
	
	@Override
	public ArrayList<Choice> getAll() {
		return choiceMan.retrieve(new Choice(null, null, null, null));
	}
	
	@Override
	public ArrayList<Choice> retrieve(Choice choice) {
		return choiceMan.retrieve(choice);
	}

	@Override
	public void insert(Choice choice) {
		choiceMan.insert(choice);
	}

	@Override
	public void update(Choice choice) {
		choiceMan.update(choice);
	}
}
