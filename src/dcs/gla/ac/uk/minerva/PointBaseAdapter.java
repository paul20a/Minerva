package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Paul Cairney
 * 
 */
public class PointBaseAdapter extends MinervaBaseAdapter {

	public PointBaseAdapter(Context context, ArrayList<Object> in) {
		super(context, in);
		lInflater = LayoutInflater.from(context);
	}

	private LayoutInflater lInflater;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// holder to represent a single row
		ViewHolder holder;
		if (convertView == null) {
			// set up a new view consisting of textViews, assign the holder to
			// it.
			convertView = lInflater.inflate(R.layout.row_layout, parent, false);
			holder = new ViewHolder();
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.rowNameTextView);
			holder.thumbImageView = (ImageView) convertView
					.findViewById(R.id.thumbnail);

			convertView.setTag(holder);
		} else {
			// assign the holder to an existing view
			holder = (ViewHolder) convertView.getTag();
		}
		// update the view
		POI item = ((POI) super.getItem(position));
		holder.nameTextView.setText(item.getName());

		Context context = lInflater.getContext();
		Resources r = context.getResources();
		int rID = r.getIdentifier(item.getImage(), "raw",
				context.getPackageName());
		//set image, this method compress image to an appropriate size
		if (BitMapProcessor.cancelPotentialWork(rID, holder.thumbImageView)) {
			final BitMapProcessor task = new BitMapProcessor(holder.thumbImageView,r);
		    task.execute(rID);
		    Bitmap b = null;
	        final AsyncDrawable asyncDrawable =
	        new AsyncDrawable(r, b, task);
	        holder.thumbImageView.setImageDrawable(asyncDrawable);
		}
		return convertView;
	}


	static class ViewHolder {
		TextView nameTextView;
		ImageView thumbImageView;
	}

}
