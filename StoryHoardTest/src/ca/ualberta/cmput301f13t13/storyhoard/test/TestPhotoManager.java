/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestPhotoManager extends ActivityInstrumentationTestCase2<StoryHoardActivity>{

	
	public TestPhotoManager() {
		super(StoryHoardActivity.class);
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
