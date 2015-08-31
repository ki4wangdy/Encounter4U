package com.imrub.shoulder.base.util.cache;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.LruCache;

public class BitmapLruCache {

	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5M
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10M

	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;

	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

	private DiskLruCache mDiskCache;
	private LruCache<String, Bitmap> mMemoryCache;

	public BitmapLruCache(ImageCacheParams cacheParams) {
		init(cacheParams);
	}

	public BitmapLruCache() {
		init(new ImageCacheParams());
	}

	public static BitmapLruCache findOrCreateCache(final FragmentActivity activity,
			final String uniqueName) {
		return findOrCreateCache(activity, new ImageCacheParams());
	}

	public static BitmapLruCache findOrCreateCache(final FragmentActivity activity,
			ImageCacheParams cacheParams) {

		// Search for, or create an instance of the non-UI RetainFragment
		final RetainFragment mRetainFragment = RetainFragment.findOrCreateRetainFragment(activity.getSupportFragmentManager());
		
		// See if we already have an ImageCache stored in RetainFragment
		BitmapLruCache imageCache = (BitmapLruCache) mRetainFragment.getObject();

		// No existing ImageCache, create one and store it in RetainFragment
		if (imageCache == null) {
			imageCache = new BitmapLruCache(cacheParams);
			mRetainFragment.setObject(imageCache);
		}
		
		return imageCache;
	}

	private void init(ImageCacheParams cacheParams) {
		// get a cache floder
		final File diskCacheDir = DiskLruCache.getDiskCacheDir();

		// Set up disk cache
		if (cacheParams.diskCacheEnabled) {
			mDiskCache = DiskLruCache.openCache(diskCacheDir, cacheParams.diskCacheSize);
			mDiskCache.setCompressParams(cacheParams.compressFormat, cacheParams.compressQuality);
			if (cacheParams.clearDiskCacheOnStart) {
				mDiskCache.clearCache();
			}
		}

		// Set up memory cache
		if (cacheParams.memoryCacheEnabled) {
			mMemoryCache = new LruCache<String, Bitmap>( cacheParams.memCacheSize) {
				/**
				 * Measure item size in bytes rather than units which is more
				 * practical for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return Utils.getBitmapSize(bitmap);
				}
			};
		}
	}

	public void putBitmap(String data, Bitmap bitmap) {
		if (data == null || bitmap == null) {
			return;
		}
		// Add to memory cache
		if (mMemoryCache != null && mMemoryCache.get(data) == null) {
			mMemoryCache.put(data, bitmap);
		}
		// Add to disk cache
		if (mDiskCache != null && !mDiskCache.containsKey(data)) {
			mDiskCache.put(data, bitmap);
		}
	}
	
	public Bitmap getBitmap(String data){
		if(data == null){
			return null ;
		}
		Bitmap bitmap = getBitmapFromMemCache(data);
		return bitmap != null ? bitmap : getBitmapFromDiskCache(data);
	}
	
	public Bitmap getBitmapFromMemCache(String data) {
		return mMemoryCache != null ? mMemoryCache.get(data) : null ;
	}

	public Bitmap getBitmapFromDiskCache(String data) {
		return mDiskCache != null ? mDiskCache.get(data) : null ;
	}

	public void clearCaches() {
		mDiskCache.clearCache();
		mMemoryCache.evictAll();
	}

	public static class ImageCacheParams {
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		public ImageCacheParams(){
		}
	}

}
