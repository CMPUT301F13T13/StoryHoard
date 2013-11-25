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
package ca.ualberta.cmput301f13t13.storyhoard.gui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.ualberta.cmput301f13t13.storyhoard.R;
import ca.ualberta.cmput301f13t13.storyhoard.dataClasses.Story;

/**
 * Class which handles adapting the Story object to a displayable view type
 * 
 * @author alexanderwong
 * 
 */
public class AdapterStories extends ArrayAdapter<Story> {

	Context context;
	int layoutResourceId;
	ArrayList<Story> data = new ArrayList<Story>();

	public AdapterStories(Context context, int layoutResourceId,
			ArrayList<Story> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StoryHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new StoryHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.storyText);
			holder.imageItem = (ImageView) row.findViewById(R.id.storyImage);
			row.setTag(holder);
		} else {
			holder = (StoryHolder) row.getTag();
		}

		Story item = data.get(position);
		// Check for no text here
		if (item.getTitle().equals("")) {
			holder.txtTitle.setText("<No Title>");
		} else {
			holder.txtTitle.setText(item.getTitle());
		}
		// Implement this when stories actually have pictures
		// holder.imageItem.setImageBitmap(item.getImage());
		holder.imageItem.setImageBitmap((Bitmap) BitmapFactory.decodeResource(
				context.getResources(), R.drawable.book));
		return row;

	}

	static class StoryHolder {
		TextView txtTitle;
		ImageView imageItem;
	}
}
