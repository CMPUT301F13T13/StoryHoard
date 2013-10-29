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

import junit.framework.TestCase;

import org.junit.Test;

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
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
