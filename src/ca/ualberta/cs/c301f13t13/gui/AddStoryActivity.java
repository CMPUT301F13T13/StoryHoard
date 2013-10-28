package ca.ualberta.cs.c301f13t13.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ca.ualberta.cmput301f13t13.storyhoard.R;

public class AddStoryActivity extends Activity {
	private EditText newTitle;
	private EditText newAuthor;
	private EditText newDescription;
	private Button firstChapter;
	private Button cancelStory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_story);
	
		newTitle = (EditText) findViewById(R.id.newStoryTitle);
		newAuthor = (EditText) findViewById(R.id.newStoryAuthor);
		newDescription = (EditText) findViewById(R.id.newStoryDescription);
		firstChapter = (Button) findViewById(R.id.addFirstChapter);
		cancelStory = (Button) findViewById(R.id.cancelStory);
		
		firstChapter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*
				 * Save all text forms to first story
				 * Switch to first chapter creation activity
				 */
				newTitle.getText();
				newAuthor.getText();
				newDescription.getText();
			}
		});
		
		cancelStory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Clear all fields, return to main activity
				finish();
			}
		});
	}

}
