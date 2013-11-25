package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.MediaManager;

public class ChapterController implements SHController<Chapter>{
	private static ChapterController self = null;   
	private static ChapterManager chapterMan;
	private static ChoiceManager choiceMan;
	private static MediaManager mediaMan;

	protected ChapterController(Context context) {
		chapterMan = ChapterManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
		mediaMan = MediaManager.getInstance(context);
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
			fillChapter(chap);
		}
		return fullChaps;
	}

	public Chapter getFullChapter(UUID chapId) {
		ArrayList<Chapter> chapters = retrieve(new Chapter(chapId, null, null, null));

		// Check to make sure chapter exists
		if (chapters.size() == 0) {
			return null;
		}
		
		Chapter chapter = chapters.get(0);
		fillChapter(chapter);

		return chapter;
	}
	
	private void fillChapter(Chapter chap) {
		// Get all its choices
		chap.setChoices(choiceMan.retrieve(new Choice(null, 
				chap.getId(), null, null)));
		// Get all its illustrations
		chap.setIllustrations(mediaMan.retrieve(new Media(null, 
				chap.getId(), null, Media.ILLUSTRATION, "")));
		// Get all its photos
		chap.setPhotos(mediaMan.retrieve(new Media(null, 
				chap.getId(), null, Media.PHOTO, "")));		
	}
	
	
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

	@Override
	public void remove(UUID id) {
		chapterMan.remove(id);
	}	
}
