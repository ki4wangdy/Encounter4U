package com.imrub.shoulder.base.util.bitmaploader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.base.util.cache.BitmapLruCache;

public class BitmapLoaderManager implements IBitmapCache{

	private BitmapLruCache mImageCache;
	
	private static BitmapLoaderManager mInstance ;
	private BitmapLoaderManager(){
		mImageCache = new BitmapLruCache();
	}

	public static final BitmapLoaderManager getInstance(){
		if(mInstance == null){
			mInstance = new BitmapLoaderManager();
		}
		return mInstance;
	}
	
	public void loadBitmap(String iconUrl, ImageView imageView, int width, int height, Bitmap defaultBitmap){
		if(iconUrl == null || "".equalsIgnoreCase(iconUrl)){
			if(imageView != null){
				imageView.setImageBitmap(defaultBitmap);
			}
			return ;
		}
		
		Bitmap bitmap = null;
		if (mImageCache != null) {
			bitmap = mImageCache.getBitmap(iconUrl);
		}

		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
			return ;
		}
		
		if(BitmapLoadUtil.getBitmapWorkerTask(imageView) == null){
			BitmapLoadTask task = new BitmapLoadTask(imageView, iconUrl, width, height);
			task.setBitmapCache(this);
			
			final AsyncDrawable asyncDrawable = new AsyncDrawable(AppContext.getResource(), task, defaultBitmap);
			imageView.setImageDrawable(asyncDrawable);
			addTheTaskToTheadPool(task);
		}
	}

	private void addTheTaskToTheadPool(BitmapLoadTask task){
		ThreadFacade.runOnImageLoaderThread(task);
	}
	
	public void setImageCache(BitmapLruCache cacheCallback) {
		mImageCache = cacheCallback;
	}

	public BitmapLruCache getImageCache() {
		return mImageCache;
	}
	
	@Override
	public Bitmap getBitmapFromCache(String iconUrl) {
		if(mImageCache != null){
			return mImageCache.getBitmapFromDiskCache(iconUrl);
		}
		return null;
	}

	@Override
	public Bitmap getBitmapFromDisk(String iconUrl) {
		if(mImageCache != null){
			return mImageCache.getBitmapFromDiskCache(iconUrl);
		}
		return null;
	}
	
	@Override
	public void putBitmapToCache(String iconUrl, Bitmap bitmap) {
		if(mImageCache != null){
			mImageCache.putBitmap(iconUrl, bitmap);
		}
	}
	
}
