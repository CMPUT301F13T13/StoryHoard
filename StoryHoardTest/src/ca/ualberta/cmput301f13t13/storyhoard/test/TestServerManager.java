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
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Tests the methods of the ServerManager class.
 * 
 * @author Stephanie Gil
 * 
 */
public class TestServerManager extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	private static ServerManager sm = null;
	private static ArrayList<Story> stories;

	public TestServerManager() {
		super(InfoActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testUpdateStory() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		
		UUID id1 = UUID.fromString("f1bda3a9-4560-4530-befc-2d58db9419b7");
		Story story = new Story(id1, "Harry Potter test", "oprah", 
				"the emo boy", "232");
		
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Chapter chap2 = new Chapter(story.getId(), "he lughe");
		Choice c1 = new Choice(chap.getId(), chap2.getId(), "hit me!");

		// adding choices + chapters
		chap.getChoices().add(c1);
		story.getChapters().add(chap);
		story.getChapters().add(chap2);

		sm.insert(story);
		Story newStory = sm.getById(story.getId());
		assertNotNull(newStory);
		
		newStory = new Story(story.getId(), "new title", "new author",
				"new des", "125");
		chap = new Chapter(story.getId(), "on a dark cold night");
		c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		chap.getChoices().add(c1);
		newStory.setFirstChapterId(chap.getId());
		newStory.getChapters().add(chap);
		
		sm.update(newStory);		
		newStory = sm.getById(id1);
		
		ArrayList<Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 1);
		assertTrue(newStory.getAuthor().equals("new author"));
		assertTrue(newStory.getTitle().equals("new title"));

		// cleaning up server
		sm.remove(story.getId().toString());
	}

	/**
	 * Tests loading all created stories, and makes sure the results don't
	 * include any stories not created by author.
	 */
	public synchronized void testAllPublishedStories() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		
		Story story = new Story("Harry Potter test", "oprah", 
				"the emo boy", "232");
		Story story2 = new Story("Ugly Duckling test", "oprah", 
				"the emo boy", "232");
		
		sm.insert(story);
		sm.insert(story2);
		
		stories = sm.getAll();
		try {
			Thread.sleep(10000);
			assertTrue(hasStory(stories, story));
			assertTrue(hasStory(stories, story2));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sm.remove(story.getId().toString());
		sm.remove(story2.getId().toString());
	}

	/**
	 * Tests getting a random story
	 */
	public void testRandomStory() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		
		UUID id1 = UUID.fromString("f1bda3a9-4560-4530-befc-2d58db9419b7");
		UUID id2 = UUID.fromString("e4558e4e-5140-4838-be40-e4d5be0b5299");
		Story story = new Story(id1, "Harry Potter test", "oprah", 
				"the emo boy", "232");
		Story story2 = new Story(id2, "Ugly Duckling test", "oprah", 
				"the emo boy", "232");
		
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Chapter chap2 = new Chapter(story.getId(), "he lughe");
		Choice c1 = new Choice(chap.getId(), chap2.getId(), "hit me!");

		chap.getChoices().add(c1);
		story.getChapters().add(chap);
		story.getChapters().add(chap2);

		sm.insert(story);
		sm.insert(story2);
		
		story = sm.getRandom();
		assertNotNull(story);
		
		// Cleaning up server
		sm.remove(story.getId().toString());
		sm.remove(story2.getId().toString());
	}

	/**
	 * Tests inserting stories into server, retrieving them by id,
	 * retrieving them by keywords, and deleting them.
	 */
	public void testInsertRetrieveSearchRemove() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		
		Story story = new Story("Harry Potter test", "oprah", 
				"the emo boy", "232");
		Story story2 = new Story("Ugly Duckling test", "oprah", 
				"the emo boy", "232");
		
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Chapter chap2 = new Chapter(story.getId(), "he lughe");
		Choice c1 = new Choice(chap.getId(), chap2.getId(), "hit me!");

		chap.getChoices().add(c1);
		story.getChapters().add(chap);
		story.getChapters().add(chap2);

		sm.insert(story);
		sm.insert(story2);
		
		// By Id
		story = sm.getById(story.getId());
		assertNotNull(story);
		
		// By keywords
		ArrayList<Story> results = sm.searchByKeywords("Ugly duckling test");
		assertNotSame(results.size(), 0);
		
		story = results.get(0);
		assertTrue(story.getTitle().equals("Ugly Duckling test"));
		
		// testing remove
		sm.remove(story2.getId().toString());
		sm.remove(story.getId().toString());
		Story newStory = sm.getById(story2.getId());
		assertNull(newStory);
		newStory = sm.getById(story.getId());
		assertNull(newStory);
	}
	
    /**
     * Checks whether a story is contained in a stories ArrayList.
     */
    public Boolean hasStory(ArrayList<Story> stories, Story aStory) {
            for (Story story : stories) {
                    if (story.getId().equals(aStory.getId())) {
                            return true;
                    }
            }
            return false;
    }	
}
