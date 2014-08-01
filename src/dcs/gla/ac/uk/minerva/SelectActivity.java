package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class SelectActivity extends ActionBarActivity {
	
    public final static String NAME = "dcs.gla.ac.uk.NAME";
    public final static String DESCRIPTION = "dcs.gla.ac.uk.DESCRIPTION";
	private ArrayList<POI> pList;
	mFragmentPagerAdapter pagerAdapter;
	ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_select);
		XmlParser xParser= new XmlParser();
		
		try {
			InputStream in = this.getAssets().open("data.xml");
			setpList((ArrayList<POI>) xParser.parse(in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pagerAdapter = new mFragmentPagerAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) findViewById(R.id.pager);
		//viewpager is null here!!!!!!!!!!!!!!!!!
			viewPager.setAdapter(pagerAdapter);

		
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
            }
        });
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select, menu);
		return true;
	}

	
	public ArrayList<POI> getpList() {
		return pList;
	}

	public void setpList(ArrayList<POI> pList) {
		this.pList = pList;
	}

}
