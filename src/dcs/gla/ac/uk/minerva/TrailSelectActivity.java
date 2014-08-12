package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class TrailSelectActivity extends Activity {
	public final static String TITLE = "dcs.gla.ac.uk.TITLE";
	public final static String TRAILPATH = "dcs.gla.ac.uk.TRAILPATH";
	
	private ListView trailListView;
	ArrayList<Object> tList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trail_select);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		XmlTrailParser xParser = new XmlTrailParser();

		try {		
			Resources resources = getResources();
			int rID = resources.getIdentifier("trails", "raw", getPackageName());  
			InputStream in= resources.openRawResource(rID);
			tList = xParser.parse(in);
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// create the listView
		trailListView = (ListView) findViewById(R.id.list_trail);
		// update this so SelectActivity is not required, stop using pList from
		// SelectActivity
		trailListView.setAdapter(new TrailBaseAdapter(TrailSelectActivity.this,
				tList));
		// listen for click Actions
		trailListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
					Object o = trailListView.getItemAtPosition(position);
					Trail trail = (Trail) o;
					Intent detailIntent = new Intent(TrailSelectActivity.this,
							SelectActivity.class);
					// Need to update this class is too dependent on
					// SelectActivity
					detailIntent.putExtra(TrailSelectActivity.TITLE,
							trail.getTitle());
							
					detailIntent.putExtra(TrailSelectActivity.TRAILPATH,
							trail.getIdList());
					Toast.makeText(TrailSelectActivity.this,
							"Loading details for " + trail.getTitle(),
							Toast.LENGTH_LONG).show();
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
	        // Display the fragment as the main content.
	        return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
