package ca.ualberta.cmput301f13t13.storyhoard.test;

import org.junit.Before;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.GridView;
import android.widget.ListAdapter;
import ca.ualberta.cs.c301f13t13.gui.ViewBrowseStories;

/**
 * Test case for the activity to view all stories
 * 
 * @author alexanderwong
 * 
 */
public class TestViewBrowseStories extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {

	private Activity mActivity;
	private GridView mGridView;
	private ListAdapter mAdapter;

	public static final int ADAPTER_COUNT = 0;

	public TestViewBrowseStories() {
		super(ViewBrowseStories.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		mGridView = (GridView) mActivity
				.findViewById(ca.ualberta.cmput301f13t13.storyhoard.R.id.gridStoriesView);
		mAdapter = mGridView.getAdapter();
	}

	/**
	 * Testing that the preconditions setup properly
	 */
	public void testPreConditions() {
		assertTrue(mGridView.getOnItemSelectedListener() != null);
		assertTrue(mAdapter != null);
		assertEquals(mAdapter.getCount(), ADAPTER_COUNT);
	}
}
