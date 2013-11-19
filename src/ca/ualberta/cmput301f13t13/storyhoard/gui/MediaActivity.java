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
package ca.ualberta.cmput301f13t13.storyhoard.gui;

import java.io.File;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.HolderApplication;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ObjectType;
import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author sgil
 *
 */
public abstract class MediaActivity extends Activity {
	public static final int BROWSE_GALLERY_ACTIVITY_REQUEST_CODE = 1;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	private GUIMediaUtilities util;
	private Uri imageFileUri;
	private String imageType;;
	
	/**
	 * Code for browsing gallery
	 * </br>
	 * CODE REUSE 
	 * </br>
	 * URL: http://stackoverflow.com/questions/6016000/how-to-open-phones-gallery-through-code
	 * </br>
	 * Date: Nov. 7, 2013
	 */
	public void browseGallery(String imageType) {
		this.imageType = imageType;
		util = new GUIMediaUtilities();
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				BROWSE_GALLERY_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * Code for calling the camera app to take a photo.
	 * </br>
	 * CODE REUSE 
	 * </br>
	 * LonelyTweeter Camera Code from Lab 
	 * </br>
	 * Author: Joshua Charles
	 * </br>
	 * Campbell License: Unlicense 
	 * </br>
	 * Date: Nov. 7, 2013
	 * 
	 * @param imageFileUri
	 */
	public void takePhoto(String imageType) {
		this.imageType = imageType;
		util = new GUIMediaUtilities();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageFileUri = util.getUri();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	/**
	 * Adds an image into the gallery
	 */
	public void insertIntoGallery(Media image) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(image.getPath());
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Chapter chapter = ((HolderApplication) this.getApplication()).getChapter();
				Media photo = new Media(chapter.getId(),
						imageFileUri.getPath(), imageType);
				SHController gc = SHController.getInstance(this);
				gc.addObject(photo, ObjectType.MEDIA);
				insertIntoGallery(photo);
			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}

		} else if (requestCode == BROWSE_GALLERY_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri imageFileUri = intent.getData();
				Chapter chapter = ((HolderApplication) this.getApplication()).getChapter();
				String path = util.getRealPathFromURI(imageFileUri, this);
				SHController gc = SHController.getInstance(this);
				Media photo = new Media(chapter.getId(), path, imageType);
				gc.addObject(photo, ObjectType.MEDIA);
			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}
		}
	}	
}
