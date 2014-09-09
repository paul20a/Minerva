package dcs.gla.ac.uk.minerva;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DirectionsDialogFragment extends DialogFragment  {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//Instantiate view
		this.getDialog().setTitle("Directions");
		View v = inflater.inflate(R.layout.directions_layout, container);
		TextView tView = (TextView) v.findViewById(R.id.txtViewDirect);
		tView.setText("Directions to be made");
		return v;
	}
}
