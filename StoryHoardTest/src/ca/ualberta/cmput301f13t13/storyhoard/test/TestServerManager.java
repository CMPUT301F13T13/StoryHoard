
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

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.backend.*;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Tests the methods of the ServerManager class.
 * 
 * @author Stephanie Gil
 *
 */
public class TestServerManager 
		extends ActivityInstrumentationTestCase2<ViewBrowseStories>{
	private static ServerManager sm = null;
	
	public TestServerManager() {
		super(ViewBrowseStories.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		sm = ServerManager.getInstance();
		sm.setTestServer();
	}

	public void tearDown() throws Exception {
		super.tearDown();
		sm = ServerManager.getInstance();
		sm.setRealServer();
	}
	/**
	 * Tests uploading and retrieving a story by id from the server. Also
	 * tests removing a story from a server
	 */
	public void testGetByIdAndDelete() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Chapter chap2 = new Chapter(story.getId(), "he lughe");
		Choice c1 = new Choice(chap.getId(), chap2.getId(), "hit me!");

		chap.addChoice(c1);
		story.addChapter(chap);
		story.addChapter(chap2);
		
		sm.update(story);
		story = sm.getById(story.getId());
		assertNotNull(story);

		ArrayList<Chapter> chaps = story.getChapters();
		assertEquals(chaps.size(), 2);
		
		// delete
		sm.remove(story.getId().toString());
		story = sm.getById(story.getId());
		assertNull(story);
	}
	
	/**
	 * Tests updating a story on the server.
	 */
	public void testUpdateStory() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");

		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.update(story);
		Story newStory = sm.getById(story.getId());
		assertNotNull(newStory);
		
		newStory.setTitle("new title");
		newStory.setAuthor("new author");
		newStory.addChapter(new Chapter(newStory.getId(), "my text"));
		
		sm.update(newStory);
		newStory = sm.getById(story.getId());
		assertNotNull(newStory);
		
		ArrayList<Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 2);
		assertFalse(newStory.getAuthor().equals(story.getAuthor()));
		assertFalse(newStory.getTitle().equals(story.getTitle()));
		
		sm.remove(newStory.getId().toString());
	}

	/**
	 * Tests loading all created stories, and makes sure the results don't
	 * include any stories not created by author.
	 */
	public void testGetAllPublishedStories() {		
		Story mockStory1 = new Story("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(getActivity()));
		sm.update(mockStory1);
		Story mockStory2 = new Story("My Frog", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(getActivity()));
		sm.update(mockStory2);

		// setting search criteria
		ArrayList<Story> stories = sm.getAll();
		assertTrue(stories.size() > 0);

		sm.remove(mockStory1.getId().toString());
		sm.remove(mockStory2.getId().toString());
	}
	
	/**
	 * Tests searching for a story by keywords in the title.
	 */
	public void testSearchByKeywords() {
		Story mockStory1 = new Story("test My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(getActivity()));
		sm.update(mockStory1);
		Story mockStory2 = new Story("test My Cow Again", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(getActivity()));
		sm.update(mockStory2);

		// setting search criteria
		ArrayList<Story> stories = sm.searchByKeywords("test");
		assertEquals(stories.size(), 2);
		sm.remove(mockStory1.getId().toString());
		sm.remove(mockStory2.getId().toString());			
	}
	
	/**
	 * Tests getting a random story
	 */
	public void testRandomStory() {
		Story mockStory1 = new Story("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(getActivity()));
		sm.update(mockStory1);
		Story mockStory2 = new Story("My Frog", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(getActivity()));
		sm.update(mockStory2);

		Story story = sm.getRandom();
		assertNotNull(story);
		sm.remove(mockStory1.getId().toString());
		sm.remove(mockStory2.getId().toString());		
	}
}
