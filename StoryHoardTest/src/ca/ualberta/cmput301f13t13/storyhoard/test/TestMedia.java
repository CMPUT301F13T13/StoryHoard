/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.UUID;

import android.graphics.Bitmap;
import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cs.c301f13t13.backend.*;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the Media class in the StoryHoard application.
 * 
 * @author Owner
 * @see Media
 */
public class TestMedia extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private static final Uri uri = Uri.parse("https://raw.github.com/CMPUT301F13T13/StoryHoard/master/mockups/all_chapters.png");

	public TestMedia() {
		super(ViewBrowseStories.class);
	}

	/**
	 * Tests creating a media object.
	 */
	public void testCreateMedia() {
		// Make photo
		try {
			Media photo = new Media(UUID.randomUUID(), uri, Media.PHOTO);
			assertTrue(photo.getBitmap() != null);
		} catch (Exception e) {
			fail("error creating a new media object");
		}

		// Make illustration
		try {
			Media ill = new Media(UUID.randomUUID(), uri, Media.ILLUSTRATION);
			assertTrue(ill.getBitmap() != null);
		} catch (Exception e) {
			fail("error creating a new media object");
		}
	}

	/**
	 * Tests the setters and getters of a Media object.
	 */
	@SuppressWarnings("unused")
	public void testSettersGetters() {
		Media photo = new Media(UUID.randomUUID(), uri, Media.PHOTO);

		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		Bitmap bm = photo.getBitmap();

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);
		photo.setUri(Uri.parse("https://raw.github.com/CMPUT301F13T13/StoryHoard/master/mockups/published_stories.jpg"));

		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertNotSame(type, photo.getType());
		assertTrue(photo.getBitmap() != null);
		assertTrue(photo.getUri() != uri);
	}
}
