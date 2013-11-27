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
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;

/**
 * Class meant for the testing of the Media class in the StoryHoard application.
 * 
 * @author Stephanie Gil
 * 
 * @see Media
 */
public class TestMedia extends ActivityInstrumentationTestCase2<InfoActivity> {
	private static String path;
	private static String path2;

	public TestMedia() {
		super(InfoActivity.class);
	}

	public void setUp() throws Exception {
		super.setUp();

		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
	}

	/**
	 * Tests creating a media object.
	 */
	public void testCreateMedia() {
		path = BogoPicGen.createPath("img1.jpg");
		// Make photo
		try {
			@SuppressWarnings("unused")
			Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO);
			Bitmap bm = BitmapFactory.decodeFile(path);
			assertTrue(bm != null);
		} catch (Exception e) {
			fail("error creating a new media object");
		}
	}

	/**
	 * Tests the setters and getters of a Media object.
	 */
	@SuppressWarnings("unused")
	public void testSettersGetters() {
		path = BogoPicGen.createPath("img1.jpg");
		Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO);

		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		Bitmap bm = photo.getBitmap();
		String text = "text";

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);
		photo.setText(text);

		path2 = BogoPicGen.createPath("img2.jpg");
		photo.setPath(path2);

		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertFalse(type.equals(photo.getType()));
		assertTrue(text.equals(photo.getText()));
		assertTrue(photo.getBitmap() != null);
		assertFalse(photo.getPath().equals(path));
	}
}
