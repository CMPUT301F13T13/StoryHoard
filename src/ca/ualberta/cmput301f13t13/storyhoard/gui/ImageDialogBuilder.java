/**
 * Copyright 2013 Alex Wong, Ashley Brown, Josh Tate, Kim Wu, Stephanie Gil
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package ca.ualberta.cmput301f13t13.storyhoard.gui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.MediaController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;
import android.view.View;
import android.widget.LinearLayout;

public class ImageDialogBuilder {
	private AlertDialog illDialog;

	public AlertDialog getIllDialog() {
		return illDialog;
	}

	public void setIllDialog(AlertDialog illDialog) {
		this.illDialog = illDialog;
	}

	public void setDialog(final EditChapterActivity editChapterActivity,
			final View viewClicked, final LinearLayout illustrations) {
		AlertDialog.Builder alert = new AlertDialog.Builder(editChapterActivity);
		alert.setTitle("Permanently delete illustration?");
		final String[] methods = { "yes", "no" };
		alert.setSingleChoiceItems(methods, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							editChapterActivity.setMediaCon(MediaController
									.getInstance(editChapterActivity
											.getBaseContext()));
							Media media = (Media) viewClicked.getTag();
							editChapterActivity.getMediaCon().remove(
									media.getId());
							illustrations.removeView(viewClicked);
							break;
						case 1:
							break;
						}
						illDialog.dismiss();
					}
				});
		illDialog = alert.create();
		illDialog.show();
	}
}