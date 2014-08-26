package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TourActivity extends FragmentActivity implements OnClickListener {
	public final static String TITLE = "dcs.gla.ac.uk.TITLE";
	public final static String TRAILPATH = "dcs.gla.ac.uk.TRAILPATH";
	
	ArrayList<Object> tList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//As this is lead activity initialise global image cache
		BitmapProcessor.initBitmapCache();
		
		setContentView(R.layout.activity_trail_select);
		XmlTrailParser xParser = new XmlTrailParser();
		getActionBar().setIcon(getResources().getIdentifier("logo", "raw", "dcs.gla.ac.uk.minerva"));
	
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

		Bundle bundle = new Bundle();
		bundle.putString("title", ((Trail)tList.get(0)).getTitle());
		bundle.putString("image", ((Trail)tList.get(0)).getImage());
		bundle.putString("description", ((Trail)tList.get(0)).getDescription());
		
		if (savedInstanceState == null) {
			ViewPointFragment vpf = ViewPointFragment.newInstance("");
			vpf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.trail_container, vpf ).commit();
		}
		Button startBtn = (Button) findViewById(R.id.start_btn);
		startBtn.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//holder for options
		int id = item.getItemId();
		if (id == R.id.audio_settings) {

			}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==(R.id.start_btn)){
			Intent intent=new Intent(this, SelectActivity.class);
			intent.putExtra(TRAILPATH, ((Trail)tList.get(0)).getFile());
			startActivity(intent);
			}
		
	}
}
