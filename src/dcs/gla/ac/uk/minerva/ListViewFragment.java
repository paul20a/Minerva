package dcs.gla.ac.uk.minerva;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewFragment extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//Inflate selection view
	    View v = inflater.inflate(R.layout.activity_select, container, false);   
	    //create the listView
	    final ListView lView =  (ListView) v.findViewById(R.id.list);
	    //update this so SelectActivity is not required, stop using pList from SelectActivity
		lView.setAdapter(new MinervaBaseAdapter(getActivity(),( ((SelectActivity) getActivity()).getpList()))) ;
		//listen for click Actions
		lView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lView.getItemAtPosition(position);
                POI point = (POI)o;
                Intent detailIntent = new Intent(getActivity(), MainActivity.class);
                //Need to update this class is too dependent on SelectActivity
                detailIntent.putExtra(SelectActivity.NAME, point.getName());
                detailIntent.putExtra(SelectActivity.DESCRIPTION, point.getDescription());
                Toast.makeText(getActivity(), "Loading details for " + " " + point.getName()
                		, Toast.LENGTH_LONG).show();
                startActivity(detailIntent);
            }
        });

	    return v;
	}

	
	
	public static ListViewFragment newInstance(String text) {

		ListViewFragment f = new ListViewFragment();
		//update this section to pass relevant data to the list
		//this will provide extra usability of the list fragment
	    return f;
	}
}
