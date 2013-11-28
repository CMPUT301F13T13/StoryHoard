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
	private static final Story story = new Story(
			UUID.fromString("f1bda3a9-4560-4530-befc-2d58db9419b7"),
			"Harry Potter", "oprah", "the emo boy", "232");
	private static final Story story2 = new Story(
			UUID.fromString("e4558e4e-5140-4838-be40-e4d5be0b5299"),
			"Ugly Duckling", "oprah", "the emo boy", "232");
	private static final Story story3 = new Story(
			UUID.fromString("e4558e4e-5140-4838-be40-e7d5be0b5277"),
			"remove me", "oprah", "the emo boy", "232");
	private static ArrayList<Story> stories;

	public TestServerManager() {
		super(InfoActivity.class);
	}

	/**
	 * Tests no errors occur while inserting a story onto the server.
	 */
	public void testAAInsert() {
		sm = ServerManager.getInstance();
		sm.setTestServer();

		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Chapter chap2 = new Chapter(story.getId(), "he lughe");
		Choice c1 = new Choice(chap.getId(), chap2.getId(), "hit me!");

		chap.addChoice(c1);
		story.addChapter(chap);
		story.addChapter(chap2);

		try {
			sm.insert(story);
			sm.insert(story2);
			sm.insert(story3);
		} catch (Exception e) {
			fail("error while inserting story on server");
		}
	}

	/**
	 * Tests the inserting worked by testing getting the story by its Id.
	 */
	public void testGetById() {
		sm = ServerManager.getInstance();
		sm.setTestServer();

		Story newStory = sm.getById(story.getId());
		assertNotNull(newStory);
	}

	/**
	 * Tests no errors occurring while updating server.
	 */
	public void testUpdateStoryPart1() {
		sm = ServerManager.getInstance();
		sm.setTestServer();

		Story newStory = new Story(story2.getId(), "new title", "new author",
				"new des", "125");
		Chapter chap = new Chapter(story2.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		chap.addChoice(c1);
		newStory.setFirstChapterId(chap.getId());
		newStory.addChapter(chap);

		try {
			sm.update(newStory);
		} catch (Exception e) {
			fail("error while updating story on server");
		}
	}

	/**
	 * Tests the update actually did update the story.
	 */
	public void testUpdateStoryPart2() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		Story newStory = sm.getById(story2.getId());
		assertNotNull(newStory);

		ArrayList<Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 1);
		assertTrue(newStory.getAuthor().equals("new author"));
		assertTrue(newStory.getTitle().equals("new title"));

		// cleaning up server
		sm.remove(story.getId().toString());
		sm.remove(story2.getId().toString());
		sm = ServerManager.getInstance();
		sm.setTestServer();

		try {
			stories = sm.getAll();
		} catch (Exception e) {
			fail("error occured while getting all published stories on server");
		}
	}

	/**
	 * Tests loading all created stories, and makes sure the results don't
	 * include any stories not created by author.
	 */
	public void testAllPublishedStoriesPart2() {
		sm = ServerManager.getInstance();
		sm.setTestServer();

		stories = sm.getAll();
		stories = sm.getAll();
		stories = sm.getAll();
		assertEquals(stories.size(), 3);
	}

	/**
	 * Tests searching for a story by keywords in the title.
	 */
	public void testSearchByKeywords() {
		sm = ServerManager.getInstance();
		sm.setTestServer();

		// setting search criteria
		stories = sm.searchByKeywords("harry");
		assertEquals(stories.size(), 1);
	}

	/**
	 * Tests getting a random story
	 */
	public void testRandomStory() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		Story story = sm.getRandom();
		assertNotNull(story);
	}

	/**
	 * Tests no errors occur while removing story from server.
	 */
	public void testRemovePart1() {
		sm = ServerManager.getInstance();
		sm.setTestServer();

		try {
			sm.remove(story3.getId().toString());
		} catch (Exception e) {
			fail("error while removing story from server");
		}
	}

	/**
	 * Tests that the story actually was removed.
	 */
	public void testRemovePart2() {
		sm = ServerManager.getInstance();
		sm.setTestServer();
		sm.remove(story3.getId().toString());
		stories = sm.getAll();
		assertEquals(stories.size(), 2);
	}
}
