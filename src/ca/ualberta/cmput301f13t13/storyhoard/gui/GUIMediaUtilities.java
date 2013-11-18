/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.gui;

import java.io.File;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;

/**
 * @author Owner
 *
 */
public class GUIMediaUtilities {

	/**
	 * Code for getting uri of a new file created for an image
	 * 
	 * CODE REUSE LonelyTweeter Camera Code from Lab 
	 * Author: Joshua Campbell 
	 * License: Unlicense 
	 * Date: Nov. 7, 2013
	 * @return 
	 */
	public Uri getUri() {

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
	public static View insertImage(Media ill, Context context) {
		Bitmap bm = Utilities.decodeSampledBitmapFromUri(Uri.parse(ill.getPath()), 
				220, 220);
		LinearLayout layout = new LinearLayout(context);

		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bm);

		layout.addView(imageView);
		return layout;
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
	public String getRealPathFromURI(Uri contentUri, Context context) {
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
		Cursor cursor = loader.loadInBackground();
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}		
}
