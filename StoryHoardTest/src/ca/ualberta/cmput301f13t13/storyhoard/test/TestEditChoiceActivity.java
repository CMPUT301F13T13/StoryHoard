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
import org.junit.Test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import ca.ualberta.cs.c301f13t13.gui.EditChoiceActivity;

/**
 * Test case for the activity to edit choices
 * 
 * @author Joshua Tate
 * 
 */
public class TestEditChoiceActivity extends
		ActivityInstrumentationTestCase2<EditChoiceActivity> {
	
	private Activity mActivity;
	
	public TestEditChoiceActivity() {
		super(EditChoiceActivity.class);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@Before
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void test() {
		fail("Not yet implemented");
		mActivity = getActivity();
		mActivity.findViewById(ca.ualberta.cmput301f13t13.storyhoard.R.layout.activity_edit_choice);	
		
	}
}
