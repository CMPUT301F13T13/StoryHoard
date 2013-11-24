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

import ca.ualberta.cmput301f13t13.storyhoard.backend.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Syncher;
import ca.ualberta.cmput301f13t13.storyhoard.backend.Utilities;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Class meant for the testing of the Utilities class in the StoryHoard
 * application.
 * 
 * @author Stephanie Gil
 * @author Ashley Brown
 * 
 * @see Utilities
 */
public class TestUtilities extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	
	public TestUtilities() {
		super(ViewBrowseStories.class);
	}

	/**
	 * Tests the get phoneId returns a string of phoneid 
	 */
	
	public void testGetPhoneId() {
	assertFalse(Utilities.getPhoneId(this.getActivity()) == null);
	}
	
	/** 
	 * Tests getting the phoneid
	 */
	public void testSaveImageToSD() {
		Bitmap bmp = BogoPicGen.generateBitmap(50, 50);
		String path = Syncher.saveImageToSD(bmp);
		assertNotNull(path);
	}
}
