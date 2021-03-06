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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.graphics.Bitmap;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.local.MediaManager;

/**
 * Class meant for the testing of the MediaManager class in the StoryHoard
 * application.
 * 
 * @author Stephanie Gil
 * 
 * @see MediaManager
 */
public class TestMediaManager extends
		ActivityInstrumentationTestCase2<InfoActivity> {
	private MediaManager mm = null;
	private static final String fname1 = "img1.jpg";
	private static final String fname2 = "img2.jpg";
	private Media mockMedia;
	private Media mockMedia3;
	private ArrayList<Media> mockMedias;

	public TestMediaManager() {
		super(InfoActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests adding and loading an image.
	 */
	public void testAddLoadImage() {
		mm = MediaManager.getInstance(getActivity());
		
		Chapter chap = new Chapter(UUID.randomUUID(), "lala");
		Media mockMedia = new Media(chap.getId(), createPath(fname1), null, "");

		mm.insert(mockMedia);

		ArrayList<Media> objects = mm.retrieve(mockMedia);
		assertEquals(objects.size(), 1);

		Media check = (Media) objects.get(0);
		assertTrue(check.getBitmap() != null);
	}

	/**
	 * Tests updating a chapter's media.
	 */
	public void testUpdateMedia() {
		mm = MediaManager.getInstance(getActivity());
		
		Chapter chap = new Chapter(UUID.randomUUID(), "lala");

		Media mockMedia = new Media(chap.getId(), null, null, "");
		mm.insert(mockMedia);

		Media criteria = new Media(null, chap.getId(), null, null, "");

		ArrayList<Media> objects = mm.retrieve(criteria);
		assertEquals(objects.size(), 1);

		Media newM1 = (Media) objects.get(0);

		newM1.setType(Media.ILLUSTRATION);
		newM1.setPath(createPath(fname2));

		mm.update(newM1);

		// make sure you can find new chapter
		objects = mm.retrieve(newM1);
		assertEquals(objects.size(), 1);
		newM1 = (Media) objects.get(0);

		assertFalse(newM1.getType().equals(Media.PHOTO));
		assertFalse(newM1.getPath().equals(fname1));
	}

	/**
	 * Creates a new bitmap, save sit on to SD card and sets path to it.
	 */
	public String createPath(String fname) {
		mm = MediaManager.getInstance(getActivity());
		
		Bitmap bm = BogoPicGen.generateBitmap(50, 50);
		File mFile1 = Environment.getExternalStorageDirectory();

		String fileName = fname;

		File mFile2 = new File(mFile1, fileName);
		try {
			FileOutputStream outStream;

			outStream = new FileOutputStream(mFile2);

			bm.compress(Bitmap.CompressFormat.JPEG, 75, outStream);

			outStream.flush();

			outStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String path = mFile1.getAbsolutePath().toString() + "/" + fileName;

		Log.i("maull", "Your IMAGE ABSOLUTE PATH:-" + path);

		File temp = new File(path);

		if (!temp.exists()) {
			Log.e("file", "no image file at location :" + path);
		}
		return path;
	}

	/**
	 * Tests the correct determining of whether a media exists locally or not.
	 */
	public void testExistsLocally() {
		mm = MediaManager.getInstance(getActivity());
		
		UUID chapId = UUID.randomUUID();
		Media mockMedia = new Media(chapId, createPath(fname1), Media.PHOTO, "");
		mm.insert(mockMedia);
		Media mockMedia2 = new Media(chapId, null, Media.PHOTO, "");

		assertTrue(mm.existsLocally(mockMedia.getId()));
		assertFalse(mm.existsLocally(mockMedia2.getId()));
	}

	/**
	 * Tests synching a media, which is really already tested by inserting and
	 * updating a media.
	 */
	public void testSync() {
		mm = MediaManager.getInstance(getActivity());
		
		UUID chapId = UUID.randomUUID();
		Media mockMedia = new Media(chapId, createPath(fname1), Media.PHOTO, "");
		mockMedia.setBitmapString(mockMedia.getBitmap());
		mm.sync(mockMedia, mockMedia.getId());
		ArrayList<Media> mockMedias = mm.retrieve(mockMedia);
		assertEquals(mockMedias.size(), 1);
	}

	/**
	 * Tests synching the deletion of media.
	 */
	public void testSyncDeletion() {
		mm = MediaManager.getInstance(getActivity());
		
		UUID chapId = UUID.randomUUID();
		Media mockMedia = new Media(chapId, createPath(fname1), Media.PHOTO, "");
		mm.insert(mockMedia);
		Media mockMedia2 = new Media(chapId, null, Media.PHOTO, "");
		mm.insert(mockMedia2);

		ArrayList<UUID> newList = new ArrayList<UUID>();
		newList.add(mockMedia.getId());

		mm.syncDeletions(newList, chapId);
		ArrayList<Media> mockMedias = mm.retrieve(mockMedia2);
		assertEquals(mockMedias.size(), 0);
	}

	/**
	 * Tests removing media from the database.
	 */
	public void testRemove() {
		mm = MediaManager.getInstance(getActivity());
		
		UUID chapId = UUID.randomUUID();
		mockMedia = new Media(chapId, null, Media.PHOTO, "");
		mm.insert(mockMedia);

		mockMedias = mm.getPhotosByChapter(chapId);
		assertEquals(mockMedias.size(), 1);

		mm.remove(mockMedia.getId());
		mockMedias = mm.getPhotosByChapter(chapId);
		assertEquals(mockMedias.size(), 0);
	}
	
	/**
	 * Tests getting all chapters from a chapter.
	 */
	public void testGetMediasByChapter() {
		mm = MediaManager.getInstance(getActivity());
		
		UUID chapId = UUID.randomUUID();
		mockMedia = new Media(chapId, null, Media.PHOTO, "");
		mm.insert(mockMedia);
		mockMedia3 = new Media(chapId, null, Media.ILLUSTRATION, "");
		mm.insert(mockMedia3);

		mockMedias = mm.getPhotosByChapter(chapId);
		assertEquals(mockMedias.size(), 1);
		mockMedias = mm.getIllustrationsByChapter(chapId);
		assertEquals(mockMedias.size(), 1);
	}
}
