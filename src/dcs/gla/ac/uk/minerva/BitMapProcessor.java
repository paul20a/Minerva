package dcs.gla.ac.uk.minerva;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitMapProcessor extends AsyncTask<Integer, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private int data = 0;
    private Resources r;
    
    public BitMapProcessor(ImageView imageView,Resources r) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.r=r;
    }
    
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int h = options.outHeight;
		final int w = options.outWidth;
		int sampleSize = 1;

		if (h > reqHeight || w > reqWidth) {

			// work out sample size
			while (((h / 2) / sampleSize) > reqHeight
					&& ((w / 2) / sampleSize) > reqWidth) {
				sampleSize *= 2;
			}
		}
		return sampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
	
    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0]; 
       
            try{
                if(imageViewReference!=null){
                	int h=imageViewReference.get().getHeight();
                	int w=imageViewReference.get().getWidth();
                	Log.d("Size", h+"*"+w);
                	return decodeSampledBitmapFromResource(r, data, h, w);
                }
                }
                catch(Exception e){
                	this.cancel(true);
                }
            return null;
        }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {   
    	if (isCancelled()) {
        bitmap = null;
    }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            final BitMapProcessor bitMapProcessor =
                    getBitMapProcessor(imageView);
            if (this == bitMapProcessor && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    
    
    private static BitMapProcessor getBitMapProcessor(ImageView imageView) {
    	   if (imageView != null) {
    	       final Drawable drawable = imageView.getDrawable();
    	       if (drawable instanceof AsyncDrawable) {
    	           final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
    	           return asyncDrawable.getBitMapProcessor();
    	       }
    	    }
    	    return null;
    	}
    
    public static boolean cancelPotentialWork(int data, ImageView imageView) {
        final BitMapProcessor bitMapProcessor = getBitMapProcessor(imageView);

        if (bitMapProcessor != null) {
            final int bitmapData = bitMapProcessor.data;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
            	bitMapProcessor.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
}