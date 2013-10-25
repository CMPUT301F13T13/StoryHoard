package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

/**
 * Interface for storing objects locally or to the server
 * 
 * @author Stephanie, Ashley
 *
 */
public interface StoringManager {
	
	public void insert(Object object);
	
	public Object getSingle();
	
	public ArrayList<Object> getCollection();
	
	public void search(Object object);
	
	public void update(Object object);
	
	// public void delete();
}
