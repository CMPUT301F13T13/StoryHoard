/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChoiceController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Class for testing the functionality of ChoiceController.java
 * 
 * @author sgil
 *
 */
public class TestChoiceController extends ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private ChoiceController choiceCon;
	private ArrayList<Choice> mockChoices;
	private Choice mockChoice;
	private Choice mockChoice2;
	private Choice mockChoice3;
	
	public TestChoiceController() {
		super(ViewBrowseStories.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		choiceCon = ChoiceController.getInstance(getActivity());	
	}

	/**
	 * Tests getting all chapters from a chapter.
	 */
	public void testGetChoicesByChapter() {
		UUID chapId = UUID.randomUUID();
		mockChoice = new Choice(chapId, UUID.randomUUID(), "bob went away");
		choiceCon.insert(mockChoice);
		mockChoice2 = new Choice(chapId, UUID.randomUUID(), "Lily drove");
		choiceCon.insert(mockChoice2);
		mockChoice3 = new Choice(UUID.randomUUID(), UUID.randomUUID(), 
				"Lily drove");
		choiceCon.insert(mockChoice3);

		mockChoices = choiceCon.getChoicesByChapter(chapId);
		assertEquals(mockChoices.size(), 2);		
	}
	
	/**
	 * Tests getting all created choices.
	 */
	public void testGetAll() {
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
		UUID chapId = UUID.randomUUID();
		mockChoice = new Choice(chapId, chapId, "");
		choiceCon.insert(mockChoice);
		mockChoice2 = new Choice(chapId, chapId, "");
		choiceCon.insert(mockChoice2);

		mockChoices = choiceCon.getAll();
		assertEquals(mockChoices.size(), 2);		
	}
	
	/**
	 * Tests inserting, retrieving, and updating a choice.
	 */
	public void testInsertRetrieveUpdate() {
		UUID chapId = UUID.randomUUID();
		mockChoice = new Choice(chapId, UUID.randomUUID(), "bob went away");
		choiceCon.insert(mockChoice);
		
		mockChoices = choiceCon.getChoicesByChapter(chapId);
		assertEquals(mockChoices.size(), 1);
		
		mockChoice2 = mockChoices.get(0);
		mockChoice2.setText("hello");
		choiceCon.update(mockChoice2);
		
		mockChoices = choiceCon.getChoicesByChapter(chapId);
		assertEquals(mockChoices.size(), 1);	
		mockChoice2 = mockChoices.get(0);
		
		assertFalse(mockChoice2.getText().equals(mockChoice.getText()));
	}	
	
	/**
	 * Tests retrieving a random choice from a chapter.
	 */
	public void testRandomChoice() {
		UUID chapId = UUID.randomUUID();
		mockChoice = new Choice(chapId, chapId, "");
		choiceCon.insert(mockChoice);
		mockChoice2 = new Choice(chapId, chapId, "");
		choiceCon.insert(mockChoice2);

		Choice random = choiceCon.getRandomChoice(chapId);
		assertNotNull(random);		
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
	}
	
}
