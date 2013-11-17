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
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseChapters;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

/**
 * Tests the view browse chapters class.
 * 
 * @author Stephanie Gil
 * 
 */
public class TestViewBrowseChapters extends
		ActivityInstrumentationTestCase2<ViewBrowseChapters> {
	private ListView storyChapters;
	private ViewBrowseChapters activity;

	public TestViewBrowseChapters() {
		super(ViewBrowseChapters.class);
	}

	@Override
	public void setUp() throws Exception{
		super.setUp();
		Intent intent = new Intent();
		intent.putExtra("storyID", UUID.randomUUID());
		setActivityIntent(intent);
	}

	public void testPreConditions() {
		fail("not yet implemented");
		
		activity = getActivity();
		storyChapters = (ListView) activity.findViewById(R.id.storyChapters);		
		assertTrue(storyChapters != null);
	}
}
