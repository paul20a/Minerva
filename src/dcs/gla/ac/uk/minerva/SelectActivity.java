package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SelectActivity extends ActionBarActivity {
	ArrayList<POI> pList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		XmlParser xParser= new XmlParser();
		LinearLayout layout =  (LinearLayout) findViewById(R.id.list);
		
		try {
			
			InputStream in = this.getAssets().open("data.xml");
			pList=(ArrayList<POI>) xParser.parse(in);
			for (int i=0;i<pList.size();i++){
			
				TextView nameView=new TextView(this);
				nameView.setText(pList.get(i).getName());
				nameView.setId(i);
				nameView.setLayoutParams(new LayoutParams(
			            LayoutParams.WRAP_CONTENT,
			            LayoutParams.WRAP_CONTENT));
				
				TextView descView=new TextView(this);
				descView.setText(pList.get(i).getDescription());
				descView.setId(i);
				descView.setLayoutParams(new LayoutParams(
			            LayoutParams.WRAP_CONTENT,
			            LayoutParams.WRAP_CONTENT));
				
				layout.addView(nameView);
				layout.addView(descView);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
