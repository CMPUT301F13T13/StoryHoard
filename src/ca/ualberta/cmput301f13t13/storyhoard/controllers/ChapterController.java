package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ChapterManager;

public class ChapterController implements SHController<Chapter>{
	private static ChapterController self = null;   
	private static ChapterManager chapterMan;
	private static ChoiceController choiceCon;
	private static MediaController mediaCon;

	protected ChapterController(Context context) {
		chapterMan = ChapterManager.getInstance(context);
		choiceCon = ChoiceController.getInstance(context);
		mediaCon = MediaController.getInstance(context);
	}
	
	public static ChapterController getInstance(Context context) {
		if (self == null) {
			self = new ChapterController(context);
		}
		return self;
	}
	
	/**
	 * Retrieves all the chapters that are in a given story.
	 * 
	 * @param storyId
	 *            Id of the story the chapters are wanted from.
	 * 
	 * @return ArrayList of the chapters.
	 */
	public ArrayList<Chapter> getChaptersByStory(UUID storyId) {
		Chapter criteria = new Chapter(null, storyId, null);	
		return chapterMan.retrieve(criteria);
	}

	@Override
	public ArrayList<Chapter> getAll() {
		return chapterMan.retrieve(new Chapter(null, null, null));
	}

	public ArrayList<Chapter> getFullStoryChapters(UUID storyId) {
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
	
	@Override
	public ArrayList<Chapter> retrieve(Chapter chapter) {
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
