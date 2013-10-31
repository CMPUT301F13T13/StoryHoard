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

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import junit.framework.TestCase;

import ca.ualberta.cs.c301f13t13.backend.*;
import static org.junit.Assert.*;

import org.junit.Test;

import android.net.Uri;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.gui.MainActivity;

/**
 * @author Owner
 *
 */
public class TestChapter extends TestCase{

	public TestChapter() {
		super();
	}
	
	
	/**
	 * Tests creating a chapter two ways.
	 */
	public void testCreateChapter(){
		Chapter newChapter = new Chapter(UUID.randomUUID(), "this is text");
		newChapter = new Chapter(UUID.randomUUID(), 
				UUID.randomUUID(), "again");
	}
	
//	/**
//	 * Tests displaying illustrations.
//	 */ // USED TO BE CALLED testViewIllustration
//	public void testAddGetIllustration() {
//		// Add arguments to make chapter
//		Chapter chapter = new Chapter(UUID.randomUUID(), "bobby went home");
//		
//		Uri uri = new Uri();
//
//		// Add an illustration to the chapter
//		chapter.addIllustration(illustration);
//		
//		Uri newUris = chapter.getIllustrations();
//		assertTrue(newUris.size() != 0);
//		
//		Bitmap bitmap = BitmapfromUri()...
//	}

}
