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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapController;
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
	private ImageView imageView;
	private String photoComment = "";
	private AlertDialog photoDialog;
	private ChapController chapCon;

	/**
	 * Code for browsing gallery </br></br>
	 * CODE REUSE </br>
	 * URL: http://stackoverflow.com/questions/6016000/how-to-open-phones-gallery-through-code </br>
	 * Date: Nov. 7, 2013 </br>
	 * License:  CC-BY-SA  
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
	 * Code for calling the camera app to take a photo. </br></br>
	 * 
	 * CODE REUSE </br>
	 * LonelyTweeter Camera Code from Lab </br>
	 * Author: Joshua Charles </br>
	 * Campbell License: Unlicense </br>
	 * Date: Nov. 7, 2013 </br>
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
	 * Sets up the dialog for taking a photo or browsing the gallery, and also
	 * for adding a text comment to the photo. Then calls the either takePhoto()
	 * or browseGallery() to actual complete the task of adding a photo to
	 * the chapter.
	 */
	public void addPhoto() {
		// getting image text / annotation
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		// Set dialog title
		alert.setTitle("Post a photo");
		final EditText text = new EditText(this); 
		text.setHint("Enter comment here");	
		
		// setting max length for comment
		InputFilter[] fArray = new InputFilter[1];
		fArray[0] = new InputFilter.LengthFilter(50);
		text.setFilters(fArray);
		alert.setView(text);
		
		// Options that user may choose to add photo
		final String[] methods = { "Take Photo", "Choose from Gallery" };
		alert.setSingleChoiceItems(methods, -1,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					photoComment = text.getText().toString();
					takePhoto(Media.PHOTO);
					break;
				case 1:
					photoComment = text.getText().toString();
					browseGallery(Media.PHOTO);
					break;
				}
				photoDialog.dismiss();
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				photoDialog.dismiss();
				return;   
			}
		});		
		
		photoDialog = alert.create();
		photoDialog.show();
	}
	
	/**
	 * Adds an image that was just taken with the camera app into the gallery.
	 */
	public void insertIntoGallery(Media image) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(image.getPath());
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	/**
	 * Determines what activity was just performed (either taking a
	 * photo or choosing from the gallery), and creates a new Media
	 * object to store in the LifecycleData class for other 
	 * activity classes to use.
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			String path = "";
			chapCon = ChapController.getInstance(this);
			Chapter chapter = chapCon.getCurrChapter();
			Media photo = new Media(chapter.getId(), path , imageType);
			photo.setText(photoComment);
			if (requestCode == BROWSE_GALLERY_ACTIVITY_REQUEST_CODE) {
				imageFileUri = intent.getData();
				photo.setPath(getRealPathFromURI(imageFileUri, this));
			} else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
				photo.setPath(imageFileUri.getPath());
				insertIntoGallery(photo);
			}
			chapCon.addMedia(photo);
		} else if (resultCode == RESULT_CANCELED) {
			System.out.println("cancelled action");
		} else {
			System.err.println("Error " + resultCode);
		}
	}	

	/**
	 * Code for getting the Uri of a new file created for storing an image
	 * which has just been taken by the camera app. </br> </br>
	 * 
	 * CODE REUSE </br>
	 * LonelyTweeter Camera Code from Lab </br>
	 * Author: Joshua Campbell </br>
	 * License: Unlicense </br>
	 * Date: Nov. 7, 2013 </br>
	 * 
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
	 * Inserts the bitmap of a Media object onto an imageView to display in
	 * the activity. It also puts the Media object as the Tag of the imageView
	 * so that later on if the image needs to be deleted or the text field 
	 * needs to be read, we will know which object the bitmap belongs to. </br></br>
	 * 
	 * CODE REUSE </br> 
	 * URL: http://android-er.blogspot.ca/2012/07/implement-gallery-like.html </br>
	 * Date: Nov. 7, 2013 </br>
	 * Author: Andr.oid Eric
	 */
	protected View insertImage(Media img, Context context, LinearLayout main) {
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
	 * Takes a uri, and from it finds the absolute path to the media. It 
	 * returns the absolute path as a string. </br></br>
	 * 
	 * CODE REUSE </br>
	 * URL: http://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore </br>
	 * DATE: NOV. 9, 2013 </br>
	 * License: CC- </br>
	 * 
	 * @param contentUri
	 * 			Uri we want to find the path of. Note that it is of type Uri and not URI.
	 * @param context
	 * @return the absolute path of the Uri as a string
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
	 * Calculates the size desired for bitmap. Only to be used by the 
	 * decodeSampledBitmapFromUri method below. </br></br>
	 * 
	 * CODE REUSE </br>
	 * URL: http://android-er.blogspot.ca/2012/07/implement-gallery-like.html </br>
	 * Date: Nov. 7, 2013 </br>
	 * Author: Andr.oid Eric </br>
	 */		
	private static int calculateInSampleSize(BitmapFactory.Options options, 
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
	 * Decodes a Uri into a bitmap and also make sure the bitmap is scaled
	 * down to the specified size. Only to be used by the insertImage()
	 * method when setting a bitmap to an imageView. </br></br>
	 * 
	 * CODE REUSE </br>
	 * URL: http://android-er.blogspot.ca/2012/07/implement-gallery-like.html </br>
	 * Date: Nov. 7, 2013 </br>
	 * Author: Andr.oid Eric </br>
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
