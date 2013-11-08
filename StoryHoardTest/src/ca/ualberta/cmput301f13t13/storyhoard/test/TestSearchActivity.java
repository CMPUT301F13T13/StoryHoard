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

		  Activity nextActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
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



