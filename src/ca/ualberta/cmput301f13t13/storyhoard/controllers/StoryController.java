package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.MediaManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

public class StoryController {
	private static Story story;
	private static ChapterManager chapMan;
	private static MediaManager mediaMan;
	private static ChoiceManager choiceMan;
	private static ServerManager serverMan;
	private static Syncher syncher;
	private static StoryController self;

	protected StoryController(Context context) {
		chapMan = ChapterManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
		mediaMan = MediaManager.getInstance(context);
		syncher = Syncher.getInstance(context);
		serverMan = ServerManager.getInstance();
		story = new Story("", "", "", "");  // blank story
	}
	
	public static StoryController getInstance(Context context) {
		if (self == null) {
			self = new StoryController(context);
		}
		return self;
	}
	
	public void setCurrStoryIncomplete(Story aStory) {
		story = aStory;
		story.setChapters(getFullStoryChapters(story.getId()));
	}
	
	private ArrayList<Chapter> getFullStoryChapters(UUID storyId) {
		ArrayList<Chapter> chaps = chapMan.getChaptersByStory(storyId);
		ArrayList<Chapter> fullChaps = new ArrayList<Chapter>();
		
		for (Chapter chap : chaps) {
			chap.setChoices(choiceMan.retrieve(new Choice(null, 
					chap.getId(), null, null)));
			chap.setIllustrations(mediaMan.retrieve(new Media(null, 
					chap.getId(), null, Media.ILLUSTRATION, "")));
			chap.setPhotos(mediaMan.retrieve(new Media(null, 
					chap.getId(), null, Media.PHOTO, "")));	
			fullChaps.add(chap);
		}
		return fullChaps;
	}	
	
	public void setCurrStoryComplete(Story aStory) {
		story = aStory;
	}

	public Story getCurrStory() {
		return story;
	}


	public void editFirstChapterId(UUID id) {
		story.setFirstChapterId(id);
	}
	
	public void editTitle(String title) {
		story.setTitle(title);
	}
	
	public void editAuthor(String author) {
		story.setAuthor(author);
	}
	
	public void editDescription(String desc) {
		story.setDescription(desc);
	}
	
	/**
	 * Adds a chapter onto the story object. If the story
	 * has no chapters when the new chapter is added, the
	 * firstChapterId will also be set for the story.
	 * 
	 * If the story needs to be updated in the database
	 * (due to now having a firstChapterId), then it will
	 * return true so the front end will know to use the
	 * OwnStoryManager to update the story data.
	 * 
	 * If the story already had its firstChapterId set, then
	 * there is no need to update it in the database, therefore
	 * the function will return False.
	 */
	public void addChapter(Chapter chapter) {
		ArrayList<Chapter> chaps = story.getChapters();

		if (story.getFirstChapterId() == null) {
			story.setFirstChapterId(chapter.getId());
		}
		chaps.add(chapter);
		story.setChapters(chaps);
	}	

	/**
	 * Due to performance issues, Media objects don't actually hold Bitmaps.
	 * A path to the location of the file is instead saved and used to 
	 * retrieve bitmaps whenever needed. Media objects can also hold the
	 * bitmaps after the have been converted to a string (Base64). 
	 * </br>
	 * Before a Story can be inserted into the server, all the images 
	 * belonging to the story's chapters must have their bitmap strings
	 * set. This is only done when the story is published to avoid doing
	 * the expensive conversion unnecessarily (local stories only need to 
	 * know the path).
	 * </br>
	 * 
	 * This function takes care of setting all the Medias' bitmap strings.
	 */
	private void prepareChaptersForServer() {

		// get any media associated with the chapters of the story
		ArrayList<Chapter> chaps = story.getChapters();

		for (Chapter chap : chaps) {
			prepareMediasForServer(chap);
		}
		story.setChapters(chaps);
	}	

	private void prepareMediasForServer(Chapter chap) {
		for (Media photo : chap.getPhotos()) {
			photo.setBitmapString(photo.getBitmap());
		}

		for (Media ill : chap.getIllustrations()) {
			ill.setBitmapString(ill.getBitmap());
		}
	}	

	public void pushChangesToServer() {
		prepareChaptersForServer();
		serverMan.update(story);
	}

	public void pushChangesToDb() {
		syncher.syncStoryFromMemory(story);
	}
}
