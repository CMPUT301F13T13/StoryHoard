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

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.gui.SearchActivity;

/**
 * Test case for the searching stories activity
 * 
 * @author Kim Wu
 * 
 */

public class TestSearchActivity extends ActivityInstrumentationTestCase2<SearchActivity> {
	private Button searchButton;
	private EditText titleInput;
	private Spinner spinner;
	private SearchActivity activity;
	
	public TestSearchActivity() {
		super(SearchActivity.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		activity = getActivity();
		searchButton = (Button) activity.findViewById(R.id.searchButton);
		titleInput = (EditText) activity.findViewById(R.id.story_name);
		spinner = (Spinner) activity.findViewById(R.id.search_spinner);
	}

	/**
	 * Testing that the ui widgets setup properly
	 */
	public void testPreConditions() {
		assertTrue(activity != null);
		assertTrue(searchButton != null);
		assertTrue(titleInput != null);
		assertTrue(spinner != null);
	}
	
	/**
	 * Tests setting the title on the title ui widget.
	 */
	@UiThreadTest
	public void testSetTitle() {
		String text = "The Best title Ever";
		titleInput.setText(text);
		assertTrue(titleInput.getText().toString().equals(text));
	}	
}







