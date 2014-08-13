package dcs.gla.ac.uk.minerva;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPointFragment extends Fragment{

	public static final String ARG_POS = "position";
	private String title;
	private String description;
	private String image;

	
	@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			//Inflate selection view
		    View v = inflater.inflate(R.layout.activity_main, container, false);   
		    //create the listView
			super.onCreate(savedInstanceState);
			// retrieve information from intent
			int i=this.getArguments().getInt(ARG_POS);
			image=((MainActivity) getActivity()).pList.get(i).getImage();
			description = ((MainActivity) getActivity()).pList.get(i).getDescription();
			title=((MainActivity) getActivity()).pList.get(i).getName();
			// locate widgets
			TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
			TextView descriptionTextView = (TextView) v.findViewById(R.id.textViewDesc);
		
			InputStream in = null;
			int rID =((MainActivity) getActivity()).getResources().getIdentifier(image, "raw", getActivity().getPackageName());  
			try{
			in = ((MainActivity) getActivity()).getResources().openRawResource(rID);
			titleTextView.setText(title);
			descriptionTextView.setText(description);
			ImageView imageView = (ImageView) v.findViewById(R.id.imageView);

			// load image
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			if (in != null) {
				Bitmap b = BitmapFactory.decodeStream(in);
				in.close();
				imageView.setImageBitmap(b);
			}
			}
			catch(IOException e){
				// TODO handle exception

			}
		    return v;
		}	
	

	public static ViewPointFragment newInstance(String text) {

		ViewPointFragment f = new ViewPointFragment();
		return f;
	}



}
