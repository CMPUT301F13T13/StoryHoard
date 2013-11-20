
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
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ObjectType;
import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ServerManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the GeneralController class in the 
 * StoryHoard application.
 * 
 * @author Stephanie Gil

 * @see SHController
 */
public class TestSHController extends
ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private SHController gc = null;
	private static ViewBrowseStories activity;
	private static final String path = "./mockImages/img1";

	public TestSHController() {
		super(ViewBrowseStories.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		activity = this.getActivity();
		// Clearing database
		DBHelper helper = DBHelper.getInstance(activity);
		helper.close();
		activity.deleteDatabase(DBContract.DATABASE_NAME);

		gc = SHController.getInstance(activity);				
	}

	/**
	 * Tests using the controller to add stories and then get all cached
	 * stories.
	 */
	public void testGetAllCachedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", "343423");
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "45643543");
		s3.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, ObjectType.CREATED_STORY);
		gc.addObject(s2, ObjectType.CACHED_STORY);
		gc.addObject(s3, ObjectType.CACHED_STORY);

		stories = gc.getAllStories(ObjectType.CACHED_STORY);

		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the controller to get all stories created by the author.
	 */
	public void testGetAllCreatedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "34530");
		s3.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, ObjectType.CREATED_STORY);
		gc.addObject(s2, ObjectType.CREATED_STORY);
		gc.addObject(s3, ObjectType.CACHED_STORY);

		stories = gc.getAllStories(ObjectType.CREATED_STORY);

		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the controller to publish stories and then get all published
	 * stories.
	 */
	public void testGetAllPublishedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));

		gc.addObject(s1, ObjectType.PUBLISHED_STORY);
		gc.addObject(s2, ObjectType.PUBLISHED_STORY);
		gc.addObject(s3, ObjectType.PUBLISHED_STORY);

		stories = gc.getAllStories(ObjectType.PUBLISHED_STORY);

		assertTrue(stories.size() > 3);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
		sm.remove(s2);
		sm.remove(s3);
	}

	/**
	 * Tests using the controller to add chapters and then get all chapters
	 * belonging to a story.
	 */
	public void testAddGetAllChapters() {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		Story story = new Story("title", "author", "des", 
				Utilities.getPhoneId(getActivity()));

		Chapter c1 = new Chapter(story.getId(), "text");
		Chapter c2 = new Chapter(story.getId(), "text");
		Chapter c3 = new Chapter(UUID.randomUUID(), "text");

		gc.addObject(c1, ObjectType.CHAPTER);
		gc.addObject(c2, ObjectType.CHAPTER);
		gc.addObject(c3, ObjectType.CHAPTER);

		chapters = gc.getAllChapters(story.getId());

		assertEquals(chapters.size(), 2);
	}

	/**
	 * Tests using the controller to add choices and then get all choices
	 * belonging to a chapter.
	 */
	public void testAddGetAllChoices() {
		ArrayList<Choice> choices = new ArrayList<Choice>();
		Chapter chap = new Chapter(UUID.randomUUID(), "text");

		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "text");
		Choice c2 = new Choice(chap.getId(), UUID.randomUUID(), "text");
		Choice c3 = new Choice(UUID.randomUUID(), UUID.randomUUID(), "text");

		gc.addObject(c1, ObjectType.CHOICE);
		gc.addObject(c2, ObjectType.CHOICE);
		gc.addObject(c3, ObjectType.CHOICE);

		choices = gc.getAllChoices(chap.getId());

		assertEquals(choices.size(), 2);
	}

	/**
	 * Tests using the controller to add media and then retrieve it 
	 * again.
	 */
	public void testAddGetAllMedia() {	
		Chapter chap = new Chapter(UUID.randomUUID(), "lala");
		Media photo1 = new Media(chap.getId(), null, Media.PHOTO);

		gc.addObject(photo1, ObjectType.MEDIA);

		ArrayList<Media> photos = gc.getAllPhotos(chap.getId());

		assertEquals(photos.size(), 1);
	}	

	/**
	 * Tests using the controller to test for a variety of different stories
	 * that have been added / published.
	 */
	public void testSearchStory() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("Lily the cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("Bob the hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("Bob the cow", "me", "D: none", 
				"34532432423");
		Story s4 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s5 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s6 = new Story("sad hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));

		gc.addObject(s1, ObjectType.CACHED_STORY);
		gc.addObject(s2, ObjectType.CREATED_STORY);
		gc.addObject(s3, ObjectType.CREATED_STORY);
		gc.addObject(s4, ObjectType.PUBLISHED_STORY);
		gc.addObject(s5, ObjectType.PUBLISHED_STORY);
		gc.addObject(s6, ObjectType.PUBLISHED_STORY);

		// title are null (should retrieve all created stories)
		stories = gc.searchStory(null, ObjectType.CREATED_STORY);
		assertEquals(stories.size(), 2);

		// title has cow, created
		stories = gc.searchStory("cow", ObjectType.CREATED_STORY);
		assertEquals(stories.size(), 1);

		// created, title has bob and hen
		stories = gc.searchStory("Bob hen", ObjectType.CREATED_STORY);
		assertEquals(stories.size(), 1);

		// title has sad
		stories = gc.searchStory("sad", ObjectType.PUBLISHED_STORY);
		assertEquals(stories.size(), 3);
		
		// clean up server
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s4);
		sm.remove(s5);
		sm.remove(s6);
	}

	/**
	 * Tests using the general controller to update a story.
	 */
	public void testUpdateStoryLocally() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		s1.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, ObjectType.CREATED_STORY);

		stories = gc.getAllStories(ObjectType.CREATED_STORY);

		assertEquals(stories.size(), 1);

		Story news1 = stories.get(0);
		news1.setDescription("new des");
		news1.setFirstChapterId(UUID.randomUUID());

		gc.updateObject(news1, ObjectType.CREATED_STORY);

		stories = gc.getAllStories(ObjectType.CREATED_STORY);
		news1 = stories.get(0);

		assertFalse(news1.getDescription().equals(s1.getDescription()));
		assertTrue(news1.getFirstChapterId() != null);
	}

	/**
	 * Tests using the general controller to update a chapter.
	 */
	public void testUpdateChapterLocally() {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		UUID storyId = UUID.randomUUID();

		// Insert some chapters
		Chapter c1 = new Chapter(storyId, "my chapter");

		gc.addObject(c1, ObjectType.CHAPTER);

		chapters = gc.getAllChapters(storyId);
		assertEquals(chapters.size(), 1);

		Chapter newc1 = chapters.get(0);
		newc1.setText("a cow mooed");

		gc.updateObject(newc1, ObjectType.CHAPTER);

		chapters = gc.getAllChapters(storyId);
		newc1 = chapters.get(0);

		assertFalse(newc1.getText().equals(c1.getText()));
	}

	/**
	 * Tests using the general controller to update a choice.
	 */
	public void testUpdateChoiceLocally() {
		ArrayList<Choice> choices = new ArrayList<Choice>();
		UUID chapId = UUID.randomUUID();

		// Insert some choices
		Choice c1 = new Choice(chapId, UUID.randomUUID(), "my choice");

		gc.addObject(c1, ObjectType.CHOICE);

		choices = gc.getAllChoices(chapId);
		assertEquals(choices.size(), 1);

		Choice newc1 = choices.get(0);
		newc1.setText("a cow mooed");
		newc1.setNextChapter(UUID.randomUUID());

		gc.updateObject(newc1, ObjectType.CHOICE);

		choices = gc.getAllChoices(chapId);
		newc1 = choices.get(0);

		assertFalse(newc1.getText().equals(c1.getText()));
		assertFalse(newc1.getNextChapter().equals(c1.getNextChapter()));
	}

	/**
	 * Tests using the general controller to edit media objects.
	 */
	public void testUpdateMediaLocally() {
		ArrayList<Media> medias = new ArrayList<Media>();
		UUID chapId = UUID.randomUUID();

		// Insert some media
		Media m1 = new Media(chapId, null, Media.PHOTO);

		gc.addObject(m1, ObjectType.MEDIA);

		medias = gc.getAllPhotos(chapId);
		assertEquals(medias.size(), 2);

		Media newM1 = medias.get(0);
		newM1.setType(Media.ILLUSTRATION);

		gc.updateObject(newM1, ObjectType.MEDIA);

		medias = gc.getAllPhotos(chapId);
		assertEquals(medias.size(), 1);

		medias = gc.getAllIllustrations(chapId);
		assertEquals(medias.size(), 1);
		newM1 = medias.get(0);
		assertFalse(newM1.getPath() == null);
	}

	/**
	 * Tests using the general controller to update a published story.
	 */
	public void testUpdatePublished() {
		ArrayList<Story> stories = new ArrayList<Story>();

		fail("not yet implemented");

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", Utilities.getPhoneId(getActivity()));

		gc.addObject(s1, ObjectType.PUBLISHED_STORY);
		stories = gc.getAllStories(ObjectType.PUBLISHED_STORY);

		Story newS = stories.get(0);
		newS.setAuthor("Mr. Blubbers");
		newS.setTitle("The very long night");
		newS.setDescription("There once was a blubber");

		Chapter chap = new Chapter(s1.getId(), "there is a time...");
		newS.addChapter(chap);
		newS.setFirstChapterId(chap.getId());

		gc.updateObject(newS, ObjectType.PUBLISHED_STORY);
		stories = gc.getAllStories(ObjectType.PUBLISHED_STORY);
		assertEquals(stories.size(), 1);

		newS = stories.get(0);
		assertFalse(newS.getAuthor().equals(s1.getAuthor()));
		assertFalse(newS.getTitle().equals(s1.getTitle()));
		assertFalse(newS.getDescription().equals(s1.getDescription()));

		HashMap<UUID, Chapter> chaps = newS.getChapters();
		assertEquals(chaps.size(), 1);		
		assertTrue(chaps.get(newS.getFirstChapterId()) != null);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
	}
	/**
	 * Tests getting a random choice,
	 */
	public void testGetRandomChoice() {
		UUID s1 = UUID.randomUUID();
		Chapter chap1 = new Chapter(s1, "chapter text ");
		Chapter chap2 = new Chapter(s1, "chapter text rawr");
		Choice choice1 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		Choice choice2 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		Choice choice3 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		gc.addObject(choice1, ObjectType.CHOICE);
		//gc.addObject(choice2, ObjectType.CHOICE);
		//gc.addObject(choice3, ObjectType.CHOICE);
		gc.addObject(chap1, ObjectType.CHAPTER);
		gc.addObject(chap2, ObjectType.CHAPTER);
		Choice choice = gc.getRandomChoice(chap1.getId());
		assertEquals(choice.getId(), choice1.getId());
		//assertEquals(choice.getId(), choice2.getId());
		//assertEquals(choice.getId(), choice3.getId());


	}
	/**
	 * Tests getting a complete chapter, but the chapters don't contain any
	 * media.
	 */
	public void testGetCompleteStory() {

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", Utilities.getPhoneId(getActivity()));

		// Making chapters and choices
		Chapter chap1 = new Chapter(s1.getId(), "chapter text rawr");
		Chapter chap2 = new Chapter(s1.getId(), "then this happens");
		Chapter chap3 = new Chapter(s2.getId(), "then this dies");
		Choice c1 = new Choice(chap1.getId(), chap2.getId(), "choice texters");
		Choice c2 = new Choice(chap1.getId(), UUID.randomUUID(), "hi");

		s1.setFirstChapterId(chap1.getId());
		s2.setFirstChapterId(chap3.getId());

		chap1.addChoice(c1);
		chap1.addChoice(c2);

		s1.addChapter(chap1);
		s1.addChapter(chap2);
		s2.addChapter(chap3);

		// add everything into database
		gc.addObject(c1, ObjectType.CHOICE);
		gc.addObject(c2, ObjectType.CHOICE);
		gc.addObject(chap1, ObjectType.CHAPTER);
		gc.addObject(chap2, ObjectType.CHAPTER);
		gc.addObject(chap3, ObjectType.CHAPTER);
		gc.addObject(s1, ObjectType.CREATED_STORY);
		gc.addObject(s2, ObjectType.CREATED_STORY);

		Story myStory1 = gc.getCompleteStory(s1.getId(), ObjectType.CREATED_STORY);
		Story myStory2 = gc.getCompleteStory(s2.getId(), ObjectType.CREATED_STORY);

		assertTrue(myStory1 != null);
		assertTrue(myStory2 != null);

		// Checking story that has one chapter
		assertEquals(s2.getId(), myStory2.getId());
		assertEquals(s2.getChapters().size(), 1);

		// checking for correct story information
		assertEquals(myStory2.getFirstChapterId(), s2.getFirstChapterId());
		assertTrue(myStory2.getTitle().equals(s2.getTitle()));
		assertTrue(myStory2.getAuthor().equals(s2.getAuthor()));
		assertTrue(myStory2.getDescription().equals(s2.getDescription()));

		// checking the chapter of the story
		Chapter s2Chap = myStory2.getChapter(chap3.getId());
		assertTrue(s2Chap != null);
		assertEquals(s2Chap.getId(), chap3.getId());
		assertTrue(s2Chap.getText().equals(chap3.getText()));

		// Checking the story with 2 chapters, one has choices
		assertEquals(s1.getId(), myStory1.getId());
		assertEquals(s1.getChapters().size(), 2);

		// checking story information
		assertEquals(myStory1.getFirstChapterId(), s1.getFirstChapterId());
		assertTrue(myStory1.getTitle().equals(s1.getTitle()));
		assertTrue(myStory1.getAuthor().equals(s1.getAuthor()));
		assertTrue(myStory1.getDescription().equals(s1.getDescription()));

		// Checking the chapter with choices
		Chapter s1c1 = myStory1.getChapter(chap1.getId());
		assertTrue(s1c1 != null);

		ArrayList<Choice> choices = s1c1.getChoices();
		assertEquals(choices.size(), 2);
		Choice c1c1 = choices.get(0);
		Choice c1c2 = choices.get(1);

		if (c1c1.getId().equals(c1.getId())) {
			assertTrue(c1c1.getText().equals(c1.getText()));
			assertEquals(c1c1.getNextChapter(), chap2.getId());
		} else if (c1c2.getId().equals(c1.getId())) {
			assertTrue(c1c2.getText().equals(c1.getText()));
			assertEquals(c1c2.getNextChapter(), chap2.getId());
		} else {
			fail("error in retrieving chapter choices: getCompleteStory");
		}

		// checking the chapter without choices
		Chapter s1c2 = myStory1.getChapter(chap2.getId());
		assertTrue(s1c1 != null);

		ArrayList<Choice> choices2 = s1c2.getChoices();
		assertEquals(choices2.size(), 0);
		assertTrue(s1c2.getText().equals(chap2.getText()));

	}

	/**
	 * Tests getting a complete chapter, contains choices and media.
	 */
	public void testGetCompleteChapter() {
		UUID s1 = UUID.randomUUID();

		// Making chapters and choices
		Chapter chap1 = new Chapter(s1, "chapter text rawr");
		Chapter chap2 = new Chapter(s1, "chapter text rawr");
		Choice choice1 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		Choice choice2 = new Choice(chap1.getId(), UUID.randomUUID(), "hi");

		fail("not yet implemented");
		Media m1 = new Media(chap1.getId(), path, Media.PHOTO);

		// add everything into database
		gc.addObject(m1, ObjectType.MEDIA);
		gc.addObject(choice1, ObjectType.CHOICE);
		gc.addObject(choice2, ObjectType.CHOICE);
		gc.addObject(chap1, ObjectType.CHAPTER);
		gc.addObject(chap2, ObjectType.CHAPTER);

		Chapter newChap = gc.getCompleteChapter(chap1.getId());
		assertTrue(newChap != null);
		assertEquals(newChap.getId(), chap1.getId());

		// checking media
		ArrayList<Media> medias = newChap.getPhotos();
		assertEquals(medias.size(), 1);

		// checking choices
		ArrayList<Choice> choices = newChap.getChoices();
		assertEquals(choices.size(), 2);

		Choice c1 = choices.get(0);
		Choice c2 = choices.get(1);

		if (c1.getId().equals(choice1.getId())) {
			assertTrue(c1.getText().equals(choice1.getText()));
			assertEquals(c1.getNextChapter(), chap2.getId());

			assertTrue(c2.getText().equals(choice2.getText()));
			assertTrue(c2.getNextChapter() != null);
		} else if (c2.getId().equals(choice1.getId())) {
			assertTrue(c2.getText().equals(choice1.getText()));
			assertEquals(c2.getNextChapter(), chap2.getId());

			assertTrue(c1.getText().equals(choice1.getText()));
			assertTrue(c1.getNextChapter() != null);
		} else {
			fail("error in retrieving chapter choices: getCompleteChapter");
		}
	}

	/**
	 * Tests retrieving a random story from published stories.
	 */
	public void testRandomStory() {
		Story s1 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("sad hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		
		gc.addObject(s1, ObjectType.PUBLISHED_STORY);
		gc.addObject(s2, ObjectType.PUBLISHED_STORY);
		
		Story random = gc.getRandomStory();
		assertTrue(random != null);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
		sm.remove(s2);
	}


	/**
	 * Tests caching a story.
	 */
	public void testCacheLoadStory() {
		Story mockStory = new Story("My Monkey", "TS ELLIOT",
				"monkey is in the server", Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(mockStory.getId(), "l");
		Chapter chap2 = new Chapter(mockStory.getId(), "2");
		chap.addChoice(new Choice(chap.getId(), chap2.getId(), "hi"));

		// TODO add a media to chapter
		mockStory.addChapter(chap);

		gc.cacheStory(mockStory);

		Story story = gc.getCompleteStory(mockStory.getId(), ObjectType.CACHED_STORY);
		assertTrue(story != null);	
	}
}