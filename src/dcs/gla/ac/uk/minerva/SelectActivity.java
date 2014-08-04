package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SelectActivity extends ActionBarActivity implements TabListener {

	public final static String NAME = "dcs.gla.ac.uk.NAME";
	public final static String DESCRIPTION = "dcs.gla.ac.uk.DESCRIPTION";
	mFragmentPagerAdapter pAdapter;
	ViewPager vPager;

	ArrayList<POI> pList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar navBar = getActionBar();
		XmlParser xParser = new XmlParser();

		setContentView(R.layout.pager);

		try {
			InputStream in = this.getAssets().open("data.xml");
			pList = (ArrayList<POI>) xParser.parse(in);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		navBar.setHomeButtonEnabled(false);

		navBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		pAdapter = new mFragmentPagerAdapter(getSupportFragmentManager());
		vPager = (ViewPager) findViewById(R.id.pager);
		vPager.setAdapter(pAdapter);

		vPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				navBar.setSelectedNavigationItem(position);
			}
		});
		// Setup Nav Bar
		for (int i = 0; i < pAdapter.getCount(); i++) {
			navBar.addTab(navBar.newTab().setText(getTabTitle(i))
					.setTabListener(this));
		}

	}
	
	private CharSequence getTabTitle(int i) {
		String s="";
		if(i==0)
			s="List View";
		else
			s="Map View";
		return s;
	}

	public ArrayList<POI> getpList() {
		return pList;
	}

	public void setpList(ArrayList<POI> pList) {
		this.pList = pList;
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

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		vPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
}
