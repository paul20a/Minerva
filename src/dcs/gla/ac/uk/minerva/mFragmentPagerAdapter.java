package dcs.gla.ac.uk.minerva;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class mFragmentPagerAdapter extends FragmentPagerAdapter {
	static final int MENU_ITEMS = 2;

	public mFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		if(arg0==0)
		return FragmentListView.newInstance();
		else
		return FragmentMapView.newInstance();
	}

	@Override
	public int getCount() {
		return MENU_ITEMS;
	}

}
