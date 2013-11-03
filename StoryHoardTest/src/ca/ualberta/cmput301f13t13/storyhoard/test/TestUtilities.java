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

import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Media;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.backend.Utilities;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Class meant for the testing of the Utilities class in the StoryHoard
 * application.
 * 
 * @author Stephanie
 * @see Utilities
 */
public class TestUtilities extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {

	public TestUtilities() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests converting an arrayList of objects to stories.
	 */
	@SuppressWarnings("unused")
	public void objectsToStories() {
		Story story = new Story("title", "author", "desc", true);
		Story story2 = new Story("title2", "author2", "desc2", true);
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add(story);
		objects.add(story2);
		ArrayList<Story> stories = Utilities.objectsToStories(objects);
		try {
			ArrayList<Story> as = stories;
		} catch (Exception e) {
			fail("converstion from objects to stories failed");
		}
	}

	/**
	 * Tests converting an arrayList of objects to chapters.
	 */
	@SuppressWarnings("unused")
	public void objectsToChapters() {
		Chapter chapter = new Chapter(UUID.randomUUID(), "chap text");
		Chapter chapter2 = new Chapter(UUID.randomUUID(), "chap text");
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add(chapter);
		objects.add(chapter2);
		ArrayList<Chapter> chapters = Utilities.objectsToChapters(objects);
		try {
			ArrayList<Chapter> chaps = chapters;
		} catch (Exception e) {
			fail("converstion from objects to chapters failed");
		}
	}

	/**
	 * Tests converting an arrayList of objects to choices.
	 */
	@SuppressWarnings("unused")
	public void objectsToChoices() {
		Choice choice = new Choice(UUID.randomUUID(), UUID.randomUUID(),
				"chap text");
		Choice choice2 = new Choice(UUID.randomUUID(), UUID.randomUUID(),
				"chap text");
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add(choice);
		objects.add(choice2);
		ArrayList<Choice> choices = Utilities.objectsToChoices(objects);
		try {
			ArrayList<Choice> as = choices;
		} catch (Exception e) {
			fail("converstion from objects to choices failed");
		}
	}

	/**
	 * Tests converting an arrayList of objects to medias.
	 */
	@SuppressWarnings("unused")
	public void objectsToMedia() {
		Media media = new Media(UUID.randomUUID(), Uri.parse("https://blah"),
				Media.PHOTO);
		Media media2 = new Media(UUID.randomUUID(), Uri.parse("https://blah"),
				Media.ILLUSTRATION);
		ArrayList<Object> objects = new ArrayList<Object>();
		objects.add(media);
		objects.add(media2);
		ArrayList<Media> medias = Utilities.objectsToMedia(objects);
		try {
			ArrayList<Media> as = medias;
		} catch (Exception e) {
			fail("converstion from objects to medias failed");
		}
	}

}
