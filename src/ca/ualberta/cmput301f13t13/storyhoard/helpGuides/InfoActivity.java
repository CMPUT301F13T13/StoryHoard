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
package ca.ualberta.cmput301f13t13.storyhoard.helpGuides;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;

/**
 * Handles displaying the help messages for each activity which passes an intent
 * to this activity and starts it. Takes the string from the activity passed
 * bundle and displays a message from it.
 * 
 * @author Joshua Tate
 * 
 */
public class InfoActivity extends Activity {

	private TextView infoText;
	private Button closeButton;
	private String info = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpFields();
		setInfoText();
		setCloseListener();
	}

	/**
	 * Sets up activity fields
	 */
	public void setUpFields() {
		infoText = (TextView) findViewById(R.id.infoText);
		closeButton = (Button) findViewById(R.id.closeButton);
	}

	/**
	 * Sets the text to the appropriate help guide
	 */
	public void setInfoText() {
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			info = extra.getString("theHelp");
		}
		infoText.setText(info);
	}

	/**
	 * Sets Close Button Listener
	 */
	public void setCloseListener() {
		closeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

}
