package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.local.MediaManager;

public class MediaController implements SHController<Media>{
	private static MediaController self = null;   
	private static MediaManager mediaMan;

	protected MediaController(Context context) {
		mediaMan = MediaManager.getInstance(context);
	}
	
	public static MediaController getInstance(Context context) {
		if (self == null) {
			self = new MediaController(context);
		}
		return self;
	}

	public ArrayList<Media> getPhotosByChapter(UUID chapterId) {
		return mediaMan.retrieve(new Media(null, chapterId, null, Media.PHOTO));		
	}	
	
	public ArrayList<Media> getIllustrationsByChapter(UUID chapterId) {
		return mediaMan.retrieve(new Media(null, chapterId, null, Media.ILLUSTRATION));		
	}		
	
	@Override
	public ArrayList<Media> getAll() {
		return mediaMan.retrieve(new Media(null, null, null));
	}

	@Override
	public void insert(Media media) {
		mediaMan.insert(media);
	}

	@Override
	public void update(Media media) {
		mediaMan.update(media);
	}
	
	@Override
	public void remove(UUID id) {
		mediaMan.remove(id);
	}
}
