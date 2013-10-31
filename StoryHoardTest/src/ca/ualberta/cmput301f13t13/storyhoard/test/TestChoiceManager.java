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

package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.ChoiceManager;
import ca.ualberta.cs.c301f13t13.backend.DBHelper;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.gui.AddChoiceActivity;

/**
 * @author Owner 
 *
 */
public class TestChoiceManager extends ActivityInstrumentationTestCase2<AddChoiceActivity> {
	
	/**
	 * @param name
	 */
	public TestChoiceManager(String name) {
		super(AddChoiceActivity.class);
	}
	

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	/**
	 * Tests adding a choice (saving locally to database)
	 */
	public void testSaveLoadChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
		UUID storyId= story.getId();
		Chapter chap1 = new Chapter(storyId,"test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(storyId, chap1.getId(), chap2.getId(), text);
		ChoiceManager cm = ChoiceManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(c, helper);
		try {
			// retrieving story in db that matches mockStory
			ArrayList<Object> choice = cm.retrieve(c, helper);
			assertTrue(choice.size() != 0);
			assertTrue(hasChoice(choice, c));
		} catch(Exception e) {
			fail("Could not read choice: " + e.getStackTrace());
		}
	}
	public Boolean hasChoice(ArrayList<Object> objs, Choice  choice) {
		for (Object object : objs) {
		    Choice newChoice = (Choice) object;
		    if (newChoice.getId().equals(choice.getId())) {
		    	return true;
		    }
		}		
		return false;
	}
	
	/** 
	 * Tests loading and editing a choice.
	 */
	public void testEditChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
		UUID storyId= story.getId();
		Chapter chap1 = new Chapter(storyId,"test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(storyId, chap1.getId(), chap2.getId(), text);
		ChoiceManager cm = ChoiceManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		cm.insert(c, helper);
		Choice newChoice = c;
		newChoice.setText("new choice text mrawr");
		cm.update(c,helper);
		// make sure you can find new story
		ArrayList<Object>	mockChoice = cm.retrieve(newChoice, helper);
				assertTrue(mockChoice.size() != 0);
				assertTrue(hasChoice(mockChoice, c));
				
				// make sure old version no longer exists
				mockChoice = cm.retrieve(c, helper);
				assertTrue(mockChoice.size() == 0);
	}	
}
