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

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Class meant for the testing of the MediaManager class in the StoryHoard
 * application.
 * 
 * @author Stephanie
 * @see MediaManager
 */
public class TestMediaManager extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private static final Uri uri = Uri.parse("https://raw.github.com/CMPUT301F13T13/StoryHoard/master/mockups/all_chapters.png");
	private MediaManager mm = null;
	
	public TestMediaManager() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
		mm = MediaManager.getInstance(getActivity());
	}

	/**
	 * Tests adding and loading an image.
	 */
	public void testAddLoadImage() {
		Media mockMedia = new Media(UUID.randomUUID(), uri, Media.PHOTO);

		mm.insert(mockMedia);

		ArrayList<Object> objects = mm.retrieve(mockMedia);
		assertEquals(objects.size(), 1);

		Media check = (Media) objects.get(0);
		assertTrue(check != null);
	}

	/**
	 * Tests getting all photos, and all illustrations belonging to a chapter.
	 */
	public void testGetAllMedia() {
		UUID chapId = UUID.randomUUID();
		Media m1 = new Media(chapId, uri, Media.PHOTO);
		Media m2 = new Media(chapId, uri, Media.PHOTO);
		Media m3 = new Media(chapId, uri, Media.ILLUSTRATION);
		Media m4 = new Media(chapId, uri, Media.ILLUSTRATION);

		mm.insert(m1);
		mm.insert(m2);
		mm.insert(m3);
		mm.insert(m4);

		// get all media
		Media criteria = new Media(null, chapId, null, null);
		ArrayList<Object> objects = mm.retrieve(criteria);
		assertEquals(objects.size(), 4);

		// get all photos
		criteria = new Media(null, chapId, null, Media.PHOTO);
		objects = mm.retrieve(criteria);
		assertEquals(objects.size(), 2);

		Media newm = (Media) objects.get(0);
		assertTrue(newm != null);
		assertTrue(newm.getType().equals(Media.PHOTO));

		// get all illustrations
		criteria = new Media(null, chapId, null, Media.ILLUSTRATION);
		objects = mm.retrieve(criteria);
		assertEquals(objects.size(), 2);

		newm = (Media) objects.get(0);
		assertTrue(newm != null);
		assertTrue(newm.getType().equals(Media.ILLUSTRATION));
	}

	/**
	 * Tests updating a chapter's media.
	 */
	public void testUpdateMedia() {
		Chapter mockChapter = new Chapter(UUID.randomUUID(), "hi there");

		// Making media for chapter
		Media m1 = new Media(mockChapter.getId(), uri, Media.PHOTO);
		mm.insert(m1);

		ArrayList<Object> objects = mm.retrieve(m1);
		assertEquals(objects.size(), 1);

		Media newM1 = (Media) objects.get(0);

		newM1.setType(Media.ILLUSTRATION);
		newM1.setUri(Uri.parse("https://raw.github.com/CMPUT301F13T13/StoryHoard/master/mockups/published_stories.jpg"));

		mm.update(newM1);

		// make sure you can find new chapter
		objects = mm.retrieve(newM1);
		assertEquals(objects.size(), 1);
		newM1 = (Media) objects.get(0);

		assertFalse(newM1.getType().equals(Media.PHOTO));
		assertNotSame(newM1.getUri(), uri);
	}
}
