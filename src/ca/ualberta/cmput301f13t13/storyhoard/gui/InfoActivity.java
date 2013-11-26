package ca.ualberta.cmput301f13t13.storyhoard.gui;

import ca.ualberta.cmput301f13t13.storyhoard.R;
//import ca.ualberta.cmput301f13t13.storyhoard.R.layout;
//import ca.ualberta.cmput301f13t13.storyhoard.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
