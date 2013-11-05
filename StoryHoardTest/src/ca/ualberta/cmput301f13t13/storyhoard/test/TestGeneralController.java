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

import org.junit.Before;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.DBContract;
import ca.ualberta.cs.c301f13t13.backend.DBHelper;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Media;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the GeneralController class in the StoryHoard
 * application.
 * 
 * @author Stephanie

 * @see SHController
 */
public class TestGeneralController extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	DBHelper helper = null;
	SHController gc = null;
	
	public TestGeneralController() {
		super(ViewBrowseStories.class);
		helper = DBHelper.getInstance(this.getActivity());
		SHController.getInstance(getActivity());
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		// Clearing database
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
	}

	/**
	 * Tests using the controller to add stories and then get all cached
	 * stories.
	 */
	public void testGetAllCachedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", false);
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", false);
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", true);
		s3.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, SHController.STORY);
		gc.addObject(s2, SHController.STORY);
		gc.addObject(s3, SHController.STORY);

		stories = gc.getAllStories(SHController.CACHED_STORY);

		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the controller to get all stories created by the author.
	 */
	public void testGetAllCreatedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", true);
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", true);
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", false);
		s3.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, SHController.STORY);
		gc.addObject(s2, SHController.STORY);
		gc.addObject(s3, SHController.STORY);

		stories = gc.getAllStories(SHController.CREATED_STORY);

		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the controller to publish stories and then get all published
	 * stories.
	 */
	public void testGetAllPublishedStories() {
		fail("not yet implemented");

		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", false);
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", false);
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", false);

		gc.addObject(s1, SHController.PUBLISHED_STORY);
		gc.addObject(s2, SHController.PUBLISHED_STORY);
		gc.addObject(s3, SHController.PUBLISHED_STORY);

		stories = gc.getAllStories(SHController.PUBLISHED_STORY);

		assertTrue(stories.contains(s1));
		assertTrue(stories.contains(s2));
		assertTrue(stories.contains(s3));
	}

	/**
	 * Tests using the controller to add chapters and then get all chapters
	 * belonging to a story.
	 */
	public void testAddGetAllChapters() {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		Story story = new Story("title", "author", "des", true);

		Chapter c1 = new Chapter(story.getId(), "text");
		Chapter c2 = new Chapter(story.getId(), "text");
		Chapter c3 = new Chapter(UUID.randomUUID(), "text");

		gc.addObject(c1, SHController.CHAPTER);
		gc.addObject(c2, SHController.CHAPTER);
		gc.addObject(c3, SHController.CHAPTER);

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

		gc.addObject(c1, SHController.CHOICE);
		gc.addObject(c2, SHController.CHOICE);
		gc.addObject(c3, SHController.CHOICE);

		choices = gc.getAllChoices(chap.getId());

		assertEquals(choices.size(), 2);
	}
	
	/**
	 * Tests using the controller to add media and then retrieve it 
	 * again.
	 */
	public void testAddGetAllMedia() {
		UUID cId = UUID.randomUUID();
		Media photo1 = new Media(cId, Uri.parse("https://"), Media.PHOTO);
		Media photo2 = new Media(cId, Uri.parse("https://"), Media.PHOTO);
		Media ill1 = new Media(cId, Uri.parse("https://"), Media.ILLUSTRATION);
		Media ill2 = new Media(cId, Uri.parse("https://"), Media.ILLUSTRATION);

		gc.addObject(photo1, SHController.MEDIA);
		gc.addObject(photo2, SHController.MEDIA);
		gc.addObject(ill1, SHController.MEDIA);
		gc.addObject(ill2, SHController.MEDIA);

		ArrayList<Media> photos = gc.getAllPhotos(cId);
		ArrayList<Media> ills = gc.getAllIllustrations(cId);

		assertEquals(photos.size(), 2);
		assertEquals(ills.size(), 2);
	}	

	/**
	 * Tests using the controller to test for a variety of different stories
	 * that have been added / published.
	 */
	public void testSearchStory() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("Lily the cow", "me", "D: none", true);
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("Bob the cow", "me", "D: none", true);
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("Bob the cow", "me", "D: none", false);
		s3.setFirstChapterId(UUID.randomUUID());
		Story s4 = new Story("sad cow", "me", "D: none", false);
		s4.setFirstChapterId(UUID.randomUUID());
		Story s5 = new Story("sad cow", "me", "D: none", false);
		s5.setFirstChapterId(UUID.randomUUID());
		Story s6 = new Story("sad hen", "me", "D: none", false);
		s6.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, SHController.STORY);
		gc.addObject(s2, SHController.STORY);
		gc.addObject(s3, SHController.STORY);
		gc.addObject(s4, SHController.PUBLISHED_STORY);
		gc.addObject(s5, SHController.PUBLISHED_STORY);
		gc.addObject(s6, SHController.PUBLISHED_STORY);

		// both author and title are null (should retrieve all created stories)
		stories = gc.searchStory(null, null, SHController.CREATED_STORY);
		assertEquals(stories.size(), 2);

		// author is me, and created by author
		stories = gc.searchStory(null, "me", SHController.CREATED_STORY);
		assertEquals(stories.size(), 2);

		// author is me, and not created by author
		stories = gc.searchStory(null, "me", SHController.CACHED_STORY);
		assertEquals(stories.size(), 1);

		// author is null, created, title is bob the cow
		stories = gc.searchStory("Bob the cow", null, SHController.CREATED_STORY);
		assertEquals(stories.size(), 1);

		// title is sad cow, published
		stories = gc.searchStory(null, "me", SHController.PUBLISHED_STORY);
		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the general controller to update a story.
	 */
	public void testUpdateStoryLocally() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", true);
		s1.setFirstChapterId(UUID.randomUUID());

		gc.addObject(s1, SHController.STORY);

		stories = gc.getAllStories(SHController.CREATED_STORY);

		assertEquals(stories.size(), 1);

		Story news1 = stories.get(0);
		news1.setDescription("new des");
		news1.setFirstChapterId(UUID.randomUUID());

		gc.updateObject(news1, SHController.STORY);

		stories = gc.getAllStories(SHController.CREATED_STORY);
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

		gc.addObject(c1, SHController.CHAPTER);

		chapters = gc.getAllChapters(storyId);
		assertEquals(chapters.size(), 1);

		Chapter newc1 = chapters.get(0);
		newc1.setText("a cow mooed");
		Media m = new Media(c1.getId(), Uri.parse("https://"), Media.PHOTO);
		newc1.addPhoto(m);

		gc.updateObject(newc1, SHController.CHAPTER);

		chapters = gc.getAllChapters(storyId);
		newc1 = chapters.get(0);
		ArrayList<Media> medias = gc.getAllPhotos(c1.getId());
		assertEquals(medias.size(), 1);

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

		gc.addObject(c1, SHController.CHOICE);

		choices = gc.getAllChoices(chapId);
		assertEquals(choices.size(), 1);

		Choice newc1 = choices.get(0);
		newc1.setText("a cow mooed");
		newc1.setNextChapter(UUID.randomUUID());

		gc.updateObject(newc1, SHController.CHOICE);

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
		Media m1 = new Media(chapId, Uri.parse("https://google.ca"), Media.PHOTO);
		Media m2 = new Media(chapId, Uri.parse("https://google.ca"), Media.PHOTO);

		gc.addObject(m1, SHController.MEDIA);
		gc.addObject(m2, SHController.MEDIA);
		
		medias = gc.getAllPhotos(chapId);
		assertEquals(medias.size(), 2);

		Media newM1 = medias.get(0);
		newM1.setUri(Uri.parse("https://ualberta.ca"));
		newM1.setType(Media.ILLUSTRATION);

		gc.updateObject(newM1, SHController.MEDIA);

		medias = gc.getAllPhotos(chapId);
		assertEquals(medias.size(), 1);

		medias = gc.getAllIllustrations(chapId);
		assertEquals(medias.size(), 1);
		newM1 = medias.get(0);
		assertFalse(newM1.getUri().equals(m1.getUri()));
	}

	/**
	 * Tests using the general controller to update a published story.
	 */
	public void testUpdatePublished() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", false);

		gc.publishStory(s1);
		stories = gc.getAllStories(SHController.PUBLISHED);

		Story newS = stories.get(0);
		newS.setAuthor("Mr. Blubbers");
		newS.setTitle("The very long night");
		newS.setDescription("There once was a blubber");
		
		Chapter chap = new Chapter(s1.getId(), "there is a time...");
		newS.addChapter(chap);
		newS.setFirstChapterId(chap.getId());

		gc.updatePublished(newS);
		stories = gc.getAllStories(SHController.PUBLISHED);
		assertEquals(stories.size(), 1);
		
		newS = stories.get(0);
		assertFalse(newS.getAuthor().equals(s1.getAuthor()));
		assertFalse(newS.getTitle().equals(s1.getTitle()));
		assertFalse(newS.getDescription().equals(s1.getDescription()));
		
		HashMap<UUID, Chapter> chaps = newS.getChapters();
		assertEquals(chaps.size(), 1);		
		assertTrue(chaps.get(newS.getFirstChapterId()) != null);
	}

	/**
	 * Tests getting a complete chapter, but the chapters don't contain any
	 * media.
	 */
	public void testGetCompleteStory() {

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", true);
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", true);

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
		gc.addObject(c1, SHController.CHOICE);
		gc.addObject(c2, SHController.CHOICE);
		gc.addObject(chap1, SHController.CHAPTER);
		gc.addObject(chap2, SHController.CHAPTER);
		gc.addObject(chap3, SHController.CHAPTER);
		gc.addObject(s1, SHController.STORY);
		gc.addObject(s2, SHController.STORY);

		Story myStory1 = gc.getCompleteStory(s1.getId());
		Story myStory2 = gc.getCompleteStory(s2.getId());

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
		Media m1 = new Media(chap1.getId(), Uri.parse("https://googe.ca"),
							 Media.PHOTO);

		// add everything into database
		gc.addObject(m1, SHController.MEDIA);
		gc.addObject(choice1, SHController.CHOICE);
		gc.addObject(choice2, SHController.CHOICE);
		gc.addObject(chap1, SHController.CHAPTER);
		gc.addObject(chap2, SHController.CHAPTER);

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
}
