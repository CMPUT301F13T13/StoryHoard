/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.ualberta.cmput301f13t13.storyhoard.controllers;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

/**
 * Role: Responsible for manipulating the story model. It keeps a reference to 
 * the current story model as a field and updates that whenever the user 
 * makes changes. Every time the user creates a new story or saves his changes
 * to one, the changes are pushed to the database, and whenever the story is
 * published, changes are saved to the server.</br></br>
 * 
 * Design Pattern: This class is a singleton, so only one instance of it will
 * ever exist. use getInstance() to retrieve that instance, not the 
 * constructor.
 * 
 * @author Stephanie Gil
 *
 */
public class StoryController {
	private static Story story;
	private static ServerManager serverMan;
	private static Syncher syncher;
	private static StoryController self;

	/**
	 * Initializes a new StoryController. Needs context so it is able to 
	 * initialize its Syncher and ServerManager to be able to push changes to
	 * the database and server. Also initializes a blank story to avoid
	 * null pointer exceptions if methods are called on a null story. This 
	 * constructor should never be used outside of this class (except for any 
	 * subclasses).
	 * 
	 * @param context
	 */
	protected StoryController(Context context) {
		syncher = Syncher.getInstance(context);
		serverMan = ServerManager.getInstance();
		story = new Story("", "", "", "");  	// blank story
	}
	
	/**
	 * Returns an instance of a StoryController. The same instance is always 
	 * returned since it is a singleton. This is the method every other class
	 * should be using to access the StoryController. Note that this method
	 * must also be called statically. </br></br>
	 * 
	 * Example call: </br>
	 * StoryController control = StoryController.getInstance(someActivity.this);
	 * 
	 * @param context	
	 * 			Could be an activity or application context.
	 */
	public static StoryController getInstance(Context context) {
		if (self == null) {
			self = new StoryController(context);
		}
		return self;
	}
	
	/**
	 * Sets its story to whatever story is passed in. Using this method also 
	 * means that the story you are passing in is not "complete". This means
	 * that its chapters are empty, only the actual story data like title and
	 * author is contained. Therefore, this method will retrieve all the 
	 * chapters (as well as any choices and media belonging to those chapters)
	 * and will set these chapters as the story's chapters so it is now a
	 * "complete" story. </br></br>
	 * 
	 * Example call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br></br>
	 * Assume that myStory has chapters belonging to it in stored in the 
	 * database. </br></br>
	 * 
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryIncomplete(myStory);</br></br>
	 * 
	 * The story field in StoryController is now has any chapters it had stored
	 * in the database.
	 * 
	 * @param aStory
	 * 			A story object assumed to be missing its chapters.
	 */
	public void setCurrStoryIncomplete(Story aStory) {
		story = aStory;
		story.setChapters(syncher.syncChaptersFromDb(story.getId()));
	}
	
	/**
	 * Sets its story to whatever story is passed in. Using this method also 
	 * means that the story you are passing in is "complete". This means
	 * that it has all of its chapters within in, including any choices and 
	 * media belonging to those chapters. There is no need to retrieve them
	 * from the database </br></br>
	 * 
	 * Example call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br></br>
	 * Chapter chap = new Chapter(myStory.getId(), "The guy on second floor of
	 * comp sci has a really scary and annoying laughter while I try to finish
	 * documenting this never ending project");</br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * 
	 * @param aStory
	 * 			A story object assumed to have all of its chapters ready.
	 */
	public void setCurrStoryComplete(Story aStory) {
		story = aStory;
	}
	
	/**
	 * Returns a reference to the current story model. </br></br>
	 * 
	 * Example Call: </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * Story myStory = control.getCurrStory();
	 * 
	 * @return
	 */
	public Story getCurrStory() {
		return story;
	}

	/**
	 * Edits the story model's first chapter id. No view is allowed to do this
	 * directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * UUID newChapId = UUID.randomUUID();
	 * control.editFirstChapter(newChapId);
	 * 
	 * @param id
	 * 			Id of the chapter you want to set as the story's first chapter.
	 * 			Note that it must be a UUID.
	 */
	public void editFirstChapterId(UUID id) {
		story.setFirstChapterId(id);
	}
	
	/**
	 * Edits the story model's first chapter id. No view is allowed to do this
	 * directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * UUID newChapId = UUID.randomUUID();
	 * control.editTitle("I have the best titles");
	 * 
	 * @param title
	 * 			Title (a string) you want to set as the story's title.
	 */
	public void editTitle(String title) {
		story.setTitle(title.trim());
	}
	
