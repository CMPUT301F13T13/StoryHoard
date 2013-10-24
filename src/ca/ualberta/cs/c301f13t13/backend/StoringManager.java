package ca.ualberta.cs.c301f13t13.backend;

/**
 * Interface for storing objects locally or to the server
 * 
 * @author Stephanie, Ashley
 *
 */
public interface StoringManager {
	
	public void insert();
	
	public void getSingle();
	
	public void getCollection();
	
	public void search();
	
	public void update();
	
	// public void delete();
}
