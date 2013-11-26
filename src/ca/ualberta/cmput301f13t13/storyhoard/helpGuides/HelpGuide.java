package ca.ualberta.cmput301f13t13.storyhoard.helpGuides;

import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.R.layout;
import ca.ualberta.cmput301f13t13.storyhoard.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HelpGuide extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_guide);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help_guide, menu);
		return true;
	}

}
