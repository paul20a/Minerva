package dcs.gla.ac.uk.minerva;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentViewPoint extends Fragment {

	public static final String ARG_POS = "position";
	private String title;
	private String description;
	private String image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.setRetainInstance(true);
		// Inflate selection view
		View v = inflater.inflate(R.layout.activity_main, container, false);
		// create the listView
		// retrieve information from intent
		image = this.getArguments().getString("image");
		description = this.getArguments().getString("description");
		title = this.getArguments().getString("title");
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
		Bitmap bitmap = BitmapProcessor.getCachedBitmap(String.valueOf(rID));
		if (bitmap != null) {
			Log.d("", "retrieved bitmap from cache");
			imageView.setImageBitmap(bitmap);
			return;
		} else if (BitmapProcessor.cancelPotentialWork(rID, imageView)) {
			final BitmapProcessor task = new BitmapProcessor(imageView, r);
			task.execute(rID);
			Bitmap b = null;
			final AsyncDrawable asyncDrawable = new AsyncDrawable(r, b, task);
			imageView.setImageDrawable(asyncDrawable);
		}
	}

	
	
	public static FragmentViewPoint newInstance(String text) {

		FragmentViewPoint f = new FragmentViewPoint();
		return f;
	}
}
