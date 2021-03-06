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

/**
 * This is a class for deleting an image.
 * This prompts the user to choose to delete an image or not.
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import ca.ualberta.cmput301f13t13.storyhoard.controllers.ChapterController;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Media;

public class DeleteImageDialog {
	private AlertDialog deleteDialog;
	private ChapterController chapCon;

	public void setDeleteDialog(final EditChapterActivity editChapterActivity,
			final View viewClicked, final LinearLayout illustrations) {
		AlertDialog.Builder alert = new AlertDialog.Builder(editChapterActivity);
		alert.setTitle("Delete illustration?");
		final String[] methods = { "yes", "no" };
		alert.setSingleChoiceItems(methods, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
						case 0:
							chapCon = ChapterController.getInstance(editChapterActivity);
							Media media = (Media) viewClicked.getTag();
							chapCon.removeIllustration(media);
							illustrations.removeView(viewClicked);
							editChapterActivity.updateICData();
							break;
						case 1:
							break;
						}
						deleteDialog.dismiss();
					}
				});
		deleteDialog = alert.create();
		deleteDialog.show();
	}
}