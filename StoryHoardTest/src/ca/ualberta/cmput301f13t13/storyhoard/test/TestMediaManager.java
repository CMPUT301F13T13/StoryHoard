/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.*;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author Owner
 *
 */
public class TestMediaManager extends ActivityInstrumentationTestCase2<ViewChapterActivity>{

	public TestMediaManager() {
		super(ViewChapterActivity.class);
	}
	
	/**
	 * Tests saving and loading photo.
	 */ 
	public void testSaveLoadPhoto() {
		Uri ImageFileUri = "./hereitis.jpg";
		Chapter chap = new Chapter(UUID.randomUUID(), "empty chapter");
		MediaManager pm = new MediaManager();
		pm.savePhoto(ImageFileUri, chap);
		
		// loads all photos of a chapter
		ArrayList<ImageFileUri> photos = pm.loadPhotos(chap.getId());
		assertEquals(photos.size(), 1);
	}
	
	/**
	 * Tests posting a photo to the current segment/chapter/page/whatever
	 */
	public void testPostPhoto() {
		// Photo photo = new Photo();
		 int chapterId = 0;
		 MediaManager mm = new MediaManager();
		 try {
			 mm.postPhoto(photo, chapterId);
		 } catch (Exception e) {
			 fail("Could not post photo: " + e.getStackTrace());
		 }
	}
	
	/**
	 * Tests taking a photo
	 */
	public void testTakePhoto(){
		MediaManager mm = new MediaManager();
		mm.takePhoto();
	}
}
