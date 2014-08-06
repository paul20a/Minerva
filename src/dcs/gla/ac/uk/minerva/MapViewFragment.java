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
				
				Toast.makeText(getActivity(), 
						 item.getTitle() + "\n"
						 + item.getPoint().getLatitudeE6() + " : " + item.getPoint().getLongitudeE6(), 
						 Toast.LENGTH_LONG).show();
						 return true;
			}
		};
		// Arrays 
		ArrayList<Object> pList = ((SelectActivity) getActivity()).getpList();
		ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
		
		//for each item in list add it to items array
		for (int i = 0; i < pList.size(); i++) {
			GeoPoint mPoint = new GeoPoint(
					(int) (((POI) pList.get(i)).getLat() * 1000000), (int) (((POI) pList
							.get(i)).getLon() * 1000000));
			itemList.add(new OverlayItem(((POI) pList.get(i)).getName(), "", mPoint));
		}
		
		//create new itemised overlay and add to mapView
		myItemizedOverlay = new MyItemizedOverlay(itemList, marker, marker, 0,
				gListener, resourceProxy);
		minervaMapView.getOverlays().add(myItemizedOverlay);
		
		//roughly the centre of the park add dynamically later where trail starts
		final GeoPoint g = new GeoPoint(55.965129, -3.208747);
	
		// this is a work around for centering issue in osmdriod v4.2
		minervaMapView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// Suppress version warnings
					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						minervaMapView.getController().setCenter(g);
						minervaMapView.getController().setZoom(16);
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
}