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

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cmput301f13t13.storyhoard.helpGuides.InfoActivity;
import ca.ualberta.cmput301f13t13.storyhoard.local.BogoPicGen;
import ca.ualberta.cmput301f13t13.storyhoard.local.Utilities;

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
		ActivityInstrumentationTestCase2<InfoActivity> {

	public TestUtilities() {
		super(InfoActivity.class);
	}

	/**
	 * Tests that the get phoneId returns a string of phoneid
	 */
	public void testGetPhoneId() {
		assertFalse(Utilities.getPhoneId(this.getActivity()) == null);
	}

	/**
	 * Tests saving an image to the sd card.
	 */
	public void testSaveImageToSD() {
		Bitmap bmp = BogoPicGen.generateBitmap(50, 50);
		String path = Utilities.saveImageToSD(bmp);
		assertNotNull(path);
	}
}
