/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
		return mediaMan.retrieve(new Media(null, chapterId, null, Media.PHOTO, ""));		
	}	
	
	public ArrayList<Media> getIllustrationsByChapter(UUID chapterId) {
		return mediaMan.retrieve(new Media(null, chapterId, null, Media.ILLUSTRATION, ""));		
	}		
	
	@Override
	public ArrayList<Media> getAll() {
		return mediaMan.retrieve(new Media(null, null, null, null, null));
	}

	@Override
	public void update(Media media) {
		mediaMan.syncMedia(media);
	}
	
	@Override
	public void remove(UUID id) {
		mediaMan.remove(id);
	}
}
