/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.UUID;

import org.junit.Before;

import android.graphics.Bitmap;
import android.net.Uri;

import ca.ualberta.cs.c301f13t13.backend.*;
import junit.framework.TestCase;

/**
 * Class meant for the testing of the Media class in the StoryHoard application.
 * 
 * @author Owner
 * @see Media
 */
public class TestMedia extends TestCase {

	public TestMedia() {
		super();
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests creating a media object.
	 */
	@SuppressWarnings("unused")
	public void testCreateMedia() {
		// TODO: test with uri taken from TakePhoto
		Uri uri = Uri.parse("https://google.ca");
		try {
			Media photo = new Media(UUID.randomUUID(), uri, Media.PHOTO);
		} catch (Exception e) {
			fail("error creating a new media object");
		}
	}

	/**
	 * Tests the setters and getters of a Media object.
	 */
	public void testSettersGetters() {
		Uri uri = Uri.parse("https://google.ca");
		Media photo = new Media(UUID.randomUUID(), uri, Media.PHOTO);

		UUID id = photo.getId();
		UUID chapterId = photo.getChapterId();
		String type = photo.getType();
		uri = photo.getUri();
		Bitmap bm = photo.getBitmap();

		// TODO test displaying the bitmap

		photo.setId(UUID.randomUUID());
		photo.setChapterId(UUID.randomUUID());
		photo.setType(Media.ILLUSTRATION);
		photo.setUri(null);
		photo.setBitmap(null);

		assertNotSame(id, photo.getId());
		assertNotSame(chapterId, photo.getChapterId());
		assertNotSame(type, photo.getType());
		assertTrue(photo.getBitmap() == null);
		assertTrue(photo.getUri() == null);
	}

}
