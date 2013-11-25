package ca.ualberta.cmput301f13t13.storyhoard.gui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import ca.ualberta.cmput301f13t13.storyhoard.R;

public class IntroScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro_screen);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		Thread loadingTime = new Thread() {
			public void run() {
				try {
					sleep(3000);
					Intent intent = new Intent(IntroScreen.this,
							ViewBrowseStories.class);
					startActivity(intent);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					finish();
				}
			}
		};
		loadingTime.start();
	}
}