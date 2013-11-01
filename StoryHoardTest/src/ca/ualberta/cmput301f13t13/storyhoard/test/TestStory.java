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

import ca.ualberta.cs.c301f13t13.backend.*;

/**
 * @author Owner
 *
 */
public class TestStory extends TestCase{
	
	public TestStory() {
		super();
	}
	
	/**
	 * Tests creating a story without chapters.
	 */
	public void testCreateStory() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
	}
	
	/**
	 * Tests adding a chapter to a story.
	 */
	public void testAddChapter() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
		Chapter chapter = new Chapter(story.getId(), "On a cold, dark night.");
		story.addChapter(chapter);
		assertEquals(story.getChapters().size(), 1);
	}
	
	/**
	 * Tests retrieving a specific chapter from a story.
	 */
	public void testGetChapter() {
		Story story = new Story("7 bugs", "Shamalan", "scary story", true);
		Chapter chapter1 = new Chapter(story.getId(), "On a cold, dark night.");
		Chapter chapter2 = new Chapter(story.getId(), "On a sunny, bright day.");
		story.addChapter(chapter1);
		story.addChapter(chapter2);
		
		Chapter result = story.getChapter(chapter1.getId());
		assertSame(result, chapter1);
	}
	
	/**
	 * Tests retrieving the search information places within the story, i.e.
	 * the id, title, author, description, and whether or not it was created
	 * by the author.
	 */
	public void testSetSearchCriteria() {
		// empty everything
		Story criteria = new Story(null, null, null, null, null);
		HashMap<String, String> info = criteria.getSearchCriteria();
		
		assertTrue(info.size() == 0);
	
		// not empty arguments
		criteria = new Story(null, "john", "the cow", "went home", true);
		info = criteria.getSearchCriteria();
		
		assertTrue(info.size() == 3);
		assertTrue(info.get("title").equals("john"));
		assertTrue(info.get("author").equals("the cow"));
		assertTrue(info.get("created").equals("true"));
	}
	
	public void testSettersGetters() {
		Story mockStory = new Story("title1", "author1", "desc1", true);
		
		UUID storyId = mockStory.getId();
		String title = mockStory.getTitle();
		String author = mockStory.getAuthor();
		String desc = mockStory.getDescription();
		Boolean created = mockStory.getAuthorsOwn();
		HashMap<UUID, Chapter> chapters = mockStory.getChapters();
		UUID firstChapId = mockStory.getFirstChapterId();
		
		mockStory.setId(UUID.randomUUID());
		mockStory.setTitle("new title");
		mockStory.setAuthor("pinkie");
		mockStory.setDescription("new desc");
		mockStory.setAuthorsOwn(false);
		mockStory.setChapters(null);
		mockStory.setFirstChapterId(UUID.randomUUID());
		
		assertNotSame(storyId, mockStory.getId());
		assertNotSame("new title", title);
		assertNotSame("pinkie", author);
		assertNotSame("new desc", desc);
		assertNotSame(created, mockStory.getAuthorsOwn());
		assertTrue(mockStory.getChapters() == null);
		assertNotSame(mockStory.getFirstChapterId(), firstChapId);		
	}
}
