package com.imrub.shoulder.base.util.bitmaploader;

import java.io.File;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class BitmapLoadTask extends ImageLoadTask {

	@SuppressWarnings("unused")
	private static final int FADE_IN_TIME = 200;
	
	private final WeakReference<ImageView> imageViewReference;

	private String mIconUrl;
	private IBitmapCache mCache;
	
	private int mWidth;
	private int mHeight;

	public BitmapLoadTask(ImageView imageView, String iconUrl, int width, int height) {
		imageViewReference = new WeakReference<ImageView>(imageView);
		mIconUrl = iconUrl;
		mWidth = width;
		mHeight = height;
	}

	public void setBitmapCache(IBitmapCache cache) {
		mCache = cache;
	}

	private ImageView getAttachedImageView() {
		final ImageView imageView = imageViewReference.get();
		final BitmapLoadTask bitmapWorkerTask = BitmapLoadUtil.getBitmapWorkerTask(imageView);
		if (this == bitmapWorkerTask) {
			return imageView;
		}
		return null;
	}

	@Override
	public Bitmap doProcessBitmap() {
		Bitmap bitmap = null;
		
		// 1. get bitmap from disk
		if (mCache != null && getAttachedImageView() != null) {
			bitmap = mCache.getBitmapFromDisk(mIconUrl);
			if(bitmap != null){
				return bitmap;
			}
		}

		// 2. download the bitmap to disk
		String filePath = downloadBitmapFromNet(mIconUrl);

		
		// 3. get the bitmap based on the path, and put the bitmap into the cache system
		if(filePath != null){
			bitmap = processBitmapBasedOnWidthAndHeight(filePath);
			return bitmap;
		}
		
		return null;
	}

	public String downloadBitmapFromNet(String iconUrl){
		File file = BitmapLoadUtil.downloadBitmap(iconUrl);
		if(file != null){
			return file.getAbsolutePath();
		}
		return null;
	}

	private Bitmap processBitmapBasedOnWidthAndHeight(String pathUrl){
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathUrl, options);

		// Calculate inSampleSize
		options.inSampleSize = BitmapLoadUtil.calculateInSampleSize(options, mWidth, mHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathUrl, options);
	}
	
	@Override
	public void doBitmapInUIThread(Bitmap bitmap) {
		final ImageView imageView = getAttachedImageView();
		if (bitmap != null && imageView != null) {
			setImageBitmap(imageView, bitmap);
		}
	}

	private void setImageBitmap(ImageView imageView, Bitmap bitmap) {
		// Transition drawable with a transparent drwabale and the final
		// bitmap
//		final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
//				new ColorDrawable(android.R.color.transparent),
//				new BitmapDrawable(AppContext.getAppContext().getResources(),
//						bitmap) });
//		// Set background to loading bitmap
//		// imageView.setBackgroundDrawable(new BitmapDrawable(mContext
//		// .getResources(), mLoadingBitmap));
//		imageView.setBackgroundResource(R.drawable.ic_launcher);
//
//		imageView.setImageDrawable(td);
//		td.startTransition(FADE_IN_TIME);
		imageView.setImageBitmap(bitmap);
	}

}
