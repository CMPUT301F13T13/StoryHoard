/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.ualberta.cs.c301f13t13.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;
import ca.ualberta.cs.c301f13t13.backend.Choice;
import ca.ualberta.cs.c301f13t13.backend.Media;
import ca.ualberta.cs.c301f13t13.backend.ObjectType;
import ca.ualberta.cs.c301f13t13.backend.SHController;
import ca.ualberta.cs.c301f13t13.backend.Utilities;

/**
 * Views the chapter provided through the intent. Does not allow going backwards
 * through the activity stack.
 * 
 * @author Alexander Wong
 * 
 */
public class ViewChapter extends Activity {
	private Context context = this;
	private UUID storyID;
	private UUID chapterID;
	private SHController gc;
	private Chapter chapter;
	private ArrayList<Choice> choices = new ArrayList<Choice>();
	private ArrayList<Media> photoList;
	private ArrayList<Media> illList;
	private AdapterChoices choiceAdapter;
	private AlertDialog photoDialog;
	private LinearLayout illustrations;
	private LinearLayout photos;

	private TextView chapterContent;
	private ListView chapterChoices;
	private Button addPhotoButton;

	private static int BROWSE_GALLERY_ACTIVITY_REQUEST_CODE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	Uri imageFileUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_chapter);

		// Grab the necessary UUIDs and GC
		Bundle bundle = this.getIntent().getExtras();
		storyID = (UUID) bundle.get("storyID");
		chapterID = (UUID) bundle.get("chapterID");
		gc = SHController.getInstance(this);

		// Setup the activity fields
		chapterContent = (TextView) findViewById(R.id.chapterContent);
		chapterChoices = (ListView) findViewById(R.id.chapterChoices);
		addPhotoButton = (Button) findViewById(R.id.addPhotoButton);
		illustrations = (LinearLayout) findViewById(R.id.horizontalIllustraions);
		photos = (LinearLayout) findViewById(R.id.horizontalPhotos);

		// Setup the choices and choice adapters
		choiceAdapter = new AdapterChoices(this, R.layout.browse_choice_item,
				choices);
		chapterChoices.setAdapter(choiceAdapter);
		chapterChoices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Go to the chapter in question
				UUID nextChapter = choices.get(arg2).getNextChapter();
				Intent intent = new Intent(getBaseContext(), ViewChapter.class);
				intent.putExtra("storyID", storyID);
				intent.putExtra("chapterID", nextChapter);
				startActivity(intent);
				finish();
			}
		});

		/*
		 * IMPLEMENTATION NOT READY TO GO YET. COMMENTING OUT AND TOASTING NON
		 * IMPLEMENTED MESSAGE HERE.
		 */
		addPhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// AlertDialog.Builder alert = new AlertDialog.Builder(context);
				// // Set dialog title
				// alert.setTitle("Choose method:");
				// // Options that user may choose to add photo
				// final String[] methods = { "Take Photo",
				// "Choose from Gallery" };
				// alert.setSingleChoiceItems(methods, -1,
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(DialogInterface dialog, int item) {
				// switch (item) {
				// case 0:
				// takePhoto();
				// break;
				// case 1:
				// browseGallery();
				// break;
				// }
				// photoDialog.dismiss();
				// }
				// });
				// photoDialog = alert.create();
				// photoDialog.show();
				Toast.makeText(getBaseContext(),
						"Not implemented this iteration", Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	@Override
	public void onResume() {
		chapter = gc.getCompleteChapter(chapterID);
		choices.clear();
		// Check for no chapter text
		if (chapter.getText().equals("")) {
			chapterContent.setText("<No Chapter Content>");
		} else {
			chapterContent.setText(chapter.getText());
		}
		// Check for no choices
		if (chapter.getChoices().isEmpty()) {
			chapterContent.setText(chapterContent.getText()
					+ "\n\n<No Choices>");
		} else {
			choices.addAll(chapter.getChoices());
		}
		choiceAdapter.notifyDataSetChanged();
		super.onResume();

		photoList = chapter.getPhotos();
		illList = chapter.getIllustrations();

		// Insert Photos
		for (Media photo : photoList) {
			photos.addView(insertImage(photo));
		}

		// Insert Illustrations
		for (Media ill : illList) {
			illustrations.addView(insertImage(ill));
		}
	}

	/**
	 * CODE REUSE URL:
	 * http://android-er.blogspot.ca/2012/07/implement-gallery-like.html Date:
	 * Nov. 7, 2013 Author: Andr.oid Eric
	 */
	public View insertImage(Media photo) {
		Bitmap bm = Utilities.decodeSampledBitmapFromUri(photo.getUri(), 220,
				220);
		LinearLayout layout = new LinearLayout(getApplicationContext());

		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageBitmap(bm);

		layout.addView(imageView);
		return layout;
	}

	/**
	 * Code for taking a photo.
	 * 
	 * CODE RESUSE: LonelyTweeter Camera Code from Lab Author: Joshua Charles
	 * Campbell License: Unlicense Date: Nov. 7, 2013
	 */
	public void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + "jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
				|| requestCode == BROWSE_GALLERY_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

				Media photo = new Media(chapter.getId(), imageFileUri,
						Media.PHOTO);
				gc.addObject(photo, ObjectType.MEDIA);
				photos.addView(insertImage(photo));

			} else if (resultCode == RESULT_CANCELED) {
				System.out.println("cancelled taking a photo");
			} else {
				System.err.println("Error in taking a photo" + resultCode);
			}
		}
	}

	/**
	 * Code for taking a photo.
	 * 
	 * CODE RESUSE: LonelyTweeter Camera Code from Lab Author: Joshua Charles
	 * Campbell License: Unlicense Date: Nov. 7, 2013
	 */
	public void browseGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		String folder = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/tmp";
		File folderF = new File(folder);
		if (!folderF.exists()) {
			folderF.mkdir();
		}

		String imageFilePath = folder + "/"
				+ String.valueOf(System.currentTimeMillis()) + "jpg";
		File imageFile = new File(imageFilePath);
		imageFileUri = Uri.fromFile(imageFile);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

		startActivityForResult(intent, BROWSE_GALLERY_ACTIVITY_REQUEST_CODE);

	}
}
