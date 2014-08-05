package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class MapViewFragment extends Fragment {
	private MapView minervaMapView;
	private MapController minervaMapController;
	MyItemizedOverlay myItemizedOverlay = null;
	MyLocationNewOverlay myLocationOverlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//inflate view
		View v = inflater.inflate(R.layout.map_view, container, false);
		//create map
		StartMap(v);
		return v;
	}

	public static MapViewFragment newInstance(String text) {

		MapViewFragment f = new MapViewFragment();

		return f;
	}
	

	private void StartMap(View v) {
		minervaMapView = (MapView) v.findViewById(R.id.openmapview);
		minervaMapView.setBuiltInZoomControls(true);
		minervaMapController = (MapController) minervaMapView.getController();
		minervaMapController.setZoom(15);

		// set up marker.... get a nicer flag
		Drawable marker = this.getResources().getDrawable(R.drawable.flag);
		// Drawable
		// marker=getResources().getDrawable(android.R.drawable.rsz_flag);
		int markerWidth = marker.getIntrinsicWidth();
		int markerHeight = marker.getIntrinsicHeight();
		marker.setBounds(0, markerHeight, markerWidth, 0);
		ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
				getActivity().getApplicationContext());

		// my location
		myLocationOverlay = new MyLocationNewOverlay(getActivity(),
				minervaMapView);
		minervaMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
		
		//possibly useful but also likely to get annoying
		//myLocationOverlay.enableFollowLocation();

		// add markers to POI
		ArrayList<POI> pList=((SelectActivity) getActivity()).getpList();
		for(int i=0;i<pList.size();i++){
		myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
		minervaMapView.getOverlays().add(myItemizedOverlay);
		GeoPoint mPoint = new GeoPoint((int) (pList.get(i).getLat() * 1000000),
				(int) (pList.get(i).getLon()*1000000));
		myItemizedOverlay.addItem(mPoint, "mPoint"+i, "mPoint"+i);
		}
		final GeoPoint g=new GeoPoint(55.965129, -3.208747);
		// this is a work around for centering issue in osmdriod v4.2

		minervaMapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    

		    //work around centring bug in 4.2
			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
		    public void onGlobalLayout() {
				minervaMapView.getController().setCenter(g);
				minervaMapView.getController().setZoom(16);
		        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
		        	minervaMapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		        else
		        	minervaMapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    }
		});
		
	}
}