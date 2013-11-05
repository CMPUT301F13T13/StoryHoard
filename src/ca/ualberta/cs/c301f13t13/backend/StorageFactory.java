/**
 * 
 */
package ca.ualberta.cs.c301f13t13.backend;

import android.content.Context;

/**
 * @author Owner
 *
 */
public class StorageFactory {
	private static Context context;

	protected StorageFactory(Context context) {
		this.context = context;
	}

	public StoringManager getStoringManager(int type) {
		switch (type) {
		case SHController.PUBLISHED:
			return ServerManager.getInstance(context);
		case SHController.CHAPTER:
			return ChapterManager.getInstance(context);
		case SHController.CHOICE:
			return ChoiceManager.getInstance(context);
		case SHController.MEDIA:
			return MediaManager.getInstance(context);
		default: // Story
			return StoryManager.getInstance(context);
		}
	}


}
