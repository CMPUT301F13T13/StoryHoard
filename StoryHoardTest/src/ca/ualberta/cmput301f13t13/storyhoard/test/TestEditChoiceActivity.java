/**
 * 
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
 * @author joshuatate
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
		mActivity = getActivity();
		mActivity.findViewById(ca.ualberta.cmput301f13t13.storyhoard.R.layout.activity_edit_choice);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	

}
