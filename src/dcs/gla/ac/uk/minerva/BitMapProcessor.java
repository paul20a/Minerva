package dcs.gla.ac.uk.minerva;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
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
		bitmapCache=bitmapCache();
	}

	public LruCache<String,Bitmap> bitmapCache(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int size = maxMemory / MEM_DIV;
		
			bitmapCache = new LruCache<String, Bitmap>(size) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					// size in kb
					return bitmap.getByteCount() / 1024;
				}
			};
			return bitmapCache;
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

		// Decode bitmap dimensions only
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with new sampleSize
		options.inJustDecodeBounds = false;
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
				cacheBitmap(String.valueOf(params[0]),bitmap);
				Log.d("ImageSize", bitmap.getByteCount()/1024+"kb");
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
		// if
		if (isCancelled()) {
			bitmap = null;
		} else if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			final BitmapProcessor bitMapProcessor = getBitMapProcessor(imageView);
			if (this == bitMapProcessor && imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	private static BitmapProcessor getBitMapProcessor(ImageView imageView) {
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
		final BitmapProcessor bitMapProcessor = getBitMapProcessor(imageView);

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
			bitmapCache.put(key, bitmap);
		}
	}

	public static Bitmap getCachedBitmap(String key) {
		return bitmapCache.get(key);
	}
}