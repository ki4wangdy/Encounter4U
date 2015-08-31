package com.imrub.shoulder.base.util.bitmaploader;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AsyncDrawable extends BitmapDrawable{
	
	private final WeakReference<BitmapLoadTask> bitmapWorkerTaskReference;

	public AsyncDrawable(Resources res,BitmapLoadTask bitmapWorkerTask, final Bitmap defaultBitmap) {
		super(res,defaultBitmap);
		bitmapWorkerTaskReference = new WeakReference<BitmapLoadTask>(bitmapWorkerTask);
	}

	public BitmapLoadTask getBitmapWorkerTask() {
		return bitmapWorkerTaskReference.get();
	}
}
