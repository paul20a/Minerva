package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SelectActivity extends ActionBarActivity {
	
    public final static String NAME = "dcs.gla.ac.uk.NAME";
    public final static String DESCRIPTION = "dcs.gla.ac.uk.DESCRIPTION";

    
	ArrayList<POI> pList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		XmlParser xParser= new XmlParser();
		final ListView lView =  (ListView) findViewById(R.id.list);
		
		try {
			
			InputStream in = this.getAssets().open("data.xml");
			pList=(ArrayList<POI>) xParser.parse(in);
			lView.setAdapter(new MinervaBaseAdapter(this, pList)) ;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		lView.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	                Object o = lView.getItemAtPosition(position);
	                POI point = (POI)o;
	                Intent detailIntent = new Intent(SelectActivity.this, MainActivity.class);
	                detailIntent.putExtra(NAME, point.getName());
	                detailIntent.putExtra(DESCRIPTION, point.getDescription());
	                Toast.makeText(SelectActivity.this, "Loading details for " + " " + point.getName()
	                		, Toast.LENGTH_LONG).show();
	                startActivity(detailIntent);
	            }
	        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select, menu);
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
