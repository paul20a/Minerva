package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MinervaBaseAdapter extends BaseAdapter {
	
	private static ArrayList<POI> content;
	private LayoutInflater lInflater
	;
	public MinervaBaseAdapter(Context context,ArrayList<POI> in) {
		content=in;
		lInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return content.size();
	}

	@Override
	public Object getItem(int position) {
		Object o=null;
		if(this.getCount()!=0) {
			o=content.get(position);
		}
		return o;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.row_layout, parent, false);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.rowNameTextView);
            holder.descTextView = (TextView) convertView.findViewById(R.id.rowDescTextView);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.nameTextView.setText(content.get(position).getName());
        holder.descTextView.setText(content.get(position).getDescription());
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView nameTextView;
        TextView descTextView;
	}

}
