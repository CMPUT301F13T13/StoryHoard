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
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChapterManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.MediaManager;
import ca.ualberta.cmput301f13t13.storyhoard.local.Syncher;

/**
 * Role: Responsible for manipulating the current chapter model. It keeps a 
 * reference to the current chapter model as a field and updates that whenever 
 * the user makes changes. Every time the user creates a new story or saves his 
 * changes to one, the changes are pushed to the database.</br></br>
 * 
 * Design Pattern: This class is a singleton, so only one instance of it will
 * ever exist. use getInstance() to retrieve that instance, not the 
 * constructor.
 * 
 * @author Stephanie Gil
 *
 */
public class ChapterController {
	private static MediaManager mediaMan;
	private static ChapterManager chapMan;
	private static ChoiceManager choiceMan;
	private static Syncher syncher;
	private static Chapter chapter;
	private static ChapterController self = null;
	
	/**
	 * Initializes a new ChapterController. Needs context so it is able to 
	 * initialize its manager objects to be able to push changes to
	 * the database. Also initializes a blank chapter to avoid null pointer 
	 * exceptions if methods are called on a null chapter. This constructor
	 * should never be used outside of this class (except for any subclasses).
	 * 
	 * @param context
	 */
	protected ChapterController(Context context) {
		mediaMan = MediaManager.getInstance(context);
		syncher = Syncher.getInstance(context);
		chapMan = ChapterManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
		chapter = new Chapter(null, null, "");  // blank chapter
	}
	
	/**
	 * Returns an instance of a ChapterConroller. The same instance is always 
	 * returned since it is a singleton. This is the method every other class
	 * should be using to access the ChapterController. Note that this method
	 * must be called statically. </br></br>
	 * 
	 * Example call: </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this);
	 * 
	 * @param context	
	 * 			Could be an activity or application context.
	 */
	public static ChapterController getInstance(Context context) {
		if (self == null) {
			self = new ChapterController(context);
		}
		return self;
	}
	
	/**
	 * Sets its chapter to whatever chapter in the database matches the id
	 * passed in. Evidently, you only have the id of the chapter you want, not
	 * the chapter itself, so it is "incomplete".Therefore, this method will 
	 * retrieve the chapter whose id matches the id you passed in, as well 
	 * as all the choices and media belonging to that chapter and will set
	 * the corresponding fields in the chapter to them. Then you will have a 
	 * "complete" chapter. </br></br>
	 * 
	 * Example call: </br>
	 * UUID chapId = UUID.fromString("5231b533-ba17-4787-98a3-f2df37de2aD7");
	 * Assume that there is a chapter in the database with the same id as chapId,
	 * and that it also has media and choices belonging to it in stored in the 
	 * database. </br></br>
	 * 
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterIncomplete(chapId);</br></br>
	 * 
	 * 
	 * @param id
	 * 			Id of the chapter you want to retrieve. Must be a UUID.
	 */
	public void setCurrChapterIncomplete(UUID id) {
		chapter = getFullChapter(id);
	}
	
	/**
	 * Using this method means that you first have a "complete" chapter object,
	 * including all its media and choices. This method will set the chapter 
	 * field of the ChapterController to be the chapter you are passing in
	 * so that it can be edited via methods defined in here.
	 * 
	 * Example call: </br>
	 * Chapter chap = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(chap);</br></br>
	 * 
	 * @param aChapter
	 */
	public void setCurrChapterComplete(Chapter aChapter) {
		chapter = aChapter;
	}		
	
	/**
	 * Method that retrieves all the media and choices belonging to the 
	 * current chapter. </br></br>
	 * 
	 * @param chapId
	 */
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
	
	/**
	 * Returns a reference to the current story model. </br></br>
	 * 
	 * Example Call: </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * Chapter mChap = control.getCurrChapter(); </br>
	 * 
	 * @return
	 */
	public Chapter getCurrChapter() {
		return chapter;
	}
	
	/**
	 * Edits the chapter model's text. No view is allowed to do this
	 * directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * String text = "new text"; </br>
	 * control.editText(text);
	 * 
	 * @param text
	 * 			String that you would like to set as the chapter's text.
	 */
	public void editText(String text) {
		chapter.setText(text);
	}
	
	/**
	 * Edits the chapter model's random choice boolean (determines whether or
	 * not that chapter contains should contain a random choice). No view is 
	 * allowed to do this directly, so they use this method.</br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * control.editRandomChoice(true); </br>
	 * 
	 * @param bool
	 */
	public void editRandomChoice(Boolean bool) {
		chapter.setRandomChoice(bool);
	}
	
	/**
	 * Takes a media belonging to the chapter that you would like to remove 
	 * and removes it. </br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * Media media = new Media(myChapter.getId(), null, "photo", "my comment"); </br>
	 * control.addPhoto(media); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * control.removeIllustration(media); </br>
	 * 
	 * @param bool
	 */
	public void removeIllustration(Media ill) {
		chapter.getIllustrations().remove(ill);
	}
	
	/**
	 * Adds a choice to the chapter. </br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * Choice choice = new Choice(myChapter.getId(), "choice text"); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * control.addChoice(choice); </br>
	 * 
	 * @param choice
	 * 			Choice object to be added.
	 */
	public void addChoice(Choice choice) {
		chapter.getChoices().add(choice);
	}

	/**
	 * Adds an actual random choice object (which is just one of the chapter's
	 * current choices but with the text "I'm feeling lucky") to the chapter's
	 * choice array list. </br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * control.addRandomChoice(); </br>
	 * 
	 * @param choice
	 * 			Choice object to be added.
	 */
	public void addRandomChoice() {
		chapter.getChoices().add(choiceMan.getRandomChoice(chapter.getId()));
	}
	
	/**
	 * Updates a choice of the chapter. Does so by finding the choice in the
	 * chapter's choices array list, deleting it, and inserting the new version 
	 * of it. </br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * Choice choice = new Choice(myChapter.getId(), "choice text");
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * control.addChoice(choice); </br>
	 * choice.setText("nennwweww"); </br>
	 * control.updateChoice(choice); </br>
	 * 
	 * @param chapter
	 * 			Chapter containing the new data you want to replace the old
	 * 			chapter with.
	 */
	public void updateChoice(Choice newChoice) {
		for (Choice oldChoice : chapter.getChoices()) {
			if (oldChoice.getId().equals(newChoice.getId())) {
				chapter.getChoices().remove(oldChoice);
				break;
			}
		}
		chapter.getChoices().add(newChoice);
	}	

	/**
	 * Adds a media to the chapter. Can be a photo or an illustration, the
	 * method will decide for itself and add it to the correct array list. </br></br>
	 * 
	 * Example Call: </br>
	 * Chapter myChapter = new Chapter(UUID.randomUUID(), "chap text"); </br>
	 * Media media = new Media(myChapter.getId(), null, "photo", "my comment"); </br>
	 * ChapterController control = ChapterController.getInstance(someActivity.this); </br>
	 * control.setCurrChapterComplete(myChapter);</br></br>
	 * control.addMedia(media); </br>
	 * 
	 * @param choice
	 * 			Choice object to be added.
	 */
	public void addMedia(Media photo) {
		if (photo.getType() == Media.PHOTO) {
			chapter.getPhotos().add(photo);
			mediaMan.insert(photo);
		} else {
			chapter.getIllustrations().add(photo);
		}
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
	 */
	public void pushChangesToDb() {
		chapMan.sync(chapter, chapter.getId());
		syncher.syncChapterParts(chapter);
	}
}
