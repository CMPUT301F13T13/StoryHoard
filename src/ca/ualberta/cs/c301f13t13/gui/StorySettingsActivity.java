package ca.ualberta.cs.c301f13t13.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;

/**
 * 
 * @author Alexander Wong
 * 
 */
public class StorySettingsActivity extends Activity {

	private ImageView storyImage;
	private TextView storyTitle;
	private TextView storyAuthor;
	private TextView storyDescription;
	private Button editStory;
	private Button readStory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_story);
		
		storyImage = (ImageView) findViewById(R.id.storyImage);
		storyTitle = (TextView) findViewById(R.id.storyTitle);
		storyAuthor = (TextView) findViewById(R.id.storyAuthor);
		storyDescription = (TextView) findViewById(R.id.storyDescription);
		editStory = (Button) findViewById(R.id.editStory);
		readStory = (Button) findViewById(R.id.viewFirstChapter);
		
		editStory.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// Go to an editStory activity
			}
			
		});
		
		readStory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Go to read the story, most likely just display the first activity
			}
		});
	}
}
