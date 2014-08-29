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

public class MapViewFragment extends Fragment {
	private MapView minervaMapView;
	private MapController minervaMapController;
	MyItemizedOverlay myItemizedOverlay = null;
	MyLocationNewOverlay myLocationOverlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// inflate view
		View v = inflater.inflate(R.layout.map_view, container, false);
		// create map
		StartMap(v);
		return v;
	}

	public static MapViewFragment newInstance() {

		MapViewFragment f = new MapViewFragment();

		return f;
	}

	private void StartMap(View v) {
		minervaMapView = (MapView) v.findViewById(R.id.openmapview);
		minervaMapView.setBuiltInZoomControls(true);
		minervaMapView.setMultiTouchControls(true);
		minervaMapView.setMaxZoomLevel(18);
		minervaMapView.setMinZoomLevel(16);
		minervaMapView.setUseDataConnection(false);
		minervaMapController = (MapController) minervaMapView.getController();
		// set up marker.... get a nicer flag
		Drawable marker = this.getResources().getDrawable(R.drawable.flag);
		// Drawable
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

		// possibly useful but also likely to get annoying
		// myLocationOverlay.enableFollowLocation();
		OnItemGestureListener<OverlayItem> gListener = new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemLongPress(int arg0, OverlayItem arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onItemSingleTapUp(int arg0, OverlayItem item) {

				Toast.makeText(getActivity(), item.getTitle() + "\n",
						Toast.LENGTH_SHORT).show();
				Intent detailIntent = new Intent(getActivity(),
						MainActivity.class);
				// Need to update this class is too dependent on SelectActivity
				detailIntent.putExtra("pList",
						((SelectActivity) getActivity()).getpList());
				detailIntent.putExtra("pos", Integer.parseInt(item.getUid()));
				startActivity(detailIntent);
				return true;
			}
		};
		// Arrays
		ArrayList<Object> pList = ((SelectActivity) getActivity()).getpList();
		ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
		double latMin = 1000, latMax = -1000, lonMin = 1000, lonMax = -1000;
		// for each item in list add it to items array
		for (int i = 0; i < pList.size(); i++) {
			POI p = (POI) pList.get(i);
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
				itemList.add(new OverlayItem(Integer.toString(i), p.getName(),
						"", mPoint));
			}

		}

		// create new itemised overlay and add to mapView
		myItemizedOverlay = new MyItemizedOverlay(itemList, marker, marker, 0,
				gListener, resourceProxy);
		minervaMapView.getOverlays().add(myItemizedOverlay);

		// roughly the centre of the park add dynamically later where trail
		// starts
		final GeoPoint g = new GeoPoint(((latMin + latMax) / 2),
				((lonMin + lonMax) / 2));

		// this is a work around for centering issue in osmdriod v4.2
		minervaMapView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// Suppress version warnings
					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
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


	@Override
	public void onPause() {
		// TODO Handle release of gps tracking
		myLocationOverlay.disableMyLocation();
		super.onPause();
	}
	@Override
	public void onResume() {
		// TODO Handle release of gps tracking
		myLocationOverlay.enableMyLocation();
		super.onResume();
	}
}