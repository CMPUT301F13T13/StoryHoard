package ca.ualberta.cs.c301f13t13.backend;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class GeneralController {
	public static final int ALL = -1;
	public static final int CACHED = 0;
	public static final int CREATED = 1;
	public static final int PUBLISHED = 2;
	
	public static final int STORY = 0;
	public static final int CHAPTER = 1;
	public static final int CHOICE = 2;

	public GeneralController() {
	}
	
	/** 
	 * Gets all the stories that are either cached, created by the author, or published.
	 * 
	 * @param type
	 * 			Will either be CACHED (0), CREATED (1), or PUBLISHED (2). 
	 * @param context
	 * @return ArrayList<Story>
	 */
	public ArrayList<Story> getAllStories(int type, Context context){
		StoryManager sm = StoryManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Story> stories = new ArrayList<Story>();
		ArrayList<Object> objects;
		Story criteria;
		
		switch(type) {
		case CACHED:
			criteria = new Story(null, "", "", "", false);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			criteria = new Story(null, "", "", "", true);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case PUBLISHED:
			stories = sm.getPublishedStories();
			break;
		default:		
			break;
		}
		
		return stories;
	}
	
	/**
	 *  Retrieves all the chapters that are in story.
	 * 
	 * @param storyId
	 * @param context
	 * 
	 * @return ArrayList<Chapter>
	 */
	public ArrayList<Chapter> getAllChapters(UUID storyId, Context context){
		ChapterManager cm = ChapterManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Chapter> chapters = new ArrayList<Chapter>();
		ArrayList<Object> objects;
		Chapter criteria = new Chapter(storyId, "");
		
		objects = cm.retrieve(criteria, helper);
		chapters = Utilities.objectsToChapters(objects);
		
		return chapters;
	}
	
	/** 
	 * Retrieves all the choices that are in a chapter.
	 * 
	 * @param chapterId
	 * @param context
	 * 
	 * @return ArrayList<Choice>
	 */
	public ArrayList<Choice> getAllChoices(UUID chapterId, Context context){
		ChoiceManager cm = ChoiceManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		ArrayList<Choice> choices = new ArrayList<Choice>();
		ArrayList<Object> objects;
//		Choice criteria = new Choice(chapterId, "");
//		
//		objects = cm.retrieve(criteria, helper);
//		choices = Utilities.objectsToChoices(objects);
		return choices;
	}
	
	/**
	 * Adds either a story, chapter, or choice to the database.
	 * 
	 * @param object
	 * 			Object to be inserted (must either be a Story, Chapter, or
	 * 			Choice instance).
	 * @param type
	 * 			Will either be STORY(0), CHAPTER(1), CHOICE(2).
	 * @param context
	 */
	public void addObjectLocally(Object object, int type, Context context) {
		DBHelper helper = DBHelper.getInstance(context);
		
		switch (type) {
		case STORY:
			Story story = (Story) object;
			StoryManager sm = new StoryManager(context);
			sm.insert(story, helper);
			break;
		case CHAPTER:
			Chapter chapter = (Chapter) object;
			ChapterManager cm = new ChapterManager(context);
			cm.insert(chapter, helper);
			break;
		case CHOICE:
			Choice choice = (Choice) object;
			ChoiceManager chm = new ChoiceManager(context);
			chm.insert(choice, helper);
			break;
		}
	}
	
	/** 
	 * Used to search for stories matching the given search criteria.
	 * Users can either search by specifying the title or author of
	 * the story. All stories that match will be retrieved.
	 * 
	 * @param title
	 * 			Title of the story user is looking for.
	 * @param author
	 * 			Author of the story user is looking for.
	 * @param type
	 * 			Will either be CACHED (0), CREATED (1) , or PUBLISHED (2).
	 * @param context
	 * 
	 * @return ArrayList<Story>
	 */
	public ArrayList<Story> searchStory(String title, String author,
										int type, Context context){
		Story criteria;
		ArrayList<Object> objects;
		ArrayList<Story> stories = new ArrayList<Story>();
		StoryManager sm = StoryManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		
		switch(type) {
		case CACHED:
			criteria = new Story(null, author, title, "", false);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);
			break;
		case CREATED:
			criteria = new Story(null, author, title, "", true);
			objects = sm.retrieve(criteria, helper);
			stories = Utilities.objectsToStories(objects);			
			break;
		case PUBLISHED:
			break;
		}
		
		return stories;
	}
	
	/** 
	 * Retrieves a complete chapter (including any photos, illustrations,
	 * and choices).
	 * 
	 * @return
	 */
	public Chapter getCompleteChapter(UUID id, Context context){
		ChapterManager cm = ChapterManager.getInstance(context);
		ChoiceManager chom = ChoiceManager.getInstance(context);
		MediaManager mm = MediaManager.getInstance(context);
		DBHelper helper = DBHelper.getInstance(context);
		
		// Search criteria gets set
		Chapter criteria = new Chapter(id, null, "");
		
		// Get chapter
		ArrayList<Object> objects = cm.retrieve(criteria, helper);
		Chapter chapter = (Chapter) objects.get(0);
		
		// Get chapter choices
		Choice choiceCrit = new Choice(null, null, id);
		objects = chom.retrieve(choiceCrit, helper);
		chapter.setChoices(Utilities.objectsToChoices(objects));
		
		// Get media (photos/illustrations)
		// TODO implement this
		
		return chapter;
	}
	
	/** 
	 * Retrieves a complete story (including chapters, any photos, 
	 * illustrations, and choices).
	 * 
	 * 
	 * @return
	 */
	public Story getCompleteStory(UUID id, Context context){
		//TODO everything
		
		return null;
	}
	
	public void updateObject(Object object, int type) {
		// TODO SWITCH STATEMENT
				
	}

}
