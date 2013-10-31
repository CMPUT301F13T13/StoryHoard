/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

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
	 * @throws URISyntaxException 
	 */ 
	public void testSaveLoadPhoto() throws URISyntaxException {
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		URI ImageFileUri = new URI("https://www.google.ca");
		Chapter chap = new Chapter(UUID.randomUUID(), "empty chapter");
		MediaManager mm = MediaManager.getInstance(this.getActivity());
		mm.insert(ImageFileUri, helper);
		
		// loads all photos of a chapter
		ArrayList<Object> photos = mm.retrieve(chap.getId(), helper);
		assertEquals(photos.size(), 1);
	}
	
//	/**
//	 * Tests posting a photo to the current segment/chapter/page/whatever
//	 */
//	public void testPostPhoto() {
//		// Photo photo = new Photo();
//		 int chapterId = 0;
//		 MediaManager mm = MediaManager.getInstance(this.getActivity());
//		 try {
//			 mm.postPhoto(photo, chapterId);
//		 } catch (Exception e) {
//			 fail("Could not post photo: " + e.getStackTrace());
//		 }
//	}
	
//	 /**
//	 * Tests saving and loading a chapter that has media locally.
//	 */
//	public void testAddLoadChapterMedia() {
//		// TO DO: Add media
//		ChapterManager cm = new ChapterManager(this.getActivity());
//		newMockChapter(UUID.randomUUID(), "it is raining");
//		// Give it photos/illustrations
//		
//		DBHelper helper = DBHelper.getInstance(this.getActivity());
//		cm.insert(mockChapter, helper);
//		mockChapters = cm.retrieve(mockChapter, helper);
//		assertTrue(mockChapters.size() != 0);
//		assertTrue(hasChapter(mockChapters, mockChapter));
//	}
	
	/**
	 * Tests updating a chapter's data except for media,
	 * which includes adding and loading a chapter. 
	 */
	public void testUpdateChapterMedia() {
		// ADD MEDIA
		Chapter chapter = new Chapter(UUID.randomUUID(), "the blue cow mood");
		ChapterManager cm = ChapterManager.getInstance(this.getActivity());
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		Chapter mockChapter = new Chapter(UUID.randomUUID(), "hi there");
		cm.insert(mockChapter, helper);
		
		ArrayList<Object>mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, mockChapter));
		
		Chapter newChapter = (Chapter) mockChapters.get(0);
		
//		newChapter.setText("My Wizard newt");
		cm.update(newChapter, helper);
		
		// make sure you can find new story
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() != 0);
		assertTrue(hasChapter(mockChapters, newChapter));
		
		// make sure old version no longer exists
		mockChapters = cm.retrieve(mockChapter, helper);
		assertTrue(mockChapters.size() == 0);	
	}		
	
	/**
	 * Tests edit illustration
	 * @throws URISyntaxException 
	 */
	public void testEditIllustration() throws URISyntaxException {
		//Get existing chapter from ChapterManager
		MediaManager mm = MediaManager.getInstance(this.getActivity());
		URI uri = new URI("https://www.google.ca");
		UUID chapId = UUID.randomUUID();
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		
		mm.insert(uri, helper);
		
		// TODO replace uri with Media object
		ArrayList<Object> uris = mm.retrieve(uri, helper);
		
		assertSame((URI) uris.get(0), uri);
		
		//Replace existing illustration with new one
		URI newUri = new URI("https://www.ualberta.ca");
		mm.update(uri, helper); // TODO only need one uri to update
		uris = mm.retrieve(uri, helper);
		newUri = (URI) uris.get(0);
		
		assertFalse(uri != newUri);
	}	
	
//	/**
//	 * Tests taking a photo
//	 */
//	public void testTakePhoto(){
//		MediaManager mm = new MediaManager();
//		mm.takePhoto();
//	}
	
	/**
	 * Checks whether a chapter is contained in a chapters ArrayList.
	 * 
	 * @param objs
	 * 			ArrayList of objects.
	 * @param chap
	 * 			Object for which we are testing whether or not it is 
	 * 			contained in the ArrayList.
	 * @return Boolean
	 */
	public Boolean hasChapter(ArrayList<Object> objs, Chapter chap) {
		for (Object object : objs) {
		    Chapter newChap = (Chapter) object;
		    if (newChap.getId().equals(chap.getId())) {
		    	return true;
		    }
		}		
		return false;
	}		
}
