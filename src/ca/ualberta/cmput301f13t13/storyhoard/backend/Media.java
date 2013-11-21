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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract.MediaTable;

/**
 * Role: A container to hold media information. The media can be a photo or
 * an illustration.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 */
public class Media {
	private UUID id;
	private UUID chapterId;
	private String path;
	private String type;
	private String bitmapString;

	// CONSTANTS
	public static final String PHOTO = "photo";
	public static final String ILLUSTRATION = "illustration";

	/**
	 * Initializes a new Media object without an id.
	 * 
	 * @param chapterId
	 * @param path
	 * @param type
	 */
	public Media(UUID chapterId, String path, String type) {
		this.id = UUID.randomUUID();
		this.chapterId = chapterId;
		this.type = type;
		bitmapString = "";
		if (path != null) {
			this.path = path;
		} else {
			this.path = "";
		}
	}

	/**
	 * Initializes a new Media object with an id, used for making a search
	 * criteria media object or for making a media object from the database
	 * data.
	 * 
	 * @param id
	 * @param chapterId
	 * @param path
	 * @param type
	 */
	public Media(UUID id, UUID chapterId, String path, String type) {
		this.id = id;
		this.chapterId = chapterId;
		this.type = type;
		bitmapString = "";
		if (path != null) {
			this.path = path;		
		} else {
			this.path = "";	
		}
	}

	// GETTERS

	/**
	 * Returns the id of the media.
	 * 
	 * @return id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the chapter id the media belongs to.
	 * 
	 * @return chapterId
	 */
	public UUID getChapterId() {
		return this.chapterId;
	}

	/**
	 * Returns the path of the media.
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the media's type (PHOTO, ILLUSTRATION, AUDIO, or VIDEO).
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the media's bitmap.
	 * 
	 * @return bitmap
	 */
	public Bitmap getBitmap() {
		return BitmapFactory.decodeFile(path);
	}
	
	/**
	 * Returns the media's bitmap as an encoded String
	 * 
	 * @return bitmap string
	 */
	public String getBitmapString() {
		return bitmapString;
	}	
	
	/**
	 * Returns the media's bitmap, but does so by converting the bitmap
	 * from string to bitmap.
	 * 
	 * @return bitmap 
	 */
	public Bitmap getBitmapFromString() {
		return getBitmapFromString(bitmapString);
	}	

	// SETTERS

	/**
	 * Sets the media's id.
	 * 
	 * @param id
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the chapter id the media belongs to.
	 * 
	 * @param chapterId
	 */
	public void setChapterId(UUID chapterId) {
		this.chapterId = chapterId;
	}

	/**
	 * Sets the path of the media. Also updates the bitmap.
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Sets the media's bitmapString from a bitmap.
	 * 
	 * @param bitmap
	 */
	public void setBitmapString(Bitmap bitmap) {
		this.bitmapString = getStringFromBitmap(bitmap);
	}
	
	/**
	 * Sets the media's type.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the information of the media (id, chapterId, type) that could be
	 * used in searching for a media in the database. This information is
	 * returned in a HashMap where the keys are the corresponding Media Table
	 * column names.
	 * 
	 * @return HashMap
	 */
	public HashMap<String, String> getSearchCriteria() {
		HashMap<String, String> info = new HashMap<String, String>();

		if (id != null) {
			info.put(MediaTable.COLUMN_NAME_MEDIA_ID, id.toString());
		}

		if (chapterId != null) {
			info.put(MediaTable.COLUMN_NAME_CHAPTER_ID, chapterId.toString());
		}

		if (type != null) {
			info.put(MediaTable.COLUMN_NAME_TYPE, type);
		}

		return info;
	}
	
	/**
	 * This functions converts Bitmap picture to a string which can be
	 * JSONified.
	 * 
	 * CODE REUSE:
	 * This code is taken straight from:
	 * 
	 * URL: http://mobile.cs.fsu.edu/converting-images-to-json-objects/
	 * Date: Nov. 4th, 2013
	 * Author: Manav
	 */
	private String getStringFromBitmap(Bitmap bitmapPicture) {
		final int COMPRESSION_QUALITY = 100;
		String encodedImage;
		ByteArrayOutputStream byteArrayBitmapStream 
		= new ByteArrayOutputStream();
		bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 
				COMPRESSION_QUALITY, byteArrayBitmapStream);
		byte[] b = byteArrayBitmapStream.toByteArray();
		encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		return encodedImage;
	}	

	/**
	 * This functions converts a string to a Bitmap picture.
	 * 
	 * CODE REUSE:
	 * This code is taken straight from:
	 * 
	 * URL: http://mobile.cs.fsu.edu/converting-images-to-json-objects/
	 * Date: Nov. 4th, 2013
	 * Author: Manav
	 */
	private Bitmap getBitmapFromString(String string) {

		byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 
				0, decodedString.length);
		return decodedByte;
	}

	public void updateSelf(Context context) {
		MediaManager mm = MediaManager.getInstance(context);
		mm.update(this);
	}

	public void addSelf(Context context) {
		MediaManager mm = MediaManager.getInstance(context);
		if (!bitmapString.equals("")) {
			path = Utilities.saveImageToSD(getBitmapFromString());
			bitmapString = "";
		}
		mm.insert(this);
	}	
}
