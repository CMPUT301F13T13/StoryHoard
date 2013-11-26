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
import ca.ualberta.cmput301f13t13.storyhoard.controllers.MediaController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.HelpGuide;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBContract;
import ca.ualberta.cmput301f13t13.storyhoard.local.DBHelper;

/**
 * Class for testing the functionality of MediaController.java
 * 
 * @author sgil
 *
 */
public class TestMediaController extends
ActivityInstrumentationTestCase2<HelpGuide> {
	private MediaController mediaCon;
	private ArrayList<Media> mockMedias;
	private Media mockMedia;
	private Media mockMedia2;
	private Media mockMedia3;
	
	public TestMediaController() {
		super(HelpGuide.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		mediaCon = MediaController.getInstance(getActivity());
	}

	/**
	 * Tests getting all chapters from a chapter.
	 */
	public void testGetMediasByChapter() {
		UUID chapId = UUID.randomUUID();
		mockMedia = new Media(chapId, null, Media.PHOTO);
		mediaCon.insert(mockMedia);
		mockMedia3 = new Media(UUID.randomUUID(), null, Media.PHOTO);
		mediaCon.insert(mockMedia3);

		mockMedias = mediaCon.getPhotosByChapter(chapId);
		assertEquals(mockMedias.size(), 1);		
	}
	
	/**
	 * Tests getting all created choices.
	 */
	public void testGetAll() {
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
		UUID chapId = UUID.randomUUID();
		mockMedia = new Media(chapId, null, "bob went away");
		mediaCon.insert(mockMedia);
		mockMedia3 = new Media(UUID.randomUUID(), null,	"photo");
		mediaCon.insert(mockMedia3);

		mockMedias = mediaCon.getAll();
		assertEquals(mockMedias.size(), 2);		
	}
	
	/**
	 * Tests inserting, retrieving, and updating a choice.
	 */
	public void testInsertRetrieveUpdate() {
		UUID chapId = UUID.randomUUID();
		mockMedia = new Media(chapId, null, Media.PHOTO);
		mediaCon.insert(mockMedia);
		
		mockMedias = mediaCon.getPhotosByChapter(chapId);
		assertEquals(mockMedias.size(), 1);
		
		mockMedia2 = mockMedias.get(0);
		mockMedia2.setText("hello");
		mediaCon.update(mockMedia2);
		
		mockMedias = mediaCon.getPhotosByChapter(chapId);
		assertEquals(mockMedias.size(), 1);	
		mockMedia2 = mockMedias.get(0);
		
		assertFalse(mockMedia2.getText().equals(mockMedia.getText()));
		
		// Clearing database
		DBHelper helper = DBHelper.getInstance(this.getActivity());
		helper.close();
		this.getActivity().deleteDatabase(DBContract.DATABASE_NAME);
		
	}	
}
