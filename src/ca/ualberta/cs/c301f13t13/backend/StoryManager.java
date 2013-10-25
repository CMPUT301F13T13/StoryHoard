/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
 * @author sgil
 *
 */
public class StoryManager implements StoringManager{
	
	/**
	 * Initializes a new StoryManager object.
	 */
	public StoryManager() {
	}

	/**
	 * Saves a new story either locally or to the server.
	 */	
	@Override
	public void insert(Object object) {
		Story story = (Story) object;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void search(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Object object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getSingle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getCollection() {
		// TODO Auto-generated method stub
		return null;
	}

}
