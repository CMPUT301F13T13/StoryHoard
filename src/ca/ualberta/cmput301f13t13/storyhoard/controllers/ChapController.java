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
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;

public class ChapController {
	private static MediaManager mediaMan;
	private static ChapterManager chapMan;
	private static ChoiceManager choiceMan;
	private static Syncher syncher;
	private static Chapter chapter;
	private static ChapController self = null;
	
	protected ChapController(Context context) {
		mediaMan = MediaManager.getInstance(context);
		syncher = Syncher.getInstance(context);
		chapMan = ChapterManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
		chapter = new Chapter(null, null, "");  // blank chapter
	}
	
	public static ChapController getInstance(Context context) {
		if (self == null) {
			self = new ChapController(context);
		}
		return self;
	}
	
	public void setCurrChapter(UUID id) {
		chapter = getFullChapter(id);
	}
	
	private Chapter getFullChapter(UUID chapId) {
		ArrayList<Chapter> chapters = chapMan.retrieve(new Chapter(chapId, null, null, null));

		// Check to make sure chapter exists
		if (chapters.size() == 0) {
			return null;
		}
		
		Chapter chapter = chapters.get(0);
		chapter.setChoices(choiceMan.retrieve(new Choice(null, 
				chapter.getId(), null, null)));
		chapter.setIllustrations(mediaMan.retrieve(new Media(null, 
				chapter.getId(), null, Media.ILLUSTRATION, "")));
		chapter.setPhotos(mediaMan.retrieve(new Media(null, 
				chapter.getId(), null, Media.PHOTO, "")));	
		return chapter;
	}
	
	public void setCurrChapterComplete(Chapter aChapter) {
		chapter = aChapter;
	}	
	
	public Chapter getCurrChapter() {
		return chapter;
	}
	
	public void editText(String text) {
		chapter.setText(text);
	}
	
	public void editRandomChoice(Boolean bool) {
		chapter.setRandomChoice(bool);
	}
	
	public void removeIllustration(Media ill) {
		chapter.getIllustrations().remove(ill);
	}
	
	/**
	 * Adds a choice to the chapter.
	 * 
	 * @param choice
	 */
	public void addChoice(Choice choice) {
		chapter.getChoices().add(choice);
	}

	public void addRandomChoice() {
		chapter.getChoices().add(choiceMan.getRandomChoice(chapter.getId()));
	}

	public void addMedia(Media photo) {
		if (photo.getType() == Media.PHOTO) {
			chapter.getPhotos().add(photo);
			mediaMan.insert(photo);
		} else {
			chapter.getIllustrations().add(photo);
		}
	}
	
	public void pushChangesToDb() {
		chapMan.syncChapter(chapter);
		syncher.syncChapterParts(chapter);
	}
}