	/**
	 * Edits the story model's title. No view is allowed to do this directly, 
	 * so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * UUID newChapId = UUID.randomUUID();
	 * control.editAuthor("Voldemort");
	 * 
	 * @param author
	 * 			Author (a string) you want to set as the story's title.
	 */
	public void editAuthor(String author) {
		story.setAuthor(author.trim());
	}
	
	/**
	 * Edits the story model's description. No view is allowed to do this
	 * directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * UUID newChapId = UUID.randomUUID();
	 * control.editDescription("yay annoying laugh guy in second floor comp sci
	 * has stopped laughing. I think the guy trying to finish 304 really 
	 * appreciates that.");
	 * 
	 * @param description
	 * 			Description (a string) you want to set as the story's 
	 * 			description.
	 */
	public void editDescription(String desc) {
		story.setDescription(desc.trim());
	}
	
	/**
	 * Adds a chapter onto the story object. If the story has no chapters when 
	 * the new chapter is added, the firstChapterId will also be set for the 
	 * story. </br></br>
	 *
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br>
	 * Chapter chap = new Chapter(myStory.getId(), "blah blah");</br>
	 * control.addChapter(chap); </br>
	 * 
	 * @param chapter
	 *			Chapter object you want to add to the story
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
	 * Updates a chapter of the story model. Does so by finding the chapter,
	 * deleting it, and inserting the new version of it. </br></br>
	 * 
	 * Example call:</br>
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br>
	 * Chapter chap = new Chapter(myStory.getId(), "blah blah");</br>
	 * control.addChapter(chap); </br>
	 * chap.setText("nennwweww"); </br>
	 * control.updateChapter(chap); </br>
	 * 
	 * @param chapter
	 * 			Chapter containing the new data you want to replace the old
	 * 			chapter with.
	 */
	public void updateChapter(Chapter chapter) {
		for (Chapter chap : story.getChapters()) {
			if (chap.getId().equals(chapter.getId())) {
				story.getChapters().remove(chap);
				break;
			}
		}
		story.getChapters().add(chapter);
	}
	
	/**
	 * Due to performance issues, Media objects don't actually hold Bitmaps.
	 * A path to the location of the file is instead saved and used to 
	 * retrieve bitmaps whenever needed. Media objects can also hold the
	 * bitmaps after the have been converted to a string (Base64). </br></br>
	 * 
	 * Before a Story can be inserted into the server, all the images 
	 * belonging to the story's chapters must have their bitmap strings
	 * set, i.e. all of them must convert the bitmap they are linked with
	 * to a string. This is only done when the story is published to avoid 
	 * doing the expensive conversion unnecessarily (local stories only 
	 * need to know the path to the image).
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

	/** 
	 * Helper function to prepareChaptersForServer. It sets the bitmap string
	 * for a single media object. Reason why: </br></br>
	 * 
	 * Before a Story can be inserted into the server, all the images 
	 * belonging to the story's chapters must have their bitmap strings
	 * set, i.e. all of them must convert the bitmap they are linked with
	 * to a string. This is only done when the story is published to avoid 
	 * doing the expensive conversion unnecessarily (local stories only 
	 * need to know the path to the image).
	 * </br>
	 * 
	 * @param chap
	 */
	private void prepareMediasForServer(Chapter chap) {
		for (Media photo : chap.getPhotos()) {
			photo.setBitmapString(photo.getBitmap());
		}

		for (Media ill : chap.getIllustrations()) {
			ill.setBitmapString(ill.getBitmap());
		}
	}	
	
	/**
	 * Any changes to the story model will now be pushed to the server. This is
	 * called any time the user publishes or republishes a story. </br></br>
	 * 
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * control.editTitle("new title"); </br>
	 * control.pushChangesToServer(); </br>
	 * 
	 */
	public void pushChangesToServer() {
		prepareChaptersForServer();
		serverMan.update(story);
	}

	/**
	 * Any changes to the story model will now be pushed to the database so the
	 * database information is consistent. </br></br>
	 * 
	 * Example Call: </br>
	 * Story myStory = new Story("Title", "author", "desc", "123bfdg6"); </br>
	 * StoryController control = StoryController.getInstance(someActivity.this); </br>
	 * control.setCurrStoryComplete(myStory);</br></br>
	 * control.editTitle("new title"); </br>
	 * control.pushChangesToServer(); </br>
	 * 
	 */
	public void pushChangesToDb() {
		syncher.syncStoryFromMemory(story);
	}
}
