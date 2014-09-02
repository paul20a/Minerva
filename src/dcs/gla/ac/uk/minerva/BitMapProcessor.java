package dcs.gla.ac.uk.minerva;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class BitmapProcessor extends AsyncTask<Integer, Void, Bitmap> {
	private final WeakReference<ImageView> imageViewReference;
	private int data = 0;
	private Resources r;
	private static LruCache<String, Bitmap> bitmapCache;
	private static final int MEM_DIV=15;
	
	public BitmapProcessor(ImageView imageView, Resources r) {
		// Use a WeakReference to ensure the ImageView can be garbage collected
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.r = r;
	}

	public static void initBitmapCache(){
		
		//calcualte memory to allocate to cache
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int size = maxMemory / MEM_DIV;
			bitmapCache = new LruCache<String, Bitmap>(size) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					// size in kb rather than entries
					return bitmap.getByteCount() / 1024;
				}
			};
	}
	
	public static void EmptyBitmapCache(){
		//empty cache, allows cache to use same id for thumbnails and larger images
		bitmapCache.evictAll();
	}
	
	public int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int h = options.outHeight;
		final int w = options.outWidth;
		int sampleSize = 1;

		if (h > reqHeight || w > reqWidth) {
			// work out sample size
			while (((h) / sampleSize) > reqHeight
					&& ((w) / sampleSize) > reqWidth) {
				sampleSize *= 2;
			}
		}
		return sampleSize;
	}

	public Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// Decode bitmap dimensions only
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		//check  if cancelled before calculating sample rate
		if(this.isCancelled())
		{
			return null;
		}
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with new sampleSize
		options.inJustDecodeBounds = false;
		//check  if cancelled before decoding bitmap
		if(this.isCancelled())
		{
			return null;
		}
		return BitmapFactory.decodeResource(res, resId, options);
	}

	@Override
	protected Bitmap doInBackground(Integer... params) {
		data = params[0];
		// check image isn't recycled or task hasn't been cancelled
		if (imageViewReference != null || !this.isCancelled()) {
			ImageView iView=imageViewReference.get();
			//try to process the bitmap if not in cache
			try {
				// get dimensions of imageView
				int h = iView.getMeasuredHeightAndState();
				int w = iView.getMeasuredWidthAndState();
				// log image size for bug finding 
				final Bitmap bitmap = decodeSampledBitmapFromResource(r, data,
						w, h);
				//add bitmap to cache
				cacheBitmap(String.valueOf(params[0]),bitmap);
				return bitmap;
			} catch (Exception e) {
				this.cancel(true);
			}
		}
		return null;
	}

	// Once complete, see if ImageView is still around and set bitmap.
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		// if the thread is cancelled return null
		if (isCancelled()) {
			bitmap = null;
		} else if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			final BitmapProcessor bitMapProcessor = getBitMapProcessor(imageView);
			if (this == bitMapProcessor && imageView != null) {
				//set the imageViews image
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	private static BitmapProcessor getBitMapProcessor(ImageView imageView) {
		//if imageView isn't null
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			//if the drawable's is AsyncDrawable
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				// return the drawable's BitmapProcessor
				return asyncDrawable.getBitMapProcessor();
			}
		}
		return null;
	}

	public static boolean cancelPotentialWork(int data, ImageView imageView) {
		final BitmapProcessor bitMapProcessor = getBitMapProcessor(imageView);
		//check for null values
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
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	public void cacheBitmap(String key, Bitmap bitmap) {
		// ensure entries don't repeat
		if (getCachedBitmap(key) == null) {
			//add to cache
			bitmapCache.put(key, bitmap);
		}
	}

	public static Bitmap getCachedBitmap(String key) {
		//retrieve entry
		return bitmapCache.get(key);
	}
}