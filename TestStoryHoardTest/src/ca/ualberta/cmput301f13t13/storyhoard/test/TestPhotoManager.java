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
	}
	
	/**
	 * Tests saving and loading photo.
	 */
	public void testSaveLoadPhoto(Photo photo, Segment segment) {
		PhotoManager pm = new PhotoManager();
		pm.savePhoto(photo, segment);
		ArrayList<Photo> photos = pm.loadPhotos(segment);
		assertEquals(photos.size(), 1);
	}
}
