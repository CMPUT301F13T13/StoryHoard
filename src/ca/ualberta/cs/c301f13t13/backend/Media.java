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

package ca.ualberta.cs.c301f13t13.backend;

import java.util.HashMap;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import ca.ualberta.cs.c301f13t13.backend.DBContract.MediaTable;

/**
 * @author Stephanie Gil, Ashley Brown
 *
 */
public class Media {
	private UUID id;
	private UUID chapterId;  
	private Uri uri;
	private String type;
	private Bitmap bitmap;

	// MACROS
	public static final String PHOTO = "photo";
	public static final String ILLUSTRATION = "illustration";
	public static final String SOUND = "sound";
	public static final String VIDEO = "video";
	
	// CONSTRUCTORS
	/**
	 * Initializes a new Media object without an id.
	 * 
	 * @param chapterId
	 * @param uri
	 * @param type
	 */
	public Media(UUID chapterId, Uri uri, String type) {
		this.id = UUID.randomUUID();
		this.chapterId = chapterId;
		this.uri = uri;
		this.type = type;
		this.bitmap = BitmapFactory.decodeFile(uri.getPath());
	}
	
	/**
	 * Initializes a new Media object with an id, used for making a search criteria
	 * media object or for making a media object from the database data.
	 * 
	 * @param id
	 * @param chapterId
	 * @param uri
	 * @param type
	 */
	public Media(UUID id, UUID chapterId, Uri uri, String type) {
		this.id = id;
		this.chapterId = chapterId;
		this.uri = uri;		
		this.type = type;
		if (uri != null) {
			this.bitmap = BitmapFactory.decodeFile(uri.getPath());
		}
	}
	
	// GETTERS
	
	public UUID getId() {
		return id;
	}
	
	public UUID getChapterId() {
		return this.chapterId;
	}
	
	public Uri getUri() {
		return uri;
	}
	
	public String getType() {
		return type;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	// SETTERS
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public void setChapterId(UUID chapterId) {
		this.chapterId = chapterId;
	}
	
	public void setUri(Uri uri) {
		this.uri = uri;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Returns the information of the media (id, chapterId, type) 
	 * that could be used in searching for a media in the database. This 
	 * information is returned in a HashMap where the keys are the 
	 * corresponding Media Table column names.
	 * 
	 * @return HashMap
	 */
	public HashMap<String,String> getSearchCriteria() {
		HashMap<String,String> info = new HashMap<String,String>();
		
		if (id != null)  {
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
	
	@Override
	public String toString() {
		return "Media [id=" + id + ", chapter_id=" + chapterId + ", uri=" + uri.toString() 
				+ ", type=" + type + "]";
	}	
}
