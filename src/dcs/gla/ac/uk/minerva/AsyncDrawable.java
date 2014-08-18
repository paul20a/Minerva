package dcs.gla.ac.uk.minerva;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<BitMapProcessor> bitMapProcessorTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
    		BitMapProcessor bitMapProcessor) {
        super(res, bitmap);
        bitMapProcessorTaskReference =
            new WeakReference<BitMapProcessor>(bitMapProcessor);
    }

    public BitMapProcessor getBitMapProcessor() {
        return bitMapProcessorTaskReference.get();
    }
}