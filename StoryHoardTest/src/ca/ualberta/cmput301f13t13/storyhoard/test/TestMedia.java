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
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

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
	private static String path;
	private static String path2;
	
	public TestMedia() {
		super(ViewBrowseStories.class);
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
		path = createPath("img1.jpg");
		// Make photo
		try {
			@SuppressWarnings("unused")
			Media photo = new Media(UUID.randomUUID(), path, 
					Media.PHOTO);
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
		path = createPath("img1.jpg");
		Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO);

		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		Bitmap bm = photo.getBitmap();

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);

		path2 = createPath("img2.jpg");
		photo.setPath(path2);

		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertNotSame(type, photo.getType());
		assertTrue(photo.getBitmap() != null);
		assertFalse(photo.getPath().equals(path));
	}
	
	/**
	 * tests adding itself to the database
	 */
	public void testAddSelf() {
		Media media = new Media(UUID.randomUUID(), null, Media.PHOTO);
		media.addSelf(getActivity());
		MediaManager mm = MediaManager.getInstance(getActivity());
		ArrayList<Object> objs = mm.retrieve(media);
		assertEquals(objs.size(), 1);
	}
	
	/**
	 * tests updating itself in the database
	 */
	public void testUpdateSelf() {
		path = createPath("img1.jpg");
		Media media = new Media(UUID.randomUUID(), path, Media.PHOTO);
		media.addSelf(getActivity());
		MediaManager mm = MediaManager.getInstance(getActivity());	
		media.setType(Media.ILLUSTRATION);
		media.updateSelf(getActivity());
		ArrayList<Object> objs = mm.retrieve(new Media(media.getId(), null, null, null));
		assertEquals(objs.size(), 1);		
		assertTrue(((Media)objs.get(0)).getType().equals(Media.ILLUSTRATION));
	}
	
	/**
	 * Creates a new bitmap, save sit on to SD card and sets path to it.
	 */
	public String createPath(String fname) {
		Bitmap bm = BogoPicGen.generateBitmap(50, 50);
		File mFile1 = Environment.getExternalStorageDirectory();

		String fileName = fname;

		File mFile2 = new File(mFile1,fileName);
		try {
			FileOutputStream outStream;

			outStream = new FileOutputStream(mFile2);

			bm.compress(Bitmap.CompressFormat.JPEG, 75, outStream);

			outStream.flush();

			outStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String path = mFile1.getAbsolutePath().toString()+"/"+fileName;

		Log.i("maull", "Your IMAGE ABSOLUTE PATH:-"+path); 

		File temp=new File(path);

		if(!temp.exists()){
			Log.e("file","no image file at location :"+path);
		}
		
		return path;
	}
	
	/**
	 * Tests removing itself from the database
	 */
	public void testRemoveSelf() {
		Media media = new Media(UUID.randomUUID(), null, Media.PHOTO);
		media.addSelf(getActivity());
		
		MediaManager mm = MediaManager.getInstance(getActivity());
		ArrayList<Object> objs = mm.retrieve(media);
		assertEquals(objs.size(), 1);		

		media.removeSelf(getActivity());
		ArrayList<Object> medias = mm.retrieve(media);
		assertEquals(medias.size(), 0);
	}	
}
