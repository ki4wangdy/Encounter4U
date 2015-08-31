package com.imrub.shoulder.base.util.bitmaploader;

import android.graphics.Bitmap;

import com.imrub.shoulder.base.thread.ThreadFacade;

public abstract class ImageLoadTask implements Runnable{

	@Override
	public void run() {
		final Bitmap bitmap = doProcessBitmap();
		ThreadFacade.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				doBitmapInUIThread(bitmap);
			}
		});
	}

	/**
	 * 1. get bitmap from cache
	 * 2. download file to disk
	 * 3. get the bitmap from disk through sampleFactor
	 */
	public abstract Bitmap doProcessBitmap();
	
	/**
	 * @param bitmap after the sample factor , the bitmap is fit the imageView size
	 */
	public abstract void doBitmapInUIThread(final Bitmap bitmap);
	
}
