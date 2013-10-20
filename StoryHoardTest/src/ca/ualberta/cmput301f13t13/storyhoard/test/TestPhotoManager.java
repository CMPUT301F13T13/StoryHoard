/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Owner
 *
 */
public class TestPhotoManager {

	
	public TestPhotoManager() {
		PhotoManager pm = new PhotoManager();
	}
	
	/**
	 * Tests saving and loading photo.
	 */
	public void testSaveLoadPhoto() {
		Photo photo = new Photo();
		int chapterId = 0;
		PhotoManager pm = new PhotoManager();
		pm.savePhoto(photo, chapterId);
		ArrayList<Photo> photos = pm.loadPhotos(chapterId);
		assertEquals(photos.size(), 1);
	}
}
