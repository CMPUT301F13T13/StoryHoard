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

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Media;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Story;
import ca.ualberta.cs.c301f13t13.gui.AdapterChoices;
import ca.ualberta.cs.c301f13t13.gui.ViewChapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Tests the View Chapter class.
 *
 */
public class TestViewChapter extends
		ActivityInstrumentationTestCase2<ViewChapter> {
	private ViewChapter activity;

	private AdapterChoices choiceAdapter;
	private AlertDialog photoDialog;
	private LinearLayout illustrations;
	private LinearLayout photos;

	private TextView chapterContent;
	private ListView chapterChoices;
	private Button addPhotoButton;
	
	public TestViewChapter() {
		super(ViewChapter.class);
	}
	
	@Override
	public void setUp() throws Exception {
		Story story = new Story("title", "author", "es", "432432");
		Chapter chap = new Chapter(story.getId(), null);
		Intent intent = new Intent();
		
		intent.putExtra("storyID", story.getId());
		intent.putExtra("chapterID", chap.getId());
		
		setActivityIntent(intent);
	}

	public void testPreConditions() {
		fail("not yet implemented");
		
		activity = getActivity();
		
		// Setup the activity fields
		chapterContent = (TextView) activity.findViewById(R.id.chapterContent);
		chapterChoices = (ListView) activity.findViewById(R.id.chapterChoices);
		addPhotoButton = (Button) activity.findViewById(R.id.addPhotoButton);
		illustrations = (LinearLayout) activity.findViewById(R.id.horizontalIllustraions);
		photos = (LinearLayout) activity.findViewById(R.id.horizontalPhotos);
		
		assertTrue(chapterContent != null);
		assertTrue(chapterChoices != null);
		assertTrue(addPhotoButton != null);
		assertTrue(illustrations != null);
		assertTrue(photos != null);
	}
}
