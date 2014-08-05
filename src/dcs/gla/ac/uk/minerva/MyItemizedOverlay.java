package dcs.gla.ac.uk.minerva;

import java.util.List;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.drawable.Drawable;

public class MyItemizedOverlay extends ItemizedOverlayWithFocus<OverlayItem> {
 
	public MyItemizedOverlay(
			List<OverlayItem> aList,
			Drawable pMarker,
			Drawable pMarkerFocused,
			int pFocusedBackgroundColor,
			org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> aOnItemTapListener,
			ResourceProxy pResourceProxy) {
		super(aList, pMarker, pMarkerFocused, pFocusedBackgroundColor,
				aOnItemTapListener, pResourceProxy);
		// TODO Auto-generated constructor stub
	}


	  
	 public void addItem(GeoPoint p, String title, String snippet){
	  OverlayItem newItem = new OverlayItem(title, snippet, p);
	  //align image
	  newItem.setMarkerHotspot(OverlayItem.HotspotPlace.LOWER_LEFT_CORNER);
	  super.addItem(newItem);
	  populate(); 
	 }

}
