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

package ca.ualberta.cmput301f13t13.storyhoard.backend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.Settings;

/**
 * Class meant for general functions that can be used by any class.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 */
public class Utilities {

	/**
	 * Takes an array of objects and converts them all to Stories.
	 * 
	 * @param objects
	 * @return stories
	 */
	public static ArrayList<Story> objectsToStories(ArrayList<Object> objs) {
		ArrayList<Story> stories = new ArrayList<Story>();

		for (Object obj : objs) {
			stories.add((Story) obj);
		}

		return stories;
	}

	/**
	 * Takes an array of objects and converts them all to Chapters.
	 * 
	 * @param objects
	 * @return chapters
	 */
	public static ArrayList<Chapter> objectsToChapters(ArrayList<Object> objs) {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();

		for (Object obj : objs) {
			chapters.add((Chapter) obj);
		}

		return chapters;
	}

	/**
	 * Takes an array of objects and converts them all to Choice objects.
	 * 
	 * @param objects
	 * @return choices
	 */
	public static ArrayList<Choice> objectsToChoices(ArrayList<Object> objs) {
		ArrayList<Choice> choices = new ArrayList<Choice>();

		for (Object obj : objs) {
			choices.add((Choice) obj);
		}

		return choices;
	}

	/**
	 * Takes an array of objects and converts them all to Media objects.
	 * 
	 * @param objects
	 * @return medias
	 */
	public static ArrayList<Media> objectsToMedia(ArrayList<Object> objects) {
		ArrayList<Media> medias = new ArrayList<Media>();

		for (Object obj : objects) {
			medias.add((Media) obj);
		}

		return medias;
	}



	/**
	 * This functions gets the id of the device and returns it as a string
	 * 
	 * CODE REUSE:
	 * This code is modified from:
	 * 
	 * URL: http://developer.samsung.com/android/technical-docs/How-to-retrieve-the-Device-Unique-ID-from-android-device
	 * Date: Nov. 5th, 2013
	 * 
	 * 
	 */
	public static String getPhoneId(Context context) {
		String PhoneId = Settings.Secure.getString(context.getContentResolver(), 
				Settings.Secure.ANDROID_ID);
		return PhoneId;
	}


	/**
	 * Saves a bitmap to a location on the phone's sd card. Returns
	 * the path of where the image was saved to.
	 * 
	 */
	public static String saveImageToSD(Bitmap bmp) {
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath;
		imageFilePath = folder + "/"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);

		FileOutputStream fout;
		try {
			fout = new FileOutputStream(imageFile);
			bmp.compress(Bitmap.CompressFormat.JPEG, 85, fout);

			fout.flush();
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return imageFilePath;
	}
}
