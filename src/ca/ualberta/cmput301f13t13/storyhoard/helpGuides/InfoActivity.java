package ca.ualberta.cmput301f13t13.storyhoard.helpGuides;

import android.app.Activity;
//import ca.ualberta.cmput301f13t13.storyhoard.R.layout;
//import ca.ualberta.cmput301f13t13.storyhoard.R.menu;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;

public class InfoActivity extends Activity {

	private TextView infoText;

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
	}

	/**
	 * Sets up activity fields
	 */
	public void setUpFields() {
		infoText = (TextView) findViewById(R.id.infoText);
	}
	
	/**
	 * Sets the text to the appropriate help guide
	 */
	public void setInfoText() {
		infoText.setText("Help guid goes here");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
