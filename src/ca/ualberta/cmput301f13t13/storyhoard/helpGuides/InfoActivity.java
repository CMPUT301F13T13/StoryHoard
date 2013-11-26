package ca.ualberta.cmput301f13t13.storyhoard.helpGuides;

import android.app.Activity;
//import ca.ualberta.cmput301f13t13.storyhoard.R.layout;
//import ca.ualberta.cmput301f13t13.storyhoard.R.menu;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;

public class InfoActivity extends Activity {

	private TextView infoText;
	private Button closeButton;
	private String info = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			info = extra.getString("theHelp");
		}
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
		if (info != null) {
			infoText.setText(info);
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
