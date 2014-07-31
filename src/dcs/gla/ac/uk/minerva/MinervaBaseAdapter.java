package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Paul Cairney
 *
 */
public class MinervaBaseAdapter extends BaseAdapter {
	
	private static ArrayList<POI> content;
	private LayoutInflater lInflater;
	
	
	/**
	 * 
	 * 
	 * @param context
	 * @param in 
	 */
	public MinervaBaseAdapter(Context context,ArrayList<POI> in) {
		content=in;
		lInflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return content.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		Object o=null;
		if(this.getCount()!=0) {
			o=content.get(position);
		}
		return o;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

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
        holder.nameTextView.setText(content.get(position).getName());
        holder.descTextView.setText(content.get(position).getDescription());
 
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
