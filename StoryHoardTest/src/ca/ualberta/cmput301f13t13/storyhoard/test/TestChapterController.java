package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author sgil
 *
 */
public class TestChapterController extends ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private Chapter mockChapter;
	private Chapter mockChapter2;
	private Chapter mockChapter3;
	private ChapterController chapCon;
	
	public TestChapterController() {
		super(ViewBrowseStories.class);
		// TODO Auto-generated constructor stub
	}
	/* (non-Javadoc)
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public ArrayList<Chapter> getChaptersByStory(UUID storyId) {
		Chapter criteria = new Chapter(null, storyId, null);	
		return chapterMan.retrieve(criteria);
	}
	
	public void testGetAll() {
		mockChapter = new Chapter(UUID.randomUUID(), "bob went away");
		chapCon.insert(mockChapter);
		mockChapter2 = new Chapter(mockChapter.getStoryId(),
				"Lily drove");
		chapCon.insert(mockChapter2);
		mockChapter3 = new Chapter(UUID.randomUUID(), "Lily drove");
		chapCon.insert(mockChapter3);

		Chapter criteria = new Chapter(null, null, null);

		mockChapters = chapCon.retrieve(criteria);
		assertEquals(mockChapters.size(), 3);		
	}

	public void testGetFullStoryChapters(UUID storyId) {
		ArrayList<Chapter> chaps = getChaptersByStory(storyId);
		ArrayList<Chapter> fullChaps = new ArrayList<Chapter>();
		
		for (Chapter chap : chaps) {
			// Get all its choices
			chap.setChoices(choiceCon.getChoicesByChapter(chap.getId()));
			// Get all its illustrations
			chap.setIllustrations(mediaCon.getIllustrationsByChapter(chap.getId()));
			// Get all its photos
			chap.setPhotos(mediaCon.getPhotosByChapter(chap.getId()));
		}
		
		return fullChaps;
	}

	public Chapter getFullChapter(UUID chapId) {
		ArrayList<Chapter> chapters = retrieve(new Chapter(chapId, null, null, null));
		
		// Check to make sure chapter exists
		if (chapters.size() == 0) {
			return null;
		}
		Chapter chapter = (Chapter) chapters.get(0);
		chapter.setChoices(choiceCon.getChoicesByChapter(chapId));
		chapter.setIllustrations(mediaCon.getIllustrationsByChapter(chapId));
		chapter.setPhotos(mediaCon.getPhotosByChapter(chapId));
		return chapter;
	}
	
	private ArrayList<Chapter> retrieve(Chapter chapter) {
		return chapterMan.retrieve(chapter);
	}

	@Override
	public void insert(Chapter chapter) {
		chapterMan.insert(chapter);
	}

	@Override
	public void update(Chapter chapter) {
		chapterMan.update(chapter);
	}	

}
