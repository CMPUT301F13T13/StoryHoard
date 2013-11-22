/**
 * 
 */
package ca.ualberta.cmput301f13t13.storyhoard.backend;

import android.content.Context;

/**
 * @author sgil
 *
 */
public abstract class StoryPart {

	public abstract void updateSelf(Context context);
	
	public abstract void addSelf(Context context);
	
	public abstract void setFullContent(Context context);
	
	public void removeSelf(Context context) {
		// optional
	}
	
}
