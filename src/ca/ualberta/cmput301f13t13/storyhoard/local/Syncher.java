package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

public class Syncher {
	private static StoryManager storyMan = null;
	private static ChapterManager chapMan = null;
	private static MediaManager mediaMan = null;
	private static ChoiceManager choiceMan = null;
	private static Syncher self = null;

	protected Syncher(Context context) {
		storyMan = StoryManager.getInstance(context);
		chapMan = ChapterManager.getInstance(context);
		mediaMan = MediaManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
	}

	public static Syncher getInstance(Context context) {
		if (self == null) {
			self = new Syncher(context);
		}
		return self;
	}

	public void syncStoryFromMemory(Story story) {
		storyMan.sync(story, story.getId());
		for (Chapter chap : story.getChapters()) {
			syncChapterParts(chap);
			chapMan.sync(chap, chap.getId());
		}
	}	

	public void syncChapterParts(Chapter chap) {
		ArrayList<UUID> medias = new ArrayList<UUID>();

		for (Media photo : chap.getPhotos()) {
			medias.add(photo.getId());
			mediaMan.sync(photo, photo.getId());
		}
		for (Media ill : chap.getIllustrations()) {
			medias.add(ill.getId());
			mediaMan.sync(ill, ill.getId());
		}
		for (Choice choice : chap.getChoices()) {
			choiceMan.sync(choice, choice.getId());
		}
		mediaMan.syncDeletions(medias, chap.getId());
	}		
	
	public void syncStoryFromServer(Story story) {
		ArrayList<UUID> medias = new ArrayList<UUID>();
		storyMan.sync(story, story.getId());

		for (Chapter chap : story.getChapters()) {
			for (Media photo : chap.getPhotos()) {
				medias.add(photo.getId());
				String path = Utilities.saveImageToSD(photo.getBitmapFromString());
				photo.setPath(path);
				mediaMan.sync(photo, photo.getId());
			}
			for (Media ill : chap.getIllustrations()) {
				String path = Utilities.saveImageToSD(ill.getBitmapFromString());
				ill.setPath(path);
				medias.add(ill.getId());
				mediaMan.sync(ill, ill.getId());
			}
			for (Choice choice : chap.getChoices()) {
				choiceMan.sync(choice, choice.getId());
			}
			chapMan.sync(chap, chap.getId());
			mediaMan.syncDeletions(medias, chap.getId());
		}
	}

	public ArrayList<Chapter> syncChaptersFromDb(UUID storyId) {
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
}
