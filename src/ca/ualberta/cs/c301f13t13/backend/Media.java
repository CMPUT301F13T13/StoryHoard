/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.UUID;

import android.graphics.Bitmap;

/**
 * @author Stephanie Gil, Ashley Brown
 *
 */
public class Media {
	private UUID id;
	private UUID chapter_id;
	private String uri;
	private Bitmap bitmap;
	private int type;
	
	// MACROS
	public static final int PHOTO = 0;
	public static final int ILLUSTRATION = 1;
	
	public UUID getId() {
		return id;
	}
	
	public void setId(UUID id) {
		this.id = id;
	}
	
	public UUID getChapter_id() {
		return chapter_id;
	}
	
	public void setChapter_id(UUID chapter_id) {
		this.chapter_id = chapter_id;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
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
	
	
	
	
	
}
