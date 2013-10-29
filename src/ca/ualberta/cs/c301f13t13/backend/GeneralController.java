package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.UUID;

public class GeneralController {
	private static final Integer CACHED = 0;
	private static final Integer CREATED = 1;
	private static final Integer STORY = 0;
	private static final Integer CHAPTER = 1;
	private static final Integer CHOICE = 2;

	
	/** gets all stories that are either cached or created by author
	 * 
	 * @return
	 */
	public ArrayList <Object> getAllStories(int type){
		//TODO everything
		
		return null;
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
