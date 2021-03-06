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
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;

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
	}

	/**
	 * Tests creating a media object.
	 */
	public void testCreateMedia() {
		path = BogoPicGen.createPath("img1.jpg");
		// Make photo
		try {
			@SuppressWarnings("unused")
			Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO, "");
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
		Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO, "");

		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		Bitmap bm = photo.getBitmap();
		String text = "text";

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);
		photo.setText(text);

		path2 = createPath("img2.jpg");
		photo.setPath(path2);

		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertFalse(type.equals(photo.getType()));
		assertTrue(text.equals(photo.getText()));
		assertTrue(photo.getBitmap() != null);
		assertFalse(photo.getPath().equals(path));
	}
	
	/**
	 * Creates a new bitmap, save sit on to SD card and sets path to it.
	 * This code is only for use in the JUnit tests. </br></br>
	 * 
	 * CODE REUSE: </br>
	 * This code was modified from the code at:</br>
	 * URL: http://stackoverflow.com/questions/7021728/android-writing-bitmap-to-sdcard </br>
	 * Date: Nov. 2, 2013 </br>
	 * License`: CC-BY-SA
	 */ 
	public static String createPath(String fname) {
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
			e.printStackTrace();
		} catch (IOException e) {
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
}
