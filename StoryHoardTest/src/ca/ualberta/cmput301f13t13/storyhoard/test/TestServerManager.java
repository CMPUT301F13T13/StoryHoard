
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
import java.util.HashMap;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.backend.*;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Tests the methods of the ServerManager class.
 * 
 * @author Stephanie Gil
 *
 */
public class TestServerManager 
		extends ActivityInstrumentationTestCase2<ViewBrowseStories>{
	private static ServerManager sm = null;
	private static final String path = "./mockImages/img1";
	
	public TestServerManager() {
		super(ViewBrowseStories.class);
	}

	public void setUp() throws Exception {
		// clean up server
		sm = ServerManager.getInstance();
	}

	/**
	 * Tests uploading and retrieving a story from the server.
	 */
	public void testUploadRetrieveStory() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		Media m = new Media(chap.getId(), path, Media.PHOTO);
		
		chap.addPhoto(m);
		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.insert(story);
		ArrayList<Object> stories = sm.retrieve(story);
		assertEquals(stories.size(), 1);
		
		Story newStory = (Story) stories.get(0);
		
		HashMap<UUID, Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 1);
		Chapter nChap = chaps.get(newStory.getFirstChapterId());
		ArrayList<Choice> choices = nChap.getChoices();
		assertEquals(choices.size(), 1);
		ArrayList<Media> photos = nChap.getPhotos();
		assertEquals(photos.size(), 1);
	}
	
	/**
	 * Tests updating a story on the server.
	 */
	public void testUpdateStory() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		Media m = new Media(chap.getId(), path, Media.PHOTO);
		
		chap.addPhoto(m);
		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.insert(story);
		ArrayList<Object> stories = sm.retrieve(story);
		assertEquals(stories.size(), 1);
		
		Story newStory = (Story) stories.get(0);
		newStory.setTitle("new title");
		newStory.setAuthor("new author");
		newStory.addChapter(new Chapter(newStory.getId(), "my text"));
		
		sm.update(newStory);
		stories = sm.retrieve(story);
		assertEquals(stories.size(), 1);
		
		HashMap<UUID, Chapter> chaps = newStory.getChapters();
		assertEquals(chaps.size(), 2);
		assertFalse(newStory.getAuthor().equals(story.getAuthor()));
		assertFalse(newStory.getTitle().equals(story.getTitle()));
	}
	
	/**
	 * Tests deleting a story from the server.
	 */
	public void testDeleteStory() {
		Story story = new Story("Harry Potter", "oprah", "the emo boy", 
				Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(story.getId(), "on a dark cold night");
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "hit me!");
		Media m = new Media(chap.getId(), path, Media.PHOTO);
		
		chap.addPhoto(m);
		chap.addChoice(c1);
		story.setFirstChapterId(chap.getId());
		story.addChapter(chap);
		
		sm.insert(story);
		ArrayList<Object> stories = sm.retrieve(story);
		assertEquals(stories.size(), 1);
		
//		sm.deletePublished(story);
		stories = sm.retrieve(story);
		assertEquals(stories.size(), 0);
	}
	
	/**
	 * Tests loading all created stories, and makes sure the results don't
	 * include any stories not created by author.
	 */
	public void testGetAllPublishedStories() {
		Story mockStory1 = new Story("My Cow", "Dr. Poe", "my chubby cow",
				Utilities.getPhoneId(getActivity()));
		sm.insert(mockStory1);
		Story mockStory2 = new Story("My Frog", "Dr. Phil",
				"my chubby frog", Utilities.getPhoneId(getActivity()));
		sm.insert(mockStory2);
		Story mockStory3 = new Story("My Hen", "Dr. Farmer",
				"my chubby hen", Utilities.getPhoneId(getActivity()));
		sm.insert(mockStory3);

		// setting search criteria
		Story mockCriteria = new Story(null, null, null, null, 
				Utilities.getPhoneId(getActivity()));
		ArrayList<Object> mockStories = sm.retrieve(mockCriteria);
		assertEquals(mockStories.size(), 3);

	}
	
	/**
	 * Tests publishing story, caching it, then loading it from server.
	 */
	public void testPublishCacheLoadStory() {
		fail("Not yet implemented");

		Story mockStory = new Story("My Monkey", "TS ELLIOT",
				"monkey is in the server", Utilities.getPhoneId(getActivity()));
		
		sm.insert(mockStory);
		sm.insert(mockStory);

		ArrayList<Object> pubStories = sm.retrieve(mockStory);
		assertEquals(pubStories.size(), 1);
	}
}
