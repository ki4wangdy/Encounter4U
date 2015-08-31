/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imrub.shoulder.module.photopicker.imageUtils;

import java.io.FileDescriptor;
import java.lang.ref.WeakReference;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.imrub.shoulder.base.app.AppContext;
import com.imrub.shoulder.base.thread.ThreadFacade;
import com.imrub.shoulder.module.photopicker.image.IImage;
import com.imrub.shoulder.module.photopicker.imageUtils.ImageLoader.LoadedCallback;

public class BitmapManager implements IImageLoader{

	private LruCache<String, Bitmap> mCache ;
	private ImageLoader mLoader;
	
//	private static final Animation mAnimation = AnimationUtils.loadAnimation(AppContext.getAppContext(),
//			com.imrub.shoulder.R.anim.photopicker_anim_fade_out);
	
	private static BitmapManager sManager = null;
	private BitmapManager() {
	}

	public Bitmap getThumbnail(ContentResolver cr, long origId, int kind,
			BitmapFactory.Options options, boolean isVideo) {
		if (isVideo) {
			return Video.Thumbnails.getThumbnail(cr, origId, Thread.currentThread().getId(), kind,
					options);
		} else {
			return Images.Thumbnails.getThumbnail(cr, origId, Thread.currentThread().getId(), kind,
					options);
		}
	}

	public static synchronized BitmapManager instance() {
		if (sManager == null) {
			sManager = new BitmapManager();
		}
		return sManager;
	}

	/**
	 * The real place to delegate bitmap decoding to BitmapFactory.
	 */
	public Bitmap decodeFileDescriptor(FileDescriptor fd,
			BitmapFactory.Options options) {
		if (options.mCancel) {
			return null;
		}
		return BitmapFactory.decodeFileDescriptor(fd, null, options);
	}
	
	@Override
    public void onImageLoade(final WeakReference<ImageView> imageView, final IImage image, final int position) {
    	Bitmap bitmap = mCache.get(image.getDataPath());
    	if(bitmap != null){
    		ImageView imageTarget = imageView.get();
    		if(imageTarget != null){
    			imageTarget.setImageBitmap(bitmap);
    			return ;
    		}
    	}
    	
    	mLoader.getBitmap(image, new LoadedCallback() {
			@Override
			public void run(final Bitmap result) {
				if(result == null || mCache == null){
					return ;
				}
				String datapath = image.getDataPath();
				if(datapath != null && result != null){
					mCache.put(datapath, result);
					final ImageView imageViewTarget = imageView.get();
					if(imageViewTarget != null){
						ThreadFacade.runOnUiThread(new Runnable() {
							public void run() {
								imageViewTarget.setImageBitmap(result);
							}
						});
					}
				}
			}
		}, position);
    }
	
	@Override
	public Bitmap getFullBitmap(IImage image) {
		String path = image.getDataPath() + "full";
		final Bitmap b = mCache.get(path);
		if(b == null){
			Bitmap bitmap = image.fullSizeBitmap(IImage.UNCONSTRAINED, AppContext.getScreenWidth()*AppContext.getScreenHeight());
			if(bitmap != null){
				mCache.put(path, bitmap);
				return bitmap;
			}
		}
		return b;
	}
	
	public LruCache<String, Bitmap> getCache(){
		return mCache;
	}
	
	public ImageLoader getImageLoader(){
		return mLoader;
	}
	
	public void onCreate(){
		mLoader = new ImageLoader();
		mCache = new LruCache<String, Bitmap>(1024*1024*5);
	}

	public void startImageLoader(){
		mLoader.start();
	}

	public void stopImageLoader(){
		if(mLoader != null){
			mLoader.stop();
		}
	}
	
	public void onDestory(){
		mLoader.stop();
		mLoader = null;
    	mCache.evictAll();
    	mCache = null;
	}
	
}
