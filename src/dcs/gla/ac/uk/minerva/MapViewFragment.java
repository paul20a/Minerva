package dcs.gla.ac.uk.minerva;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MapViewFragment extends Fragment {
	private MapView minervaMapView;
	private MapController minervaMapController;
	MyItemizedOverlay myItemizedOverlay = null;
	MyLocationNewOverlay myLocationOverlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map_view, container, false);
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

		// set up marker
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
		myItemizedOverlay = new MyItemizedOverlay(marker, resourceProxy);
		minervaMapView.getOverlays().add(myItemizedOverlay);
		final GeoPoint myPoint1 = new GeoPoint((int) (55.867401 * 1000000),
				(int) (1000000 * -4.282309));
		myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");

		// this is a work around for centering issue in osmdriod v4.2
		ViewTreeObserver vto = minervaMapView.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				minervaMapView.getController().setCenter(myPoint1);
			}
		});
	}
}