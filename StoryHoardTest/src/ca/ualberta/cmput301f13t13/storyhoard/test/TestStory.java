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
import java.util.HashMap;
import java.util.UUID;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.backend.ChoiceManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.backend.DBHelper;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Media;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Story;
import ca.ualberta.cmput301f13t13.storyhoard.backend.StoryManager;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

/**
 * Class meant for the testing of the Story class in the StoryHoard 
 * application.
 * 
 * @author Stephanie Gil
 * 
 * @see Story
 */
public class TestStory extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private Context activity;

	public TestStory() {
		super(ViewBrowseStories.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		activity = getActivity();
	}
	
	/**
	 * Tests creating a story without chapters.
	 */
	@SuppressWarnings("unused")
	public void testCreateStory() {
		try {
			Story story = new Story("7 bugs", "Shamalan", "scary story",
					Utilities.getPhoneId(this.getActivity()));
		} catch (Exception e) {
			fail("error creating a new story object");
		}
	}

	/**
	 * Tests adding a chapter to a story.
	 */
	public void testAddChapter() {
		Story story = new Story("7 bugs", "Shamalan", "scary story",
				Utilities.getPhoneId(this.getActivity()));
		Chapter chapter = new Chapter(story.getId(), "On a cold, dark night.");
		story.addChapter(chapter);
		assertEquals(story.getChapters().size(), 1);
	}

	/**
	 * Tests retrieving a specific chapter from a story.
	 */
	public void testGetChapter() {
		Story story = new Story("7 bugs", "Shamalan", "scary story",
				Utilities.getPhoneId(this.getActivity()));
		Chapter chapter1 = new Chapter(story.getId(), 
				"On a cold, dark night.");
		Chapter chapter2 = new Chapter(story.getId(), 
				"On a sunny, bright day.");
		story.addChapter(chapter1);
		story.addChapter(chapter2);

		Chapter result = story.getChapter(chapter1.getId());
		assertSame(result, chapter1);
	}

	/**
	 * Tests retrieving the search information places within the story, i.e. the
	 * id, title, author, description, and whether or not it was created by the
	 * author.
	 */
	public void testSetSearchCriteria() {
		// empty everything
		Story criteria = new Story(null, null, null, null, null);
		HashMap<String, String> info = criteria.getSearchCriteria();

		assertTrue(info.size() == 0);

		// not empty arguments
		criteria = new Story(null, "john", "the cow", "went home",
				Utilities.getPhoneId(this.getActivity()));
		info = criteria.getSearchCriteria();
		assertEquals(info.size(), 2);
		assertTrue(info.get("title").equals("%john%"));
		assertTrue(info.get("phone_id").equals(criteria.getPhoneId()));
	}

	/**
	 * Tests the setters and getters of a story object.
	 */
	@SuppressWarnings("unused")
	public void testSettersGetters() {
		Story mockStory = new Story("title1", "author1", "desc1",
				Utilities.getPhoneId(this.getActivity()));

		UUID storyId = mockStory.getId();
		String title = mockStory.getTitle();
		String author = mockStory.getAuthor();
		String desc = mockStory.getDescription();
		String phoneid = mockStory.getPhoneId();
		HashMap<UUID, Chapter> chapters = mockStory.getChapters();
		UUID firstChapId = mockStory.getFirstChapterId();

		mockStory.setId(UUID.randomUUID());
		mockStory.setTitle("new title");
		mockStory.setAuthor("pinkie");
		mockStory.setDescription("new desc");
		mockStory.setPhoneId(Utilities.getPhoneId(this.getActivity()));
		mockStory.setChapters(null);
		mockStory.setFirstChapterId(UUID.randomUUID());

		assertNotSame(storyId, mockStory.getId());
		assertNotSame("new title", title);
		assertNotSame("pinkie", author);
		assertNotSame("new desc", desc);
		assertTrue(phoneid.equals(mockStory.getPhoneId()));
		assertTrue(mockStory.getChapters() == null);
		assertNotSame(mockStory.getFirstChapterId(), firstChapId);
	}
	
	/**
	 * tests adding itself to the database
	 */
	public void testAddSelf() {
		Story story = new Story("the mouse", "me", "little mousey", 
				Utilities.getPhoneId(getActivity()));
		story.addSelf(getActivity());
		StoryManager sm = StoryManager.getInstance(getActivity());
		ArrayList<Object> objs = sm.retrieve(story);
		assertEquals(objs.size(), 1);
	}
	
	/**
	 * tests updating itself in the database
	 */
	public void testUpdateSelf() {
		Choice mockChoice = new Choice(UUID.randomUUID(), UUID.randomUUID(),
				"opt1");
		mockChoice.addSelf(getActivity());
		mockChoice.setText("new text");
		mockChoice.updateSelf(getActivity());
		ChoiceManager cm = ChoiceManager.getInstance(getActivity());
		ArrayList<Object> objs = cm.retrieve(mockChoice);
		assertEquals(objs.size(), 1);		
		assertTrue(((Choice)objs.get(0)).getText().equals("new text"));
	}
	
	/**
	 * tests getting all components of a chapter (media + choices)
	 */
	public void testGetFullContent() {
		Story s = new Story("title", "author", "des", "phoneid");
		Chapter chap = new Chapter(UUID.randomUUID(), "chap1");
		Choice mockChoice = new Choice(chap.getId(), chap.getId(), "opt1");
		Media m = new Media(chap.getId(), null, Media.PHOTO);
		chap.addPhoto(m);
		chap.addChoice(mockChoice);
		s.addChapter(chap);
		s.addSelf(getActivity());
		
		Story fullStory = new Story(s.getId(), null, null, null, null);
		fullStory.setFullContent(getActivity());
		
		assertEquals(s.getChapters().size(), 1);
		assertEquals(s.getChapter(s.getFirstChapterId()).getChoices().size(), 1);
		assertEquals(s.getChapter(s.getFirstChapterId()).getPhotos().size(), 1);
	}
	
	/**
	 * Tests caching a story.
	 */
	public void testCacheLoadStory() {
		Story mockStory = new Story("My Monkey", "TS ELLIOT",
				"monkey is in the server", Utilities.getPhoneId(getActivity()));
		Chapter chap = new Chapter(mockStory.getId(), "l");
		Chapter chap2 = new Chapter(mockStory.getId(), "2");
		chap.addChoice(new Choice(chap.getId(), chap2.getId(), "hi"));

		mockStory.addChapter(chap);
		mockStory.cache(activity);

		Story story = new Story(mockStory.getId(), null, null, null, null);
		story.setFullContent(activity);
		assertNotNull(story.getTitle());
		assertNotNull(story.getAuthor());
		assertNotNull(story.getDescription());
		
		story.setTitle("newTitle");
		story.cache(activity);
		
		story = new Story(mockStory.getId(), null, null, null, null);
		story.setFullContent(activity);
		assertTrue(story.getTitle().equals("newTitle"));
	}	
}
