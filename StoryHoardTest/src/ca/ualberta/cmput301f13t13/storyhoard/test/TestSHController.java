
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

package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;

import ca.ualberta.cmput301f13t13.storyhoard.backend.SHController;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ServerManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the GeneralController class in the 
 * StoryHoard application.
 * 
 * @author Stephanie Gil

 * @see SHController
 */
public class TestSHController extends
ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private SHController gc = null;
	private static ViewBrowseStories activity;

	public TestSHController() {
		super(ViewBrowseStories.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		activity = this.getActivity();
		// Clearing database
		DBHelper helper = DBHelper.getInstance(activity);
		helper.close();
		activity.deleteDatabase(DBContract.DATABASE_NAME);

		gc = SHController.getInstance(activity);				
	}

	/**
	 * Tests using the controller to add stories and then get all cached
	 * stories.
	 */
	public void testGetAllCachedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", "343423");
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "45643543");

		s1.addSelf(activity);
		s2.addSelf(activity);
		s3.addSelf(activity);
		
		stories = gc.getAllCachedStories();

		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the controller to get all stories created by the author.
	 */
	public void testGetAllCreatedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("T: Bob the cow", "A: me", "D: none", "34530");
		
		s1.addSelf(activity);
		s2.addSelf(activity);
		s3.addSelf(activity);
		
		stories = gc.getAllAuthorStories();
		assertEquals(stories.size(), 2);
	}

	/**
	 * Tests using the controller to publish stories and then get all published
	 * stories.
	 */
	public void testGetAllPublishedStories() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("T: Lily the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("T: Bob the cow", "A: me", "D: none", 
				Utilities.getPhoneId(getActivity()));

		s1.publish();
		s2.publish();

		stories = gc.getAllPublishedStories();

		assertTrue(stories.size() > 1);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
		sm.remove(s2);
	}

	/**
	 * Tests using the controller to add chapters and then get all chapters
	 * belonging to a story.
	 */
	public void testAddGetAllChapters() {
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		Story story = new Story("title", "author", "des", 
				Utilities.getPhoneId(getActivity()));

		Chapter c1 = new Chapter(story.getId(), "text");
		Chapter c2 = new Chapter(story.getId(), "text");
		Chapter c3 = new Chapter(UUID.randomUUID(), "text");

		c1.addSelf(activity);
		c2.addSelf(activity);
		c3.addSelf(activity);

		chapters = gc.getAllChapters(story.getId());

		assertEquals(chapters.size(), 2);
	}

	/**
	 * Tests using the controller to add choices and then get all choices
	 * belonging to a chapter.
	 */
	public void testAddGetAllChoices() {
		ArrayList<Choice> choices = new ArrayList<Choice>();
		Chapter chap = new Chapter(UUID.randomUUID(), "text");

		Choice c1 = new Choice(chap.getId(), UUID.randomUUID(), "text");
		Choice c2 = new Choice(chap.getId(), UUID.randomUUID(), "text");
		Choice c3 = new Choice(UUID.randomUUID(), UUID.randomUUID(), "text");

		c1.addSelf(activity);
		c2.addSelf(activity);
		c3.addSelf(activity);

		choices = gc.getAllChoices(chap.getId());

		assertEquals(choices.size(), 2);
	}

	/**
	 * Tests using the controller to add media and then retrieve it 
	 * again.
	 */
	public void testAddGetAllMedia() {	
		Chapter chap = new Chapter(UUID.randomUUID(), "lala");
		Media photo1 = new Media(chap.getId(), null, Media.PHOTO);

		photo1.addSelf(activity);
		ArrayList<Media> photos = gc.getAllPhotos(chap.getId());
		assertEquals(photos.size(), 1);
	}	

	/**
	 * Tests using the controller to test for a variety of different stories
	 * that have been added / published.
	 */
	public void testSearchStory() {
		ArrayList<Story> stories = new ArrayList<Story>();

		// Insert some stories
		Story s1 = new Story("Lily the cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("Bob the hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s3 = new Story("Bob the cow", "me", "D: none", 
				"34532432423");
		Story s4 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s5 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s6 = new Story("sad hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));

		s1.addSelf(activity);
		s2.addSelf(activity);
		s3.addSelf(activity);
		s4.publish();
		s5.publish();
		s6.publish();

		// title are null (should retrieve all created stories)
		stories = gc.searchLocalStory(null);
		assertEquals(stories.size(), 2);

		// title has cow, created
		stories = gc.searchLocalStory("cow");
		assertEquals(stories.size(), 1);

		// created, title has bob and hen
		stories = gc.searchLocalStory("Bob hen");
		assertEquals(stories.size(), 1);

		// title has sad
		stories = gc.searchPublishedStory("sad");
		assertEquals(stories.size(), 3);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s4);
		sm.remove(s5);
		sm.remove(s6);
	}

	/**
	 * Tests getting a random choice,
	 */
	public void testGetRandomChoice() {
		UUID s1 = UUID.randomUUID();
		Chapter chap1 = new Chapter(s1, "chapter text ");
		Chapter chap2 = new Chapter(s1, "chapter text rawr");
		Choice choice1 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		Choice choice2 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		Choice choice3 = new Choice(chap1.getId(), chap2.getId(),
				"choice texters");
		choice1.addSelf(activity);
		choice2.addSelf(activity);
		choice3.addSelf(activity);
		chap1.addSelf(activity);
		chap2.addSelf(activity);
		
		Choice choice = gc.getRandomChoice(chap1.getId());
		assertTrue(choice.getCurrentChapter().equals(chap1.getId()));
	}

	/**
	 * Tests retrieving a random story from published stories.
	 */
	public void testRandomStory() {
		Story s1 = new Story("sad cow", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		Story s2 = new Story("sad hen", "me", "D: none", 
				Utilities.getPhoneId(getActivity()));
		
		s1.publish();
		s2.publish();
		
		Story random = gc.getRandomStory();
		assertTrue(random != null);
		
		// clean up server
		ServerManager sm = ServerManager.getInstance();
		sm.remove(s1);
		sm.remove(s2);
	}

}