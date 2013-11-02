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

import org.junit.Before;

import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.*;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Class meant for the testing of the GeneralController class in the StoryHoard
 * application.
 * 
 * @author Stephanie
 * @see GeneralController
 */
public class TestGeneralController extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	
	public TestGeneralController() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);		
	}

	/**
	 * Tests using the controller to add stories and then get all cached stories.
	 */
	public void testGetAllCachedStories() {
		GeneralController gc = GeneralController.getInstance();
		ArrayList<Story> stories = new ArrayList<Story>();
		
		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", false);
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", false);
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", true);
		s3.setFirstChapterId(UUID.randomUUID());
		
		gc.addObjectLocally(s1, GeneralController.STORY, getActivity());
		gc.addObjectLocally(s2, GeneralController.STORY, getActivity());
		gc.addObjectLocally(s3, GeneralController.STORY, getActivity());
		
		stories = gc.getAllStories(GeneralController.CACHED, getActivity());
		
		assertEquals(stories.size(), 2);	
	}
	
	/**
	 * Tests using the controller to get all stories created by the author.
	 */
	public void testGetAllCreatedStories() {
		GeneralController gc = GeneralController.getInstance();
		ArrayList<Story> stories = new ArrayList<Story>();
		
		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", true);
		s1.setFirstChapterId(UUID.randomUUID());
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", true);
		s2.setFirstChapterId(UUID.randomUUID());
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", false);
		s3.setFirstChapterId(UUID.randomUUID());
		
		gc.addObjectLocally(s1, GeneralController.STORY, getActivity());
		gc.addObjectLocally(s2, GeneralController.STORY, getActivity());
		gc.addObjectLocally(s3, GeneralController.STORY, getActivity());
		
		stories = gc.getAllStories(GeneralController.CREATED, getActivity());
		
		assertEquals(stories.size(), 2);
	}	

	/**
	 * Tests using the controller to publish stories and then get all published stories.
	 */
	public void testGetAllPublishedStories() {
		fail("not yet implemented");

		GeneralController gc = GeneralController.getInstance();
		ArrayList<Story> stories = new ArrayList<Story>();
		
		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", false);
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", false);
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", false);
		
		gc.publishStory(s1, getActivity());
		gc.publishStory(s2, getActivity());
		gc.publishStory(s3, getActivity());
		
		stories = gc.getAllStories(GeneralController.PUBLISHED, getActivity());
		
		assertTrue(stories.contains(s1));
		assertTrue(stories.contains(s2));
		assertTrue(stories.contains(s3));
	}
	
	/**
	 * Tests using the controller to add chapters and then get 
	 * all chapters belonging to a story.
	 */
	public void testAddGetAllChapters() {
		GeneralController gc = GeneralController.getInstance();
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		Story story = new Story("title", "author", "des", true);
		
		Chapter c1 = new Chapter(story.getId(), "text");
		Chapter c2 = new Chapter(story.getId(), "text");
		Chapter c3 = new Chapter(UUID.randomUUID(), "text");
		
		gc.addObjectLocally(c1, GeneralController.CHAPTER, getActivity());
		gc.addObjectLocally(c2, GeneralController.CHAPTER, getActivity());
		gc.addObjectLocally(c3, GeneralController.CHAPTER, getActivity());
		
		chapters = gc.getAllChapters(story.getId(), getActivity());
		
		assertEquals(chapters.size(), 2);
	}

	/**
	 * Tests using the controller to add choices and then 
	 * get all choices belonging to a chapter.
	 */	
	public void testAddGetAllChoices() {
		GeneralController gc = GeneralController.getInstance();
		ArrayList<Choice> choices = new ArrayList<Choice>();
		Chapter chap = new Chapter(UUID.randomUUID(), "text");
		
		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "text");
		Choice c2 = new Choice(chap.getId(), UUID.randomUUID(), "text");
		Choice c3 = new Choice(UUID.randomUUID(), UUID.randomUUID(), "text");
		
		gc.addObjectLocally(c1, GeneralController.CHOICE, getActivity());
		gc.addObjectLocally(c2, GeneralController.CHOICE, getActivity());
		gc.addObjectLocally(c3, GeneralController.CHOICE, getActivity());
		
		choices = gc.getAllChoices(chap.getId(), getActivity());
		
		assertEquals(choices.size(), 2);
	}

	public void testAddGetMediaLocally() {
		fail("not yet implemented");
		// photos and illustrations
	}
	
	/**
	 * Tests using the controller to test for a variety of different
	 * stories that have been added / published.
	 */
	public void testSearchStory() {
		GeneralController gc = GeneralController.getInstance();
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
		
		gc.addObjectLocally(s1, GeneralController.STORY, getActivity());
		gc.addObjectLocally(s2, GeneralController.STORY, getActivity());
		gc.addObjectLocally(s3, GeneralController.STORY, getActivity());
		gc.publishStory(s4, getActivity());
		gc.publishStory(s5, getActivity());
		gc.publishStory(s6, getActivity());
		
		// both author and title are null
//		stories = gc.searchStory(null, null, GeneralController.CREATED, getActivity());
//		assertEquals(stories.size(), 0);
		
		// author is me, and created by author
		stories = gc.searchStory(null, "me", GeneralController.CREATED, getActivity());
		assertEquals(stories.size(), 2);
		
		// author is me, and not created by author
		stories = gc.searchStory(null, "me", GeneralController.CACHED, getActivity());
		assertEquals(stories.size(), 1);		
		
		// author is null, created, title is bob the cow
		stories = gc.searchStory("Bob the cow", null, GeneralController.CREATED, getActivity());
		assertEquals(stories.size(), 1);
		
		// title is sad cow, published
		stories = gc.searchStory(null, "me", GeneralController.PUBLISHED, getActivity());
//		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the general controller to update a story.
	 */
	public void testUpdateStoryLocally() {
		GeneralController gc = GeneralController.getInstance();
		ArrayList<Story> stories = new ArrayList<Story>();
		
		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", true);
		s1.setFirstChapterId(UUID.randomUUID());
		
		gc.addObjectLocally(s1, GeneralController.STORY, getActivity());
		
		stories = gc.getAllStories(GeneralController.CREATED, getActivity());
		
		assertEquals(stories.size(), 1);
		
		Story news1 = stories.get(0);
		news1.setDescription("new des");
		news1.setFirstChapterId(UUID.randomUUID());
		
		gc.updateObjectLocally(news1, GeneralController.STORY, getActivity());
		
		stories = gc.getAllStories(GeneralController.CREATED, getActivity());
		news1 = stories.get(0);
		
		assertFalse(news1.getDescription().equals(s1.getDescription()));
		assertTrue(news1.getFirstChapterId() != null);
	}
	
	public void testUpdateChapterLocally() {
		GeneralController gc = GeneralController.getInstance();
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		UUID storyId = UUID.randomUUID();
		
		// Insert some chapters
		Chapter c1 = new Chapter(storyId, "my chapter");
		
		gc.addObjectLocally(c1, GeneralController.CHAPTER, getActivity());
		
		chapters = gc.getAllChapters(storyId, getActivity());
		assertEquals(chapters.size(), 1);
		
		Chapter newc1 = chapters.get(0);
		newc1.setText("a cow mooed");
		
		//TODO edit media
		
		gc.updateObjectLocally(newc1, GeneralController.CHAPTER, getActivity());
		
		chapters = gc.getAllChapters(storyId, getActivity());
		newc1 = chapters.get(0);
		
		assertFalse(newc1.getText().equals(c1.getText()));
	}	

	public void testUpdateChoiceLocally() {
		fail("not yet implemented");
	}
	
	public void testUpdateMediaLocally() {
		fail("not yet implemented");
	}
	
	public void testUpdatePublished() {
		fail("not yet implemented");
	}
	
	public void testGetCompleteStory() {
		fail("not yet implemented");
	}
	
	public void testGetCompleteChapter() {
		fail("not yet implemented");
	}
	
	public void testGetCompleteChoice() {
		fail("not yet implemented");
	}
	
}
