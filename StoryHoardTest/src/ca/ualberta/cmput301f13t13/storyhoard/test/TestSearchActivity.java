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

import org.junit.Before;

import android.app.Activity;
import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.gui.SearchActivity;
import ca.ualberta.cs.c301f13t13.gui.SearchResultsActivity;

/**
 * Test case for the searching stories activity
 * 
 * @author Kim Wu
 * 
 */

public class TestSearchActivity extends ActivityInstrumentationTestCase2<SearchActivity> {

	public TestSearchActivity() {
		super(SearchActivity.class);
	}

	//Used http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium
	
	public void testOpenNextActivity() {
		  ActivityMonitor activityMonitor = getInstrumentation().addMonitor(SearchResultsActivity.class.getName(), null, false);

		  // open current activity.
		  SearchActivity myActivity = getActivity();
		  final Button button = (Button) myActivity.findViewById(R.id.searchButton);
		  myActivity.runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // click button and open next activity.
		      button.performClick();
		    }
		  });

		  Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 10);
		  // next activity is opened and captured.
		  assertNotNull(nextActivity);
		  nextActivity .finish();
		}
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Testing that the preconditions setup properly
	 */
	public void testPreConditions() {
	}
}







