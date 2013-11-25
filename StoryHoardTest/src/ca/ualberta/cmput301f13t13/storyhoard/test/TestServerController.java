/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.test;

import java.util.ArrayList;
import java.util.UUID;

import ca.ualberta.cmput301f13t13.storyhoard.controllers.ServerStoryController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;
import ca.ualberta.cmput301f13t13.storyhoard.gui.ViewBrowseStories;
import ca.ualberta.cmput301f13t13.storyhoard.serverClasses.ServerManager;

import android.test.ActivityInstrumentationTestCase2;

/**
 * @author sgil
 *
 */
public class TestServerController extends
		ActivityInstrumentationTestCase2<ViewBrowseStories> {
	private ServerStoryController serverCon;
	
	public TestServerController() {
		super(ViewBrowseStories.class);
		// TODO Auto-generated constructor stub
	}

	public void setUp() throws Exception {
		super.setUp();
		serverCon = ServerStoryController.getInstance(getActivity());
		ServerManager.getInstance().setTestServer();
	}

	public void tearDown() throws Exception {
		super.tearDown();
		ServerManager.getInstance().setRealServer();
	}

	public void testPublishGetAll() {
		return serverMan.getAll();
	}	
	
	public void testSearchByTitle(String title) {
		return serverMan.searchByKeywords(title);
	}		
	
	/**
	 * Chooses a random story from within the stories that are 
	 * published. If there are no published stories available,
	 * it will return null.
	 * 
	 */
	public void testGetRandomStory() {
		return serverMan.getRandom();
	}

	public void testUpdate(Story story) {
		serverMan.update(story);
	}

	public void testRemove(UUID objId) {
		serverMan.remove(objId.toString());
		
	}		
	

}
