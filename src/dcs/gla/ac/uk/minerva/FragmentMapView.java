package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

public class FragmentMapView extends Fragment implements
		OnItemGestureListener<OverlayItem> {
	private MapView minervaMapView;
	CustomItemizedOverlay customItemizedOverlay = null;
	MyLocationNewOverlay myLocationOverlay;
	double latMin = 1000, latMax = -1000, lonMin = 1000, lonMax = -1000;
	Drawable marker;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate view
		View v = inflater.inflate(R.layout.map_view, container, false);
		// create map
		StartMap(v);
		return v;
	}

	private void StartMap(View v) {
		//get the MapView
		minervaMapView = (MapView) v.findViewById(R.id.openmapview);
		//turn built in zoom controls and pinch zooming on
		minervaMapView.setBuiltInZoomControls(true);
		minervaMapView.setMultiTouchControls(true);
		//disable Internet access
		minervaMapView.setUseDataConnection(false);
		final MapController minervaMapController = (MapController) minervaMapView.getController();
		//create a custom marker
		createMarkerDrawable();
		//create a default resource proxy
		ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
				getActivity().getApplicationContext());
		// allow gps to locate position
		setupMyLocationGps();
		// process items
		ArrayList<OverlayItem> itemList = processItems();

		// create new itemised overlay and add to mapView
		customItemizedOverlay = new CustomItemizedOverlay(itemList, marker,
				marker, 0, this, resourceProxy);
		minervaMapView.getOverlays().add(customItemizedOverlay);

		//locate mid point of the trail
		final GeoPoint g = new GeoPoint(((latMin + latMax) / 2),
				((lonMin + lonMax) / 2));

		// this is a work around for centring issue in osmdriod v4.2
		minervaMapView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// Suppress version warnings
					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						//set center point to mid point of trail and zoom to it
						minervaMapController.setZoom(17);
						minervaMapController.setCenter(g);

						// check version to remove listener with correct method
						// version.
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
							minervaMapView.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						else
							minervaMapView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
					}
				});
	}

	/**
	 * 
	 * process a list of Points into a list of overlay items
	 * minimum and maximum latitudes and longitude are also worked out here
	 * 
	 * @return ArrayList of Overaly items
	 */
	private ArrayList<OverlayItem> processItems() {
		ArrayList<Object> pList = ((ActivitySelect) getActivity()).getpList();
		ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
		// for each item in list add it to items array
		for (int i = 0; i < pList.size(); i++) {
			Waypoint p = (Waypoint) pList.get(i);
			Double newLat = p.getLat();
			Double newLon = p.getLon();
			if (newLon != null && newLat != null) {
				GeoPoint mPoint = new GeoPoint((int) (newLat * 1000000),
						(int) (newLon * 1000000));
				if (newLat < latMin) {
					latMin = newLat;
				} else if (newLat > latMax) {
					latMax = newLat;
				}
				if (newLon < lonMin) {
					lonMin = newLon;
				} else if (newLon > lonMax) {
					lonMax = newLon;
				}
				itemList.add(new OverlayItem(Integer.toString(i), p.getTitle(),
						"", mPoint));
			}

		}
		return itemList;
	}

	/**
	 * creates a custom marker to display items on the map
	 */
	private void createMarkerDrawable() {
		// TODO Auto-generated method stub
		marker = this.getResources().getDrawable(R.drawable.flag);
		int markerWidth = marker.getIntrinsicWidth();
		int markerHeight = marker.getIntrinsicHeight();
		marker.setBounds(0, markerHeight, markerWidth, 0);
	}

	/**
	 * setup Location Overlay, this code enables the user location to display on
	 * the map
	 */
	private void setupMyLocationGps() {
		myLocationOverlay = new MyLocationNewOverlay(getActivity(),
				minervaMapView);
		minervaMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		// TODO Handle release of gps tracking
		myLocationOverlay.disableMyLocation();
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Handle release of gps tracking
		myLocationOverlay.enableMyLocation();
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener#
	 * onItemLongPress(int, java.lang.Object)
	 */
	@Override
	public boolean onItemLongPress(int arg0, OverlayItem arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener#
	 * onItemSingleTapUp(int, java.lang.Object)
	 */
	@Override
	public boolean onItemSingleTapUp(int arg0, OverlayItem item) {

		Toast.makeText(getActivity(), item.getTitle() + "\n",
				Toast.LENGTH_SHORT).show();
		Intent detailIntent = new Intent(getActivity(), ActivityMain.class);
		// Need to update this class is too dependent on ActivitySelect
		detailIntent.putExtra("pList",
				((ActivitySelect) getActivity()).getpList());
		detailIntent.putExtra("pos", Integer.parseInt(item.getUid()));
		startActivity(detailIntent);
		return true;
	}
}