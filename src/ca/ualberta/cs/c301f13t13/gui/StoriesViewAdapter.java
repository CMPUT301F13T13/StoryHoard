package ca.ualberta.cs.c301f13t13.gui;

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
import ca.ualberta.cs.c301f13t13.backend.Story;

public class StoriesViewAdapter extends ArrayAdapter<Story> {

	Context context;
	int layoutResourceId;
	ArrayList<Story> data = new ArrayList<Story>();

	public StoriesViewAdapter(Context context, int layoutResourceId,
			ArrayList<Story> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}

		Story item = data.get(position);
		holder.txtTitle.setText(item.getTitle());
		// Implement this when stories actually have pictures
		// holder.imageItem.setImageBitmap(item.getImage());
		holder.imageItem.setImageBitmap((Bitmap) BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher));
		return row;

	}

	static class RecordHolder {
		TextView txtTitle;
		ImageView imageItem;
	}
}
