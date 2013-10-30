package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class GeneralController {
	public static final int CACHED = 0;
	public static final int CREATED = 1;
	public static final int PUBLISHED = 2;
	
	public static final int STORY = 0;
	public static final int CHAPTER = 1;
	public static final int CHOICE = 2;

	
	/** gets all stories that are either cached, created by the author, or published
	 * 
	 * @return
	 */
	public ArrayList <Story> getAllStories(int type, Context context){
		StoryManager sm = new StoryManager(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Story> stories = new ArrayList<Story>();
		
		switch(type) {
		case CACHED:
			Story criteria = new Story("", "", "", false);
			ArrayList<Object> objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			break;
		case PUBLISHED:
			break;
		default:		
			break;
			
		}
		
		return stories;
	}
	/** gets all chapters that are in story
	 * 
	 * @return
	 */
	public ArrayList <Object> getAllChapters(Story story){
		//TODO everything
		
		return null;
	}
	/** gets all choices that are in story
	 * 
	 * @return
	 */
	public ArrayList <Object> getAllChoices(Choice choice){
		//TODO everything
		
		return null;
	}
	/** have to figure out.... this will be used to search by keyword
	 * 
	 * @return
	 */
	public ArrayList<Story> searchStory(String string){
		//TODO everything
		
		return null;
	}
	/** load all photo/illustration/choice as a chapter
	 * 
	 * @return
	 */
	public Chapter getCompleteChapter(UUID id){
		//TODO everything
		
		return null;
	}
	/** load all photo/illustration/choice/chapters as a story
	 * 
	 * @return
	 */
	public Story getCompleteStory(UUID id){
		//TODO everything
		
		return null;
	}
	public void updateObject(Object object, int type) {
		// TODO SWITCH STATEMENT
				
	}

}
