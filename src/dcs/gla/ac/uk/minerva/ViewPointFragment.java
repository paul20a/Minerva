package dcs.gla.ac.uk.minerva;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPointFragment extends Fragment {

	public static final String ARG_POS = "position";
	private int pos;
	private String title;
	private String description;
	private String image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate selection view
		View v = inflater.inflate(R.layout.activity_main, container, false);
		// create the listView
		super.onCreate(savedInstanceState);
		// retrieve information from intent
		pos = this.getArguments().getInt(ARG_POS);
		image = ((MainActivity) getActivity()).pList.get(pos).getImage();
		description = ((MainActivity) getActivity()).pList.get(pos)
				.getDescription();
		title = ((MainActivity) getActivity()).pList.get(pos).getName();
		// locate widgets
		TextView titleTextView = (TextView) v.findViewById(R.id.titleTextView);
		TextView descriptionTextView = (TextView) v
				.findViewById(R.id.textViewDesc);
		titleTextView.setText(title);
		descriptionTextView.setText(description);

		final ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
		imageView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					// Suppress version warnings
					@SuppressWarnings("deprecation")
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						// check version to remove listener with correct method
						// version.
						if (imageView.getHeight() > 0
								&& imageView.getWidth() > 0) {
							loadImage(imageView);
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
								imageView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							else
								imageView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
						}
					}
				});

		return v;
	}

	private void loadImage(ImageView imageView) {
		Resources r = getResources();
		int rID = (r
				.getIdentifier(image, "raw", getActivity().getPackageName()));
		if (BitMapProcessor.cancelPotentialWork(rID, imageView)) {
			final BitMapProcessor task = new BitMapProcessor(imageView, r);
			task.execute(rID);
			Bitmap b = null;
			final AsyncDrawable asyncDrawable = new AsyncDrawable(r, b, task);
			imageView.setImageDrawable(asyncDrawable);
		}
	}

	public static ViewPointFragment newInstance(String text) {

		ViewPointFragment f = new ViewPointFragment();
		return f;
	}
}
