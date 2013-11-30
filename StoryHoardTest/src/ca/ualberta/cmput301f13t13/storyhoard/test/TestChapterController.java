package ca.ualberta.cmput301f13t13.storyhoard.test;
//package ca.ualberta.cmput301f13t13.storyhoard.test;
//
//import java.util.ArrayList;
//import java.util.UUID;
//
//import android.content.pm.ActivityInfo;
//import android.test.ActivityInstrumentationTestCase2;
//import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
//import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
//import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
//import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
//
//public class TestChapController extends ActivityInstrumentationTestCase2<ActivityInfo> {
//
//	public TestChapController() {
//		super(ActivityInfo.class);
//	}
//
//	/**
//	 * Tests adding a choice to a chapter.
//	 */
//	public void testAddChoice() {
//		
//		Chapter chapter = new Chapter(UUID.randomUUID(), "hello there");
//		Choice choice = new Choice(chapter.getId(), UUID.randomUUID(), "rawr");
//		chapter.addChoice(choice);
//
//		assertEquals(chapter.getChoices().size(), 1);
//	}
//	
//	/**
//	 * Tests converting all the bitmaps of the chapter's media into string
//	 * format, base 64 so they can be placed into the server.
//	 */
//	public void testPrepareMedia() {
//		String path = BogoPicGen.createPath("img1.jpg");
//		Media photo = new Media(UUID.randomUUID(), path, Media.PHOTO);
//		Chapter mockChapter = new Chapter(UUID.randomUUID(), "chap texty");
//		mockChapter.addPhoto(photo);
//
//		mockChapter.prepareMediasForServer();
//		ArrayList<Media> photos = mockChapter.getPhotos();
//		photo = photos.get(0);
//
//		assertFalse(photo.getBitmapString().equals(""));
//	}	
//	
//	/**
//	 * Tests getting a full chapter back from the database (including choices
//	 * and media).
//	 */
//	public void testGetFullChapter() {
//		mockChapter = new Chapter(UUID.randomUUID(), "bob went away");
//		Choice c1 = new Choice(mockChapter.getId(), UUID.randomUUID(), "c1");
//		Media m1 = new Media(mockChapter.getId(), null, Media.ILLUSTRATION);
//		choiceCon.insert(c1);
//		mediaCon.insert(m1);
//		chapCon.insert(mockChapter);
//
//		Chapter newChapter = chapCon.getFullChapter(mockChapter.getId());
//		assertEquals(newChapter.getChoices().size(), 1);
//		assertEquals(newChapter.getIllustrations().size(), 1);
//		assertTrue(mockChapter.getText().equals(newChapter.getText()));
//	}	
//}
