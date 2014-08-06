package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Paul Cairney
 *
 */
public class PointBaseAdapter extends MinervaBaseAdapter {
	
	public PointBaseAdapter(Context context, ArrayList<Object> in) {
		super(context, in);
		lInflater=LayoutInflater.from(context);
	}

	private LayoutInflater lInflater;
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//holder to represent a single row
        ViewHolder holder;
        if (convertView == null) {
        	//set up a new view consisting of textViews, assign the holder to it.
            convertView = lInflater.inflate(R.layout.row_layout, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.rowNameTextView);
            holder.descTextView = (TextView) convertView.findViewById(R.id.rowDescTextView);
 
            convertView.setTag(holder);
        } else {
        	//assign the holder to an existing view
            holder = (ViewHolder) convertView.getTag();
        }
        //update the view
        holder.nameTextView.setText(((POI)super.getItem(position)).getName());
        holder.descTextView.setText(((POI)super.getItem(position)).getDescription());
 
        return convertView;
    }
 
    /**
     * @author Paul Cairney
     * class to hold a list entry
     */
    static class ViewHolder {
        TextView nameTextView;
        TextView descTextView;
	}

}
