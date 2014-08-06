package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * @author Paul Cairney
 *
 */
public abstract class MinervaBaseAdapter extends BaseAdapter {
	
	private ArrayList<Object> content;

	
	public MinervaBaseAdapter(Context context,ArrayList<Object> in) {
		content=in;
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


}
