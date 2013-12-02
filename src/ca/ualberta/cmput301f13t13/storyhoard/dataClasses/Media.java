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

package ca.ualberta.cmput301f13t13.storyhoard.dataClasses;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Role: A data class to hold media information. The media can be a photo or 
 * an illustration. Instead of holding an actual bitmap as a field, this class 
 * holds the path to the bitmap on external memory, and also has a field for 
 * the compressed string version of the bitmap (bitmapString) that is the base 
 * 64 string version of the bitmap. This is done so we have a way of storing 
 * the bitmap information onto the server. </br></br>
 * 
 * Also, a media object will always belong to a specific chapter, and no media
 * can belong to more than one chapter.
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
	private String text;
	public static final String PHOTO = "photo";
	public static final String ILLUSTRATION = "illustration";

	/**
	 * Initializes a new Media object without needing to specify an id since a
	 * random one will be generated on the media's creation. This is what 
	 * should be used to create a new Media object that is NOT holding search 
	 * criteria and is NOT being created from data just retrieved from the 
	 * database. 
	 * 
	 * @param chapterId
	 * 			Must be a UUID. An example of a UUID's format as a string is:
	 * 			</br> "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 * @param path
	 * 			A string that contains the path to the image on the phone's
	 * 			SD card.
	 * @param type
	 * 			The type of Media. The application currently only supports
	 * 			two types: "photo" and "illustration".
	 * @param text 
	 * 			Any text associated with the media. This is currently only
	 * 			used for photos, not illustrations, as only photos can
	 * 			carry a text comment with them.
	 */
	public Media(UUID chapterId, String path, String type, String text) {
		this.id = UUID.randomUUID();
		this.chapterId = chapterId;
		this.type = type;
		this.text = text;
		bitmapString = "";
		if (path != null) {
			this.path = path;
		} else {
			this.path = "";
		}
	}

	/**
	 * Initializes a new media object by specifying its id as the first  
	 * parameter. Usually used when making a media object that will be 
	 * holding search criteria since you can specify the id of the media 
	 * that you would like to search for. When making a media to hold 
	 * search criteria, any field you don't want to include in the search 
	 * has to be set to null.</br></br>
	 * 
	 * This constructor is also used to create a new chapter object from
	 * data which has been retrieved from the database. </br></br>
	 * 
	 * Example: </br>
	 * If you want to search for the Media that has the id of 
	 * "5231b533-ba17-4787-98a3-f2df37de2aD7", you would create the search 
	 * criteria holding Media objects as follows: </br>
	 * UUID id = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");</br>
	 * Media criteria = new Media(id, null, null, null, null); </br>
	 * 
	 * 
	 * @param id
	 * 			Must be a UUID. It is the unique identifier of the media object
	 * 			in the database.
	 * @param chapterId
	 * 			Must be a UUID. It is the id of the chapter to which the media
	 * 			belongs to.
	 * @param path
	 *			A string that contains the path to the image on the phone's
	 * 			SD card.	
	 * @param type
	 * 			The type of Media. The application currently only supports
	 * 			two types: "photo" and "illustration".
	 * @param text 
	 * 			Any text associated with the media. This is currently only
	 * 			used for photos, not illustrations, as only photos can
	 * 			carry a text comment with them.
	 */
	public Media(UUID id, UUID chapterId, String path, String type, String text) {
		this.id = id;
		this.chapterId = chapterId;
		this.type = type;
		this.text = text;
		bitmapString = "";
		if (path != null) {
			this.path = path;		
		} else {
			this.path = "";	
		}
	}

	// GETTERS

	/**
	 * Returns the id of the media as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * UUID id = myMedia.getId();</br>
	 * System.out.println(id);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Returns the chapter id the media belongs to as a UUID. </br></br>
	 * 
	 * Example:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * UUID newChapId = myMedia.getChapterId();</br>
	 * System.out.println(newChapId);</br></br>
	 * 
	 * Output would be something like: "5231b533-ba17-4787-98a3-f2df37de2aD7"
	 */
	public UUID getChapterId() {
		return this.chapterId;
	}

	/**
	 * Returns the path of the media as a String. This indicates where the 
	 * bitmap associated with the media is stored. It is also assumed the 
	 * user has not manually deleted or moved the bitmap from its original 
	 * location. </br></br>
	 * 
	 * Example:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * String path = myMedia.getPath();</br>
	 * System.out.println(path);</br></br>
	 * 
	 * Output would be like: "sd://resources/img1.jpg"
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Returns the media's type ("photo" or "illustration") as a String. 
	 * </br></br>
	 * 
	 * Example:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * String photo = myMedia.getType();</br>
	 * System.out.println(type);</br></br>
	 * 
	 * Output would be like: "photo"
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the media's bitmap. It decodes the path that is has stored using 
	 * the BitmapFactory decodeFile() method. The bitmap is also shrunken 
	 * before being returned. This is to be able to put it on the server with 
	 * less of a hassle. </br></br>
	 * 
	 * Example call:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * Bitmap bitmap = myMedia.getBitmap();</br>
	 */
	public Bitmap getBitmap() {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		Bitmap resizedbmp = Bitmap.createScaledBitmap(bitmap, 125, 125, false);
		return resizedbmp;
	}
	
	/**
	 * Returns the media's bitmap as an encoded String. This method assumes the 
	 * bitmapString field has already been filled. If not, an empty string would 
	 * be returned since that is the default value of bitmapString when the 
	 * media object is initialized. </br></br>
	 * 
	 * Example call:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * String bmString = myMedia.getBitmapString();</br>
	 */
	public String getBitmapString() {
		return bitmapString;
	}	
	
	/**
	 * Returns the media's bitmap, but does so by converting the bitmapString 
	 * from string to bitmap. In other words, the path field is not used at 
	 * all. This method also assumes the bitmapString has been set to something 
	 * valid, else null will be returned. Note that this is an expensive 
	 * operation only used when downloading a story from the server since the 
	 * path variable in that case is useless to us.</br></br>
	 * 
	 * Example call:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * Bitmap bitmap = myMedia.getBitmapFromString();</br>
	 */
	public Bitmap getBitmapFromString() {
		return getBitmapFromString(bitmapString);
	}	

	/**
	 * Returns the media's text. This is only important for illustrations since 
	 * users have the option of adding text to the photos they post to annotate 
	 * a chapter.
	 * </br></br>
	 * 
	 * Example:</br>
	 * UUID chapId = UUID.randomUUID(); </br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * String text = myMedia.getText();</br>
	 * System.out.println(text);</br></br>
	 * 
	 * Output would be like: "hi"
	 * 
	 */
	public String getText() {
		return text;
	}
	// SETTERS

	/**
	 * Sets the Id of the media. The new Id provided must be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * UUID id = UUID.randomUUID();</br>
	 * myMedia.setId(id);</br>
	 * 
	 * @param id
	 * 			New Media id. Must be a UUID or null.
	 */
	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Sets the Id of the chapter the media belongs to. The new Id provided must 
	 * be a UUID.</br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * UUID chapId = UUID.randomUUID();</br>
	 * myMedia.setChapterId(id);</br>
	 * 
	 * @param id
	 * 			New chapter id. Must be a UUID or null.
	 */
	public void setChapterId(UUID chapterId) {
		this.chapterId = chapterId;
	}

	/**
	 * Sets the path to the image file of the Media object. The path will 
	 * should be a valid path to some file in the phone's SD card. </br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Media myMedia = (chapId, "", "photo", "hi");</br>
	 * myMedia.setPath("sd://resources/img1.jpg");</br>
	 * 
	 * @param path
	 * 			String containing valid path to image file.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Sets the bitmapString of the media. What this is, is the bitmap  
	 * associated with the Media object, but converted into a base 64 string.  
	 * If a Chapter has a media, either a photo or illustration, this field is  
	 * needed filled out when a story is sent to the server since the path  
	 * becomes useless for stories on the server.</br></br>
	 * 
	 * This method actually takes a bitmap and does the conversion itself 
	 * using the getStringFromBitmap() method, so no need to do the conversion 
	 * yourself. </br></br>
	 * 
	 * Example call:</br>
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "photo", "hi");</br>
	 * myMedia.setBitmapString(myMedia.getBitmap());</br>
	 * 
	 * @param bitmap
	 * 			Valid bitmap, should never be null.
	 */
	public void setBitmapString(Bitmap bitmap) {
		this.bitmapString = getStringFromBitmap(bitmap);
	}
	
	/**
	 * Sets the type of media the object is. Will either be "photo" or 
	 * "illustration". </br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", "", "hi");</br>
	 * myMedia.setType("illustration);</br>
	 * 
	 * @param type
	 * 			String saying whether the media is an illustration or photo.  
	 * 			You can also use the constant defined within the Media class,  
	 * 			example Media.PHOTO and Media.ILLUSTRATION. 
	 */ 	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Sets the text that goes along with the media. This should only ever be 
	 * set for a media of type "photo", since only photos contain text 
	 * comments. </br></br>
	 * 
	 * Example call:</br>
	 * 
	 * Media myMedia = (chapId, "sd://resources/img1.jpg", Media.Photo, "");</br>
	 * myMedia.setText("this is text");</br>
	 * 
	 * @param text
	 * 			String containing the text that the user inputed along with 
	 * 			his photo.
	 */ 	
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * This functions converts Bitmap picture to a string so that the media 
	 * object can be converted to a JSON string and placed in the server. 
	 * Should only be used by the Media object itself.
	 * </br></br>
	 * 
	 * CODE REUSE: </br>
	 * This code is taken straight from: </br>
	 * URL: http://mobile.cs.fsu.edu/converting-images-to-json-objects/ </br>
	 * Date: Nov. 4th, 2013 </br>
	 * Author: Manav </br>
	 */
	private String getStringFromBitmap(Bitmap bitmapPicture) {
		final int COMPRESSION_QUALITY = 100;
		ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
		bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 
				COMPRESSION_QUALITY, byteArrayBitmapStream);
		byte[] b = byteArrayBitmapStream.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}	

	/**
	 * This functions converts a base 64 string to a Bitmap. This is used 
	 * whenever story is retrieved from the server since all of its 
	 * media objects must have the bitmaps they are associated with 
	 * saved onto the SD card and have a path saved into the atcual media
	 * object. </br></br>
	 * 
	 * CODE REUSE: </br>
	 * This code is taken straight from: </br>
	 * URL: http://mobile.cs.fsu.edu/converting-images-to-json-objects/ </br>
	 * Date: Nov. 4th, 2013 </br>
	 * Author: Manav </br>
	 */
	private Bitmap getBitmapFromString(String string) {
		byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 
				0, decodedString.length);
		return decodedByte;
	}
}
