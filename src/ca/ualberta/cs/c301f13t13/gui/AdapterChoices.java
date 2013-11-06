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
import ca.ualberta.cs.c301f13t13.backend.Choice;

/**
 * Class which handles taking the Choice object and adapting it to a displayable
 * view type
 * 
 * @author alexanderwong
 * 
 */
public class AdapterChoices extends ArrayAdapter<Choice> {

	Context context;
	int layoutResourceID;
	ArrayList<Choice> data = new ArrayList<Choice>();
	
	public AdapterChoices(Context context, int layoutResourceId, ArrayList<Choice> data) {
		super(context,  layoutResourceId, data);
		this.context = context;
		this.layoutResourceID = layoutResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ChoiceHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceID, parent, false);
			
			holder = new ChoiceHolder();
			holder.choiceText = (TextView) row.findViewById(R.id.choiceText);
			row.setTag(holder);
		} else {
			holder = (ChoiceHolder) row.getTag();
		}
		Choice item = data.get(position);
		// Check for no text here
		if (item.getText().equals("")) {
			holder.choiceText.setText("<No Choice Text>");
		} else {
			holder.choiceText.setText(item.getText());
		}
		return row;
	}
	
	static class ChoiceHolder {
		TextView choiceText;
	}
}
