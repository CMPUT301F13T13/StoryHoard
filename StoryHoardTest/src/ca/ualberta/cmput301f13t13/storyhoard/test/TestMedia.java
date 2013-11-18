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

import java.util.UUID;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cmput301f13t13.storyhoard.backend.*;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the Media class in the StoryHoard 
 * application.
 * 
 * @author Stephanie Gil
 * 
 * @see Media
 */
public class TestMedia extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private static final String path = "android.resource://ca.ualberta.cmput301f13t13.storyhoard.test/" + R.drawable.img1;
	private static final String path2 = "android.resource://ca.ualberta.cmput301f13t13.storyhoard.test/" + R.drawable.img2;
	
	public TestMedia() {
		super(ViewBrowseStories.class);
	}

	/**
	 * Tests creating a media object.
	 */
	public void testCreateMedia() {
		// Make photo
		try {
			Media photo = new Media(UUID.randomUUID(), path, 
					Media.PHOTO);
			assertTrue(photo.getBitmap() != null);
		} catch (Exception e) {
			fail("error creating a new media object");
		}
	}

	/**
	 * Tests the setters and getters of a Media object.
	 */
	@SuppressWarnings("unused")
	public void testSettersGetters() {
		Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO);
		
		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		Bitmap bm = photo.getBitmap();

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);
		photo.setPath(path2);
		
		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertNotSame(type, photo.getType());
		assertTrue(photo.getBitmap() != null);
		assertFalse(photo.getPath().equals(path));
	}
}
