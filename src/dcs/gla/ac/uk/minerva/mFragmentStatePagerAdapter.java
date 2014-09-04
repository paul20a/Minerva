package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class mFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	private int size;
	private ArrayList<Waypoint> points;
	
	public mFragmentStatePagerAdapter(FragmentManager fm,int s,ArrayList<Waypoint> a) {
		super(fm);
		this.size=s;
		this.points=a;
	}

	@Override
	public Fragment getItem(int i) {
        Fragment fragment = new FragmentViewPoint();
        Bundle args = new Bundle();
        // insert arguments as a bundle
        args.putString("image", points.get(i).getImage());
        args.putString("title", points.get(i).getTitle());
        args.putString("description", points.get(i).getDescription());
        fragment.setArguments(args);
        return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return size;
	}

}
