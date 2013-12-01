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

import android.test.ActivityInstrumentationTestCase2;
import android.widget.GridView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.StoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

/**
 * Test case for the activity to view all stories
 * 
 * @author Alex Wong
 * 
 */
public class TestViewBrowseStories extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private GridView gridView;
	private ViewBrowseStories activity;

	public TestViewBrowseStories() {
		super(ViewBrowseStories.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Testing that the preconditions setup properly
	 */
	public void testPreConditions() {
		StoryController sc = StoryController.getInstance(getActivity());
		Story story = new Story("7 bugs", "Shamalan", "scary story",
				Utilities.getPhoneId(this.getActivity()));
		Chapter chapter = new Chapter(story.getId(), "On a cold, dark night.");
		story.getChapters().add(chapter);
		sc.setCurrStoryComplete(story);
		sc.pushChangesToDb();
		
		activity = getActivity();
		gridView = (GridView) activity.findViewById(R.id.gridStoriesView);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(gridView != null);
	}
}
