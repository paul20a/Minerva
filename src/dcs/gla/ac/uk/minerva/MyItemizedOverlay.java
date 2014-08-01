package dcs.gla.ac.uk.minerva;

import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> itemList = new ArrayList<OverlayItem>();
	 
	 public MyItemizedOverlay(Drawable pDefaultMarker,
	   ResourceProxy pResourceProxy) {
	  super(pDefaultMarker, pResourceProxy);
	  // TODO Auto-generated constructor stub
	 }
	  
	 public void addItem(GeoPoint p, String title, String snippet){
	  OverlayItem newItem = new OverlayItem(title, snippet, p);
	  newItem.setMarkerHotspot(OverlayItem.HotspotPlace.LOWER_LEFT_CORNER);
	  itemList.add(newItem);
	  populate(); 
	 }
	 
	 @Override
	 public boolean onSnapToItem(int arg0, int arg1, Point arg2, IMapView arg3) {
	  // TODO Auto-generated method stub
	  return false;
	 }
	 
	 @Override
	 protected OverlayItem createItem(int i) {
	  // TODO Auto-generated method stub
	  return itemList.get(i);
	 }
	 
	 @Override
	 public int size() {
	  // TODO Auto-generated method stub
	  return itemList.size();
	 }
}
