package ca.ualberta.cmput301f13t13.storyhoard.local;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Chapter;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Choice;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

public class Syncher {
	private static StoryManager storyMan = null;
	private static ChapterManager chapMan = null;
	private static MediaManager mediaMan = null;
	private static ChoiceManager choiceMan = null;
	private static Syncher self = null;
	
	protected Syncher(Context context) {
		storyMan = StoryManager.getInstance(context);
		chapMan = ChapterManager.getInstance(context);
		mediaMan = MediaManager.getInstance(context);
		choiceMan = ChoiceManager.getInstance(context);
	}
	
	public static Syncher getInstance(Context context) {
		if (self == null) {
			self = new Syncher(context);
		}
		return self;
	}

	public void syncStoryFromServer(Story story) {
		ArrayList<UUID> medias = new ArrayList<UUID>();
		storyMan.syncStory(story);
		
		for (Chapter chap : story.getChapters()) {
			for (Media photo : chap.getPhotos()) {
				medias.add(photo.getId());
				mediaMan.syncMedia(photo);
			}
			for (Media ill : chap.getIllustrations()) {
				medias.add(ill.getId());
				mediaMan.syncMedia(ill);
			}
			for (Choice choice : chap.getChoices()) {
				choiceMan.syncChoice(choice);
			}
			chapMan.syncChapter(chap);
			mediaMan.syncDeletions(medias, chap.getId());
		}
	}
	
    /**
     * Saves a bitmap to a location on the phone's sd card. Returns
     * the path of where the image was saved to.
     * 
     */
    public static String saveImageToSD(Bitmap bmp) {
            String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp";
            File folderF = new File(folder);
            if (!folderF.exists()) {
                    folderF.mkdir();
            }

            String imageFilePath;
            imageFilePath = folder + "/"
                                    + String.valueOf(System.currentTimeMillis()) + ".jpg";
            File imageFile = new File(imageFilePath);

            FileOutputStream fout;
            try {
                    fout = new FileOutputStream(imageFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 85, fout);

                    fout.flush();
                    fout.close();
            } catch (IOException e) {
                    e.printStackTrace();
            }
            
            return imageFilePath;
    }	
    
    /**
     * Due to performance issues, Media objects don't actually hold Bitmaps.
     * A path to the location of the file is instead saved and used to 
     * retrieve bitmaps whenever needed. Media objects can also hold the
     * bitmaps after the have been converted to a string (Base64). 
     * </br>
     * Before a Story can be inserted into the server, all the images 
     * belonging to the story's chapters must have their bitmap strings
     * set. This is only done when the story is published to avoid doing
     * the expensive conversion unnecessarily (local stories only need to 
     * know the path).
     * </br>
     * 
     * This function takes care of setting all the Medias' bitmap strings.
     * 
     * @param story
     */
    public void prepareStory(Story story) {

            // get any media associated with the chapters of the story
            ArrayList<Chapter> chaps = story.getChapters();

            for (Chapter chap : chaps) {
                    ArrayList<Media> photos = chap.getPhotos();

                    for (Media photo : photos) {
                            photo.setBitmapString(photo.getBitmap());
                    }
                    chap.setPhotos(photos);

                    ArrayList<Media> ills = chap.getIllustrations();
                    for (Media ill : ills) {
                            ill.setBitmapString(ill.getBitmap());
                    }
                    chap.setIllustrations(ills);
            }
            story.setChapters(chaps);
    }
}
