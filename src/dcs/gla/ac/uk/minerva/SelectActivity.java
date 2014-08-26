package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class SelectActivity extends ActionBarActivity implements TabListener {

	mFragmentPagerAdapter pAdapter;
	SwipelessPager vPager;
	public ArrayList<Object> pList=new ArrayList<Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BitmapProcessor.EmptyBitmapCache();
		final ActionBar navBar = getActionBar();
		XmlPointParser xParser = new XmlPointParser();
		navBar.setIcon(getResources().getIdentifier("logo", "raw", "dcs.gla.ac.uk.minerva"));
		setContentView(R.layout.pager);
		try {
			Resources resources = getResources();
			int rID = resources.getIdentifier(getIntent().getStringExtra(
					TourActivity.TRAILPATH), "raw", getPackageName());  
			InputStream in= resources.openRawResource(rID);
			resources=null;
			pList = xParser.parse(in);
			
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		navBar.setHomeButtonEnabled(true);

		navBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		pAdapter = new mFragmentPagerAdapter(getSupportFragmentManager());
		vPager = (SwipelessPager) findViewById(R.id.pager);
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
		String s = "";
		if (i == 0)
			s = "List View";
		else
			s = "Map View";
		return s;
	}

	public ArrayList<Object> getpList() {
		return pList;
	}

	public void setpList(ArrayList<Object> pList) {
		this.pList = pList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {		
		switch (item.getItemId()) {
    // Respond to the action bar's Up/Home button
    case android.R.id.home:
    	this.finish();
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
