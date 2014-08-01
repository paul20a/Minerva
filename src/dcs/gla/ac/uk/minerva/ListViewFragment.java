package dcs.gla.ac.uk.minerva;

import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewFragment extends ListFragment {

	ListView lView;	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		lView =  (ListView) getView().findViewById(R.id.list);
		
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
		return lView;
	}
}
