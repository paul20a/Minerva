package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TrailSelectActivity extends Activity {
	ArrayList<Object> tList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trail_select);
		
		XmlTrailParser xParser = new XmlTrailParser();
		
		try {
			InputStream in = this.getAssets().open("data.xml");
			tList = xParser.parse(in);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    //create the listView
	    final ListView lView =  (ListView) findViewById(R.id.list_trail);
	    //update this so SelectActivity is not required, stop using pList from SelectActivity
		lView.setAdapter(new TrailBaseAdapter(this,tList)) ;
		//listen for click Actions
		lView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = lView.getItemAtPosition(position);
                Trail trail = (Trail)o;
                Intent detailIntent = new Intent(TrailSelectActivity.this, MainActivity.class);
                //Need to update this class is too dependent on SelectActivity
                detailIntent.putExtra(SelectActivity.NAME, trail.getTitle());
                detailIntent.putExtra(SelectActivity.DESCRIPTION, trail.getDescription());
                Toast.makeText(TrailSelectActivity.this, "Loading details for " + " " + trail.getTitle()
                		, Toast.LENGTH_LONG).show();
                startActivity(detailIntent);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trail_select, menu);
		return true;
	}

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
}
