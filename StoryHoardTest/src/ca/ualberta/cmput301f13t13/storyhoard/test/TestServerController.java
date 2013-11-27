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
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ServerStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Tests the functionality of the server story controller.
 * 
 * @author sgil
 * 
 */
public class TestServerController extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	private static ServerStoryController serverCon;
	private static final Story story = new Story(
			UUID.fromString("f1bda3a9-4560-4530-befc-2d58db9419b7"),
			"Harry Potter", "oprah", "the emo boy", "232");
	private static final Story story2 = new Story(
			UUID.fromString("e4558e4e-5140-4838-be40-e4d5be0b5299"),
			"Ugly Duckling", "oprah", "the emo boy", "232");

	public TestServerController() {
		super(InfoActivity.class);
	}

	/**
	 * Tests no errors occruing while inserting a story.
	 */
	public void testAInsert() {
		ServerManager.getInstance().setTestServer();
		serverCon = ServerStoryController.getInstance(getActivity());

		try {
			serverCon.insert(story);
			serverCon.insert(story2);
		} catch (Exception e) {
			fail("error occrured in inserting story on server");
		}
	}

	/**
	 * Tests getting all published stories, also tests the insert worked.
	 */
	public void testPublishGetAll() {
		serverCon = ServerStoryController.getInstance(getActivity());
		ServerManager.getInstance().setTestServer();
		ArrayList<Story> stories = serverCon.getAll();
		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests searching for a published story using keywords found in its title.
	 * Also tests the insert worked.
	 */
	public void testSearchByTitle() {
		serverCon = ServerStoryController.getInstance(getActivity());
		ArrayList<Story> stories = serverCon.searchByKeywords("Harry");
		assertEquals(stories.size(), 1);
	}

	/**
	 * Tests getting a random story. Also tests that the insert worked.
	 */
	public void testGetRandomStory() {
		serverCon = ServerStoryController.getInstance(getActivity());
		Story story = serverCon.getRandomStory();
		assertNotNull(story);
	}

	/**
	 * Tests no errors occur while updating a published story.
	 */
	public void testUpdatePart1() {
		serverCon = ServerStoryController.getInstance(getActivity());
		Story newStory = new Story(story.getId(), "new title", "me", "des",
				"123");
		try {
			serverCon.update(newStory);
		} catch (Exception e) {
			fail("error while updating story on server");
		}
	}

	/**
	 * Tests the updating actually updated the story data.
	 */
	public void testUpdatePart2() {
		serverCon = ServerStoryController.getInstance(getActivity());
		ArrayList<Story> stories = serverCon.searchByKeywords("new title");
		assertEquals(stories.size(), 1);

		// cleaning server
		serverCon.remove(story.getId());
	}

	/**
	 * Tests no errors occur while removing story from server.
	 */
	public void testRemovePart1() {
		// delete
		serverCon = ServerStoryController.getInstance(getActivity());
		try {
			serverCon.remove(story2.getId());
		} catch (Exception e) {
			fail("error while removing story from server");
		}
	}

	/**
	 * Tests that the story actually was removed.
	 */
	public void testRemovePart2() {
		serverCon = ServerStoryController.getInstance(getActivity());
		// delete
		ArrayList<Story> stories = serverCon.searchByKeywords("Duckling");
		assertEquals(stories.size(), 0);
	}
}
