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
		Uri ImageFileUri = "./hereitis.jpg" 
		Chapter chap = new Chapter();
		PhotoManager pm = new PhotoManager();
		pm.savePhoto(ImageFileUri, chap);
		
		// loads all photos of a chapter
		ArrayList<ImageFileUri> photos = pm.loadPhotos(chap.getId());
		assertEquals(photos.size(), 1);
	}
	
	/**
	 * Tests posting a photo to the current segment/chapter/page/whatever
	 */
	public void testPostPhoto() {
		 Photo photo = new Photo();
		 int chapterId = 0;
		 PhotoManager pm = new PhotoManager();
		 try {
			 pm.postPhoto(photo, chapterId);
		 } catch (Exception e) {
			 fail("Could not post photo: " + e.getStackTrace());
		 }
	}
	
	/**
	 * Tests taking a photo
	 */
	public void testTakePhoto(){
		PhotoManager pm = new PhotoManager();
		pm.takePhoto();
	}
}
