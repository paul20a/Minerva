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
	    View v = inflater.inflate(R.layout.activity_select, container, false);   

	    final ListView lView =  (ListView) v.findViewById(R.id.list);

		lView.setAdapter(new MinervaBaseAdapter(getActivity(),( ((SelectActivity) getActivity()).getpList()))) ;

		lView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lView.getItemAtPosition(position);
                POI point = (POI)o;
                Intent detailIntent = new Intent(getActivity(), MainActivity.class);
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
	   // Bundle b = new Bundle();
	   // b.putString("msg", text);

	    //f.setArguments(b);

	    return f;
	}
}
