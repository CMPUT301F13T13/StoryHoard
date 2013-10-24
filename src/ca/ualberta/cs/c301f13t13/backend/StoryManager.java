/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.gson.Gson;

/**
 * @author sgil
 *
 */
public class StoryManager implements StoringManager{
	private static final String FILENAME = "Stories.sav";
	FileOutputStream fop = null;
	File file;
	
	/**
	 * Initializes a new StoryManager object.
	 */
	public StoryManager() {
	}

	/**
	 * Saves a new story either locally or to the server.
	 */
	@Override
	public void insert() {
		try {
			file = new File(FILENAME);
			fop = new FileOutputStream(file);
		
			if (!file.exists()) {
				file.createNewFile();
			}
		
			// write to file
			
			
			fop.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void getSingle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getCollection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
