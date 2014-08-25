package dcs.gla.ac.uk.minerva;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitmapProcessor> bitMapProcessorTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
    		BitmapProcessor bitmapProcessor) {
        super(res, bitmap);
        bitMapProcessorTaskReference =
            new WeakReference<BitmapProcessor>(bitmapProcessor);
    }

    public BitmapProcessor getBitMapProcessor() {
        return bitMapProcessorTaskReference.get();
    }
}