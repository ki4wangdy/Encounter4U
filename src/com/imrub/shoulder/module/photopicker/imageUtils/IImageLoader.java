package com.imrub.shoulder.module.photopicker.imageUtils;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.imrub.shoulder.module.photopicker.image.IImage;

public interface IImageLoader {
	public void onImageLoade(WeakReference<ImageView> imageView, IImage image, int position);
	public Bitmap getFullBitmap(IImage image);
}
