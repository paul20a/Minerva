package dcs.gla.ac.uk.minerva;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class mFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private int size;
	
	public mFragmentStatePagerAdapter(FragmentManager fm,int s) {
		super(fm);
		this.size=s;		
	}

	@Override
	public Fragment getItem(int i) {
        Fragment fragment = new ViewPointFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(ViewPointFragment.ARG_POS, i);
        fragment.setArguments(args);
        return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}

}
