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

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cs.c301f13t13.backend.Chapter;

/**
 * Class which handles taking the Choice object and adapting it to a displayable
 * view type
 * 
 * @author alexanderwong
 * 
 */
public class AdapterChapters extends ArrayAdapter<Chapter> {

	Context context;
	int layoutResourceID;
	ArrayList<Chapter> data = new ArrayList<Chapter>();

	public AdapterChapters(Context context, int layoutResourceId,
			ArrayList<Chapter> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceID = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ChapterHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceID, parent, false);

			holder = new ChapterHolder();
			holder.chapterText = (TextView) row
					.findViewById(R.id.chapterBrowseText);
			row.setTag(holder);
		} else {
			holder = (ChapterHolder) row.getTag();
		}
		Chapter item = data.get(position);
		// Check for no text here
		if (item.getText().equals("")) {
			holder.chapterText.setText("<Blank Chapter>");
		} else {
			holder.chapterText.setText(item.getText());
		}
		return row;
	}

	static class ChapterHolder {
		TextView chapterText;
	}
}
