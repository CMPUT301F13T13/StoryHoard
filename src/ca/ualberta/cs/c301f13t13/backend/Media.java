/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * @author Stephanie Gil, Ashley Brown
 *
 */
public class Media {
	private UUID id;
	private UUID chapterId;
	private Uri uri;
	private int type;
	private Bitmap bitmap;

	// MACROS
	public static final int PHOTO = 0;
	public static final int ILLUSTRATION = 1;
	public static final int SOUND = 2;
	public static final int VIDEO = 3;
	
	// CONSTRUCTORS
	/**
	 * Initializes a new Media object without an id.
	 * 
	 * @param uri
	 * @param type
	 */
	public Media(UUID chapterId, Uri uri, int type) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.uri = uri;
		this.bitmap = BitmapFactory.decodeFile(this.uri.getPath());
	}
	
	/**
	 * Initializes a new Media object with an id, used for making a search criteria
	 * media object or for making a media object from the database data.
	 * 
	 * @param uri
	 * @param type
	 */
	public Media(UUID id, UUID chapterId, Uri uri, int type) {
		this.id = UUID.randomUUID();
		this.type = type;
		this.uri = uri;
		this.bitmap = BitmapFactory.decodeFile(this.uri.getPath());
	}
	
	// GETTERS
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public UUID getChapter_id() {
		return chapterId;
	}
	
	public void setChapter_id(UUID chapter_id) {
		this.chapterId = chapter_id;
	}
	
	public Uri getUri() {
		return uri;
	}
	
	public void setUri(Uri uri) {
		this.uri = uri;
	}
	
	// SETTERS
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public static int getPhoto() {
		return PHOTO;
	}
	
	public static int getIllustration() {
		return ILLUSTRATION;
	}
	
	@Override
	public String toString() {
		return "Media [id=" + id + ", chapter_id=" + chapterId + ", uri=" + uri.toString() 
				+ ", type=" + type + "]";
	}	
}
