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

import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ca.ualberta.cmput301f13t13.storyhoard.backend.LifecycleData;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;

/**
 * @author sgil
 *
 */
public abstract class MediaActivity extends Activity {
	public static final int BROWSE_GALLERY_ACTIVITY_REQUEST_CODE = 1;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2;
	private Uri imageFileUri;
	private String imageType;
	private LifecycleData lifedata;
	ImageView imageView;

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
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageFileUri = getUri();
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
		lifedata = LifecycleData.getInstance();
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Chapter chapter = LifecycleData.getInstance().getChapter();
				Media photo = new Media(chapter.getId(),
						imageFileUri.getPath(), imageType);

				lifedata.addToCurrImages(photo);
				lifedata.setCurrImage(photo);
				insertIntoGallery(photo);
			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}

		} else if (requestCode == BROWSE_GALLERY_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Uri imageFileUri = intent.getData();
				Chapter chapter = LifecycleData.getInstance().getChapter();
				String path = getRealPathFromURI(imageFileUri, this);
				Media photo = new Media(chapter.getId(), path, imageType);
				lifedata.addToCurrImages(photo);
				lifedata.setCurrImage(photo);
			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}
		}
	}	

	/**
	 * Code for getting uri of a new file created for an image
	 * 
	 * CODE REUSE LonelyTweeter Camera Code from Lab 
	 * Author: Joshua Campbell 
	 * License: Unlicense 
	 * Date: Nov. 7, 2013
	 * @return 
	 */
	private Uri getUri() {
		String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(imageFilePath);
		Uri imageFileUri = Uri.fromFile(imageFile);

		return imageFileUri;
	}

	/**
	 * CODE REUSE URL:
	 * http://android-er.blogspot.ca/2012/07/implement-gallery-like.html Date:
	 * Nov. 7, 2013 Author: Andr.oid Eric
	 */
	public View insertImage(Media img, Context context, LinearLayout main) {
		Bitmap bm = decodeSampledBitmapFromUri(Uri.parse(img.getPath()), 
				250, 250);
		LinearLayout layout = new LinearLayout(context);

		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(250, 250));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bm);
		imageView.setTag(img);

		layout.addView(imageView);
		main.addView(layout);
		
		return (View) imageView;
	}		
	
	/**
	 * CODE REUSE
	 * URL: http://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore
	 * DATE: NOV. 9, 2013
	 * 
	 * @param contentUri
	 * @param context
	 * @return
	 */
	private String getRealPathFromURI(Uri contentUri, Context context) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}	

	/**
	 * Calculates size for bitmap.
	 * 
	 * CODE REUSE
	 * URL: http://android-er.blogspot.ca/2012/07/implement-gallery-like.html
	 * Date: Nov. 7, 2013
	 * Author: Andr.oid Eric
	 */		
	public static int calculateInSampleSize(BitmapFactory.Options options, 
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);   
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);   
			}   
		}

		return inSampleSize;   
	}	

	/**
	 * CODE REUSE
	 * URL: http://android-er.blogspot.ca/2012/07/implement-gallery-like.html
	 * Date: Nov. 7, 2013
	 * Author: Andr.oid Eric
	 */	
	private static Bitmap decodeSampledBitmapFromUri(Uri uri, 
			int reqWidth, int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(uri.getPath(), options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 
				reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(uri.getPath(), options); 

		return bm;  
	}		
}
