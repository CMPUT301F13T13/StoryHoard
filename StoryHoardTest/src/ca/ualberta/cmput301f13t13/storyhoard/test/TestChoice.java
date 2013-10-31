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

import java.util.HashMap;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Story;

/**
 * @author Owner
 *
 */
public class TestChoice extends TestCase{

	public TestChoice() {
		super();
	}

	/** 
	 * Tests creating a choice.
	 */
	public void testCreateChoice() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
		UUID storyId= story.getId();
		Chapter chap1 = new Chapter(storyId,"test");
		Chapter chap2 = new Chapter(storyId, "test2");
		String text = "pick me";
		Choice c = new Choice(chap1.getId(), chap2.getId(), text);
	}
	/**
	 * Tests retrieving the search information places within the choice, i.e.
	 * the id, chap1, chap2, and text
	 */
	public void testSetSearchCriteria() {
		// empty everything
		Choice criteria = new Choice(null, null, null, null);
		HashMap<String, String> info = criteria.getSearchCriteria();
		
		assertTrue(info.size() == 0);
		assertTrue(info.get("created").equals("false"));
	
		// not empty arguments
		criteria = new Choice(null, null, null, "option 1");
		info = criteria.getSearchCriteria();
		
		assertTrue(info.size() == 1);
		assertTrue(info.get("text").equals("option 1"));
	}
	
	public void testSettersGetters() {
		Choice mockChoice = new Choice(UUID.randomUUID(), UUID.randomUUID(), "opt1");
		
		UUID choiceId = mockChoice.getId();
		UUID currentChapter = mockChoice.getCurrentChapter();
		UUID nextChapter = mockChoice.getNextChapter();
		String text = mockChoice.getText();
		
		mockChoice.setId(UUID.randomUUID().toString());
		mockChoice.setNextChapter(UUID.randomUUID().toString());
		mockChoice.setCurrentChapter(UUID.randomUUID().toString());
		mockChoice.setText("blah");
		
		assertNotSame(choiceId, mockChoice.getId());
		assertNotSame(currentChapter, mockChoice.getCurrentChapter());
		assertNotSame(nextChapter, mockChoice.getNextChapter());
		assertNotSame("blah", text);	
	}

}
