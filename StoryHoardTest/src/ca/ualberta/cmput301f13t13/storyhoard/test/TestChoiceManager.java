/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestChoiceManager extends ActivityInstrumentationTestCase2<StoryHoardActivity> {

	/**
	 * @param name
	 */
	public TestChoiceManager(String name) {
		super(StoryHoardActivity.class);
	}
	

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	/**
	 * Tests adding a choice (saving to database)
	 */
	public void testAddChoice() {
		ChoiceManager cm = new Choicemanager();
		Choice mockChoice = new Choice();
		cm.addChoice(mockChoice);
	}
	
	/** 
	 * Tests editing a choice.
	 */
	public void testEditChoice() {
		ChoiceManager cm = new Choicemanager();
		Choice choice = new Choice();
		cm.addChoice(choice);
		Choice newChoice = cm.loadChoice(choice.getId());
		newChoice.setText("new choice text mrawr");
		cm.updateChoice(newChoice);
	}	
}
