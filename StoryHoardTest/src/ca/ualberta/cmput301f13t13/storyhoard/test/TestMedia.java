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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.EditChapterActivity;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;
import ca.ualberta.cs.c301f13t13.gui.ViewChapter;

/**
 * Class meant for the testing of the Media class in the StoryHoard 
 * application.
 * 
 * @author Stephanie Gil
 * 
 * @see Media
 */
public class TestMedia extends
		ActivityInstrumentationTestCase2<EditChapterActivity> {
	private static Uri uri;
	private static EditChapterActivity activity;
	
	public TestMedia() {
		super(EditChapterActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		Story story = new Story("title", "author", "es", "432432");
		Intent intent = new Intent();
		intent.putExtra("isEditing", false);
		intent.putExtra("addingNewChapt", true);
		intent.putExtra("Story", story);
		intent.putExtra("Chapter", new Chapter(story.getId(), null));
		
		setActivityIntent(intent);
		
		activity = getActivity();
		activity.takePhoto();
		uri = activity.getImageFileUri();
	}

	/**
	 * Tests creating a media object.
	 */
	public void testCreateMedia() {
		assertTrue(uri != null);
		
		// Make photo
		try {
			Media photo = new Media(UUID.randomUUID(), uri.getPath(), 
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
		fail("not yet implemented");
		
		Media photo = new Media(UUID.randomUUID(), uri.getPath(), Media.PHOTO);

		// Take new photo
		activity.takePhoto();
		Uri newUri = activity.getImageFileUri();
		
		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		Bitmap bm = photo.getBitmap();

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);
		photo.setPath(newUri.getPath());
		
		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertNotSame(type, photo.getType());
		assertTrue(photo.getBitmap() != null);
		assertFalse(photo.getPath().equals(uri.getPath()));
	}
}
