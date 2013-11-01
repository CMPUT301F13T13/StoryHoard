/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;

import android.os.storage.StorageManager;

/**
 * @author sgil
 *
 */
public class ServerManager implements StoringManager {

	ServerManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void insert(Object object, DBHelper helper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Object> retrieve(Object criteria, DBHelper helper) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Object newObject, DBHelper helper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String setSearchCriteria(Object object, ArrayList<String> sArgs) {
		// TODO Auto-generated method stub
		return null;
	}

}
