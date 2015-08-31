package com.imrub.shoulder.base.util.bitmaploader;

import android.graphics.Bitmap;

public interface IBitmapCache {
	public Bitmap getBitmapFromCache(final String iconUrl);
	public Bitmap getBitmapFromDisk(final String iconUrl);
	public void putBitmapToCache(String iconUrl, final Bitmap bitmap);
	
}
