/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

/**
 * @author Owner
 *
 */
public class ChapterManager implements StoringManager{

	private Context context;
	
	public ChapterManager(Context context) {
		this.context = context;
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
	public void update(Object object, DBHelper helper) {
		// TODO Auto-generated method stub
		
	}
}
